/* Copyright 2010 the original author or authors.
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
 * Configuration for plugin testing - will not be included in the plugin zip
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */

log4j = {
	// Example of changing the log pattern for the default console
	// appender:
	//
	//appenders {
	//    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
	//}
	
	error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
	'org.codehaus.groovy.grails.web.pages', //  GSP
	'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
	'org.codehaus.groovy.grails.web.mapping', // URL mapping
	'org.codehaus.groovy.grails.commons', // core / classloading
	'org.codehaus.groovy.grails.plugins', // plugins
	'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	'org.springframework',
	'org.hibernate',
	'net.sf.ehcache.hibernate'
	
	warn   'org.mortbay.log'
}

activiti {
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
			  databaseSchemaUpdate = true // false, true or "create-drop"	  
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

// The following properties have been added by the Upgrade process...
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
