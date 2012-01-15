/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Gant script that generates a CRUD controller and matching views for a given domain class
 *
 * @author Graeme Rocher
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 * 
 * @since 5.0.beta2
 */
import org.codehaus.groovy.grails.scaffolding.*
import grails.util.GrailsNameUtils
import groovy.text.*;
import org.apache.commons.logging.Log;
import org.springframework.core.io.*
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.scaffolding.GrailsTemplateGenerator;
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.util.BuildSettingsHolder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.context.ResourceLoaderAware;
import grails.util.BuildSettingsHolder as build

includeTargets << grailsScript("_GrailsBootstrap")

generateForName = null
generateViews = true
generateController = true

target(generateForOne: "Generates controllers and views for only one domain class.") {
    depends(loadApp)

    def name = generateForName
    name = name.indexOf('.') > -1 ? name : GrailsNameUtils.getClassNameRepresentation(name)
    def domainClass = grailsApp.getDomainClass(name)

    if (!domainClass) {
        println "Domain class not found in grails-app/domain, trying hibernate mapped classes..."
        bootstrap()
        domainClass = grailsApp.getDomainClass(name)
    }

    if (domainClass) {
        generateForDomainClass(domainClass)
        event("StatusFinal", ["Finished generation for domain class ${domainClass.fullName}"])
    }
    else {
        event("StatusFinal", ["No domain class found for name ${name}. Please try again and enter a valid domain class name"])
		exit(1)
    }
}

target(uberGenerate: "Generates controllers and views for all domain classes.") {
    depends(loadApp)

    def domainClasses = grailsApp.domainClasses

    if (!domainClasses) {
        println "No domain classes found in grails-app/domain, trying hibernate mapped classes..."
        bootstrap()
        domainClasses = grailsApp.domainClasses
    }

    if (domainClasses) {
        domainClasses.each { domainClass -> generateForDomainClass(domainClass) }
        event("StatusFinal", ["Finished generation for domain classes"])
    }
    else {
        event("StatusFinal", ["No domain classes found"])
    }
}

def generateForDomainClass(domainClass) {
    def templateGenerator = new CustomGrailsTemplateGenerator(classLoader)
    templateGenerator.templatesDirName = build.settings.config.grails.templates.dir.name?:"activiti-templates"
    templateGenerator.basedir = basedir
	  templateGenerator.pluginBasedir = activitiPluginDir
	  if (generateViews) {
        event("StatusUpdate", ["Generating views for domain class ${domainClass.fullName}"])
        templateGenerator.generateViews(domainClass, basedir)
        event("GenerateViewsEnd", [domainClass.fullName])
    }

    if (generateController) {
        event("StatusUpdate", ["Generating controller for domain class ${domainClass.fullName}"])
        templateGenerator.generateController(domainClass, basedir)
        createUnitTest(name: domainClass.fullName, suffix: "Controller",
                       superClass: "ControllerUnitTestCase")
        event("GenerateControllerEnd", [domainClass.fullName])
    }
}

class CustomGrailsTemplateGenerator implements GrailsTemplateGenerator, ResourceLoaderAware {

    static final Log LOG = LogFactory.getLog(CustomGrailsTemplateGenerator.class);

    String basedir = "."
	  String pluginBasedir
    boolean overwrite = false
    def engine = new SimpleTemplateEngine()
    def ant = new AntBuilder()
    ResourceLoader resourceLoader
    Template renderEditorTemplate
    static String activitiTemplatesDirName = "activiti-templates"
    String templatesDirName	= activitiTemplatesDirName
    	


    /**
     * Creates an instance for the given class loader
     */
    CustomGrailsTemplateGenerator(ClassLoader classLoader) {
        engine = new SimpleTemplateEngine(classLoader)
    }

    /**
     * Creates an instance
     */
    CustomGrailsTemplateGenerator() {
    }



    void setResourceLoader(ResourceLoader rl) {
        LOG.info "Scaffolding template generator set to use resource loader ${rl}"
        this.resourceLoader = rl
    }

    // a closure that uses the type to render the appropriate editor
    def renderEditor = {property ->
        def domainClass = property.domainClass
        def cp = domainClass.constrainedProperties[property.name]

        if (!renderEditorTemplate) {
            // create template once for performance
            def templateText = getTemplateText("renderEditor.template")
            renderEditorTemplate = engine.createTemplate(templateText)
        }

        def binding = [property: property, domainClass: domainClass, cp: cp, domainInstance:getPropertyName(domainClass)]
        return renderEditorTemplate.make(binding).toString()
    }

    public void generateViews(GrailsDomainClass domainClass, String destdir) {
        if (!destdir)
            throw new IllegalArgumentException("Argument [destdir] not specified")

        def viewsDir = new File("${destdir}/grails-app/views/${domainClass.propertyName}")
        if (!viewsDir.exists())
            viewsDir.mkdirs()

        def templateNames = getTemplateNames()

        for(t in templateNames) {
           LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
           generateView domainClass, t, viewsDir.absolutePath
        }

    }

    public void generateController(GrailsDomainClass domainClass, String destdir) {
        if (!destdir)
            throw new IllegalArgumentException("Argument [destdir] not specified")

        if (domainClass) {
            def fullName = domainClass.fullName
            def pkg = ""
            def pos = fullName.lastIndexOf('.')
            if (pos != -1) {
                // Package name with trailing '.'
                pkg = fullName[0..pos]
            }

            def destFile = new File("${destdir}/grails-app/controllers/${pkg.replace('.' as char, '/' as char)}${domainClass.shortName}Controller.groovy")
            if (canWrite(destFile)) {
                destFile.parentFile.mkdirs()

                destFile.withWriter {w ->
                    generateController(domainClass, w)
                }

                LOG.info("Controller generated at ${destFile}")
            }
        }
    }




    public void generateView(GrailsDomainClass domainClass, String viewName, String destDir) {
		    LOG.debug "generateView($domainClass, $viewName, $destDir)"
        def viewFile = new File("$destDir/${viewName}.gsp")
		    if (canWrite(viewFile)) {
		    	viewFile.withWriter { Writer writer ->
            generateView domainClass, viewName, writer 
                    }
				  LOG.info("${viewName} view generated at ${viewFile.absolutePath}")
		        }
    }

    void generateView(GrailsDomainClass domainClass, String viewName, Writer out) {
		  
        def templateText = getTemplateText("${viewName}.gsp")
        def t = engine.createTemplate(templateText)
        def multiPart = domainClass.properties.find {it.type == ([] as Byte[]).class || it.type == ([] as byte[]).class}

        def packageName = domainClass.packageName ? "<%@ page import=\"${domainClass.fullName}\" %>" : ""
        def binding = [packageName: packageName,
                domainClass: domainClass,
                multiPart: multiPart,
                className: domainClass.shortName,
                propertyName:  getPropertyName(domainClass),
                renderEditor: renderEditor,
                comparator: org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator.class]

        t.make(binding).writeTo(out)
    }

    void generateController(GrailsDomainClass domainClass, Writer out) {
        def templateText = getTemplateText("Controller.groovy")
               
        def binding = [packageName: domainClass.packageName,
                domainClass: domainClass,
                className: domainClass.shortName,
                propertyName: getPropertyName(domainClass),
                comparator: org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator.class]

        def t = engine.createTemplate(templateText)
        t.make(binding).writeTo(out)
    }

    private def getPropertyName(GrailsDomainClass domainClass) {
        return domainClass.propertyName + 'Instance'
    }

    private canWrite(testFile) {
        if (!overwrite && testFile.exists()) {
            try {
                ant.input(message: "File ${testFile} already exists. Overwrite?", "y,n,a", addproperty: "overwrite.${testFile.name}")
                overwrite = (ant.antProject.properties."overwrite.${testFile.name}" == "a") ? true : overwrite
                return overwrite || ((ant.antProject.properties."overwrite.${testFile.name}" == "y") ? true : false)
            } catch (Exception e) {
                // failure to read from standard in means we're probably running from an automation tool like a build server
                return true
            }
        }
        return true
    }

    private getTemplateText(String template) {
        def application = ApplicationHolder.getApplication()
        // first check for presence of template in application
        if (resourceLoader && application?.warDeployed) {
            return resourceLoader.getResource("/WEB-INF/${templatesDirName}/scaffolding/${template}").inputStream.text
        }
        else {
            def templateFile = new FileSystemResource("${basedir}/src/${templatesDirName}/scaffolding/${template}")
            if (!templateFile.exists()) {
				        LOG.debug "getTemplateText() ${pluginBasedir}/src/${templatesDirName}/scaffolding/${template}"
			          templateFile = new FileSystemResource("${pluginBasedir}/src/${activitiTemplatesDirName}/scaffolding/${template}")
                       }
            if (!templateFile.exists()) {
                // template not found in application, use default template
                def grailsHome = BuildSettingsHolder.settings?.grailsHome

                if (grailsHome) {
                    templateFile = new FileSystemResource("${grailsHome}/src/grails/${templatesDirName}/scaffolding/${template}")
                }
                else {
                    templateFile = new ClassPathResource("src/grails/${templatesDirName}/scaffolding/${template}")
                }
            }
            return templateFile.inputStream.getText()
        }
    }

    def getTemplateNames() {
        def resources = []
        Closure filter = { it[0..-5] }
        if(resourceLoader && application?.isWarDeployed()) {
            def resolver = new PathMatchingResourcePatternResolver(resourceLoader)
            try {
                resources = resolver.getResources("/WEB-INF/${templatesDirName}/scaffolding/*.gsp").filename.collect(filter)
            }
            catch (e) {
                return []
            }
        }
        else {
			      resources = getResources("${basedir}/src/${templatesDirName}/scaffolding", filter)
            if (resources) return resources
			      resources = getResources("${pluginBasedir}/src/${activitiTemplatesDirName}/scaffolding", filter)
            if (resources) return resources			
            def grailsHome = BuildSettingsHolder.settings?.grailsHome
            if(grailsHome) {
                try {
                    def grailsHomeTemplates = resolver.getResources("file:${grailsHome}/src/grails/${templatesDirName}/scaffolding/*.gsp").filename.collect(filter)
                    resources.addAll(grailsHomeTemplates)
                }
                catch (e) {
                    // ignore                    
                    LOG.debug("Error locating templates from GRAILS_HOME: ${e.message}", e)
                }
            }
            else {
                try {
                    def templates = resolver.getResources("classpath:src/grails/${templatesDirName}/scaffolding/*.gsp").filename.collect(filter)
                    resources.addAll(templates)
                }
                catch (e) {
                    // ignore
                    LOG.debug("Error locating templates from classpath: ${e.message}", e)
                }
            }
        }
        return resources
    }

    private getResources(String templatesDirPath, Closure filter) {
		    def resources
        def resolver = new PathMatchingResourcePatternResolver()
        def templatesDir = new FileSystemResource(templatesDirPath)
        if(templatesDir.exists()) {
            try {
                resources = resolver.getResources("file:$templatesDirPath/*.gsp").filename.collect(filter)
            }
            catch (e) {
                // ignore
            }
        }
		return resources		
	}

}
