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
package org.grails.activiti

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
class ActivitiConstants {
	static final List DEFAULT_DEPLOYMENT_RESOURCES = ["file:./grails-app/conf/**/*.bpmn*.xml",
	"file:./grails-app/conf/**/*.png", 
	"file:./src/taskforms/**/*.form"]
	static final String DEFAULT_PROCESS_ENGINE_NAME = "grails-activiti-noconfig"
	static final String DEFAULT_DATABASE_TYPE = "h2"
	static final String DEFAULT_DATABASE_SCHEMA_UPDATE = "false"
	static final String DEFAULT_DEPLOYMENT_NAME = "deploymentName not defined."
	static final Boolean DEFAULT_JOB_EXECUTOR_ACTIVATE = false
	static final String DEFAULT_MAIL_SERVER_HOST = "mailServerHost not defined."
	static final String DEFAULT_MAIL_SERVER_PORT = "mailServerPort not defined."
	static final String DEFAULT_MAIL_SERVER_FROM = "mailServerDefaultFrom not defined."
	static final String PLUGIN_AUTO_DEPLOYMENT_NAME = "ActivitiPluginAutoDeploy"
	static final String DEFAULT_SESSION_USERNAME_KEY = "username"
	static final String DEFAULT_HISTORY = "audit"
	static final boolean DEFAULT_USE_FORM_KEY = false
}
