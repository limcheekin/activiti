/* Copyright 2010-2012 the original author or authors.
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
 *
 * Deployment script to deploy business archive (BAR).
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 * 
 */
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.util.BuildSettingsHolder as build

includeTargets << grailsScript("Init")
includeTargets << grailsScript("Compile")
includeTargets << grailsScript("_GrailsPackage")

ant.property(environment:"env")
ant.property (file : "application.properties")
appName = ant.project.properties.'app.name'
appVersion = ant.project.properties.'app.version'

target(deploy: "Deploy Activiti Business Archive (BAR) and JAR") {
	depends(clean, compile, createBar)
	event("DeployBarStart", [])
	rootLoader.addURL(new File("${activitiPluginDir}/grails-app/conf").toURL())
	rootLoader.addURL(build.settings.classesDir.toURL())
	ant.taskdef (name: 'deployBar', classname : "org.activiti.engine.impl.ant.DeployBarTask")
	try {
		ant.deployBar (processEngineName:config.activiti.processEngineName, file: "${basedir}/target/${appName}-${appVersion}.bar")
	} catch (Exception e) {
		e.printStackTrace()
	} finally {
		event("DeployBarEnd", [])
	}
}

target(createBar: "Create Activiti Business Archive (BAR) that contains process files and process resources") {
	createConfig()
	ant.delete dir:"${basedir}/target/bar"
	ant.mkdir dir:"${basedir}/target/bar"
	deploymentResources = config.activiti.deploymentResources
	if (deploymentResources) {
		deploymentResources = [deploymentResources].flatten()
	} else {
	  event('StatusFinal', ['deploymentResources not configured. Use ActivitiConstants.DEFAULT_DEPLOYMENT_RESOURCES...'])
	  ActivitiConstants = classLoader.loadClass("org.grails.activiti.ActivitiConstants")
	  deploymentResources = ActivitiConstants.DEFAULT_DEPLOYMENT_RESOURCES
	}
	event('StatusUpdate', ["deploymentResources = $deploymentResources"])
	resolver = new PathMatchingResourcePatternResolver()
	deploymentResources.each { resource ->
	    resources = resolver.getResources(resource)  
		  resources.each { 
			   ant.copy file:it.file.absolutePath, todir:"${basedir}/target/bar"
		  }      
	}
	ant.jar (destfile: "${basedir}/target/${appName}-${appVersion}.bar") {
		fileset dir:"${basedir}/target/bar"
	}	
}


setDefaultTarget(deploy)