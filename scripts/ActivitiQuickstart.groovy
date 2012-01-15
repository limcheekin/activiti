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

target(main: "Install plugin's index.gsp, logo, favicon and default H2 DataSource.grooy") {
	// Backup existing files
	if (new File("${basedir}/grails-app/views/index.gsp").exists())
	  ant.move file:"${basedir}/grails-app/views/index.gsp", tofile:"${basedir}/grails-app/views/index.bak"
	ant.move file:"${basedir}/web-app/images/grails_logo.png", tofile:"${basedir}/web-app/images/grails_logo.png.bak"
	ant.move file:"${basedir}/web-app/images/favicon.ico", tofile:"${basedir}/web-app/images/favicon.ico.bak"
	ant.move file:"${basedir}/grails-app/conf/DataSource.groovy", tofile:"${basedir}/grails-app/conf/DataSource.bak"
	
	// Copy plugin related files
	ant.copy file:"${activitiPluginDir}/grails-app/views/index.gsp", todir:"${basedir}/grails-app/views", overwrite: true
	ant.copy file:"${activitiPluginDir}/web-app/images/grails_activiti_logo.png", tofile:"${basedir}/web-app/images/grails_logo.png", overwrite: true
	ant.copy file:"${activitiPluginDir}/web-app/images/grails_activiti_favicon.ico", tofile:"${basedir}/web-app/images/favicon.ico", overwrite: true
	ant.copy file:"${activitiPluginDir}/src/activiti-quickstart/DataSource.groovy", todir:"${basedir}/grails-app/conf", overwrite: true
}

setDefaultTarget(main)
