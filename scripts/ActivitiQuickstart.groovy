/* Copyright 2011 the original author or authors.
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
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 5.5
*/

includeTargets << grailsScript("Init")

target(main: "Install Activiti taskforms directory, configurations, index.gsp, logo and favicon") {
	event('StatusFinal', ['Initializing Activiti ...'])
	ant.mkdir(dir:"${basedir}/src/taskforms")
	updateConfig()
	// Backup existing files
	if (new File("${basedir}/grails-app/views/index.gsp").exists())
	  ant.move file:"${basedir}/grails-app/views/index.gsp", tofile:"${basedir}/grails-app/views/index.bak"
	ant.move file:"${basedir}/web-app/images/grails_logo.png", tofile:"${basedir}/web-app/images/grails_logo.png.bak"
	ant.move file:"${basedir}/web-app/images/favicon.ico", tofile:"${basedir}/web-app/images/favicon.ico.bak"
	
	// Copy plugin related files
	ant.copy file:"${activitiPluginDir}/grails-app/views/index.gsp", todir:"${basedir}/grails-app/views", overwrite: true
	ant.copy file:"${activitiPluginDir}/web-app/images/grails_activiti_logo.png", tofile:"${basedir}/web-app/images/grails_logo.png", overwrite: true
	ant.copy file:"${activitiPluginDir}/web-app/images/grails_activiti_favicon.ico", tofile:"${basedir}/web-app/images/favicon.ico", overwrite: true
	event('StatusFinal', ['... finished initializing Activiti.'])
}

private void updateConfig() {
	def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
	if (configFile.exists() && configFile.text.indexOf("activiti") == -1) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Grails Activiti plugin:'
			it.writeLine '''activiti {
	processEngineName = "activiti-engine-default"
	  databaseType = "h2"
	  deploymentName = appName
	  deploymentResources = ["file:./grails-app/conf/**/*.bpmn*.xml",
							 "file:./grails-app/conf/**/*.png",
							 "file:./src/taskforms/**/*.form"]
	  jobExecutorActivate = false
	  mailServerHost = "smtp.yourserver.com"
	  mailServerPort = "25"
	  mailServerUsername = ""
	  mailServerPassword = ""
	  mailServerDefaultFrom = "username@yourserver.com"
	  history = "audit" // "none", "activity", "audit" or "full"
	  sessionUsernameKey = "username"
	  useFormKey = true
}

environments {
	development {
		activiti {
			  processEngineName = "activiti-engine-dev"
			  databaseSchemaUpdate = true // true, false or "create-drop"
		}
	}
	test {
		activiti {
			  processEngineName = "activiti-engine-test"
			  databaseSchemaUpdate = true
		  mailServerPort = "5025"
		}
	}
	production {
		activiti {
			  processEngineName = "activiti-engine-prod"
			  databaseSchemaUpdate = false
			  jobExecutorActivate = true
		}
	}
}
'''

println '''
************************************************************
* Your grails-app/conf/Config.groovy has been updated with *
* default configurations of Activiti.                      *
************************************************************
'''
		}
	}
}
setDefaultTarget(main)
