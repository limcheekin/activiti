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
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
includeTargets << grailsScript("Init")
includeTargets << grailsScript("_PluginDependencies")

packageName = ''
vacationRequestDir="${activitiPluginDir}/src/sample-app/vacation-request"

target(install: "Install Vacation Request Sample Application") {	
	 if (getPluginDirForName('spring-security-core')) {
		 installSpringSecurityCoreFiles()
	 } else {
	   installStandardConfigFiles()
	 }
		ant.copy (todir:"${basedir}/grails-app/controllers", overwrite: true) {
			fileset dir:"${vacationRequestDir}/grails-app/controllers"
		}		
		ant.copy (todir:"${basedir}/grails-app/domain", overwrite: true) {
			fileset dir:"${vacationRequestDir}/grails-app/domain/standard"
		}
		ant.copy (todir:"${basedir}/grails-app/views", overwrite: true) {
			fileset dir:"${vacationRequestDir}/grails-app/views"
		}						
		ant.copy (todir:"${basedir}/src/groovy", overwrite: true) {
			fileset dir:"${vacationRequestDir}/src/groovy"
		}
		event("StatusUpdate", [ "Vacation Request Sample Application installed."])
}

private installStandardConfigFiles() {
	ant.copy (todir:"${basedir}/grails-app/conf", overwrite: true) {
		fileset dir:"${vacationRequestDir}/grails-app/conf/standard"
	}
}

private installSpringSecurityCoreFiles() {
	ant.input(message:"Enter package name for User and Role domain classes:", addproperty:"packageName")
	packageName = ant.antProject.properties["packageName"]
	
	ant.copy (todir:"${basedir}/grails-app/conf", overwrite: true) {
		fileset dir:"${vacationRequestDir}/grails-app/conf/springSecurity"
	}
	if (packageName) {
	  ant.replaceregexp match:"@import@", replace:"import ${packageName}.*", flags:"g", byline:"true", file:"${basedir}/grails-app/conf/VacationRequestBootStrap.groovy"
	}
	copyControllersAndViews()
	makeUserAndRoleDomainClasses()
	updateConfig()
}

private makeUserAndRoleDomainClasses() {
	String dir = packageToDir(packageName)
	String domainClassesDir = "${basedir}/grails-app/domain/$dir" 
	ant.mkdir dir: domainClassesDir
	ant.copy (todir:domainClassesDir) {
		fileset dir:"${vacationRequestDir}/grails-app/domain/springSecurity"
	}
	["User.groovy", "UserRole.groovy", "Role.groovy"].each {	
	  ant.replaceregexp match:"@package@", replace:"package ${packageName}", flags:"g", byline:"true", file:"$domainClassesDir$it"
	}	
}

packageToDir = { String packageName ->
	String dir = ''
	if (packageName) {
		dir = packageName.replaceAll('\\.', '/') + '/'
	}

	return dir
}

// copied from Spring Security Core Plugin: S2Quickstart.groovy
private void copyControllersAndViews() {
    String springSecurityCorePluginDir = getPluginDirForName('spring-security-core').path
	  templateDir = "$springSecurityCorePluginDir/src/templates"
	  appDir = "$basedir/grails-app"
	  ant.mkdir dir: "$appDir/views/login"
	  ant.copy file: "$templateDir/auth.gsp.template", tofile: "$appDir/views/login/auth.gsp"
	  ant.copy file: "$templateDir/denied.gsp.template", tofile: "$appDir/views/login/denied.gsp"
	  ant.copy file: "$templateDir/LoginController.groovy.template", tofile: "$appDir/controllers/LoginController.groovy"
	  ant.copy file: "$templateDir/LogoutController.groovy.template", tofile: "$appDir/controllers/LogoutController.groovy"
  }
  

private void updateConfig() {
		def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
		if (configFile.exists()) {
			configFile.withWriterAppend {
				it.writeLine '\n// Added by the Spring Security Core plugin:'
				it.writeLine "grails.plugins.springsecurity.userLookup.userDomainClassName = '${packageName}.User'"
				it.writeLine "grails.plugins.springsecurity.userLookup.authorityJoinClassName = '${packageName}.UserRole'"
				it.writeLine "grails.plugins.springsecurity.authority.className = '${packageName}.Role'"
				it.writeLine "grails.plugins.springsecurity.useSecurityEventListener = true"
			}
		}
	}
	

setDefaultTarget(install)
