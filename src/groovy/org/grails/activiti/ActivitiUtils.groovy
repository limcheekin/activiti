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
import org.codehaus.groovy.grails.commons.ApplicationHolder

class ActivitiUtils {
	static def context = ApplicationHolder.getApplication().getMainContext()
	
	static getActivitiService() {
		context.getBean("activitiService")
	}
	
	static getIdentityService() {
		context.getBean("identityService")
	}
	
	static getProcessEngine() {
		context.getBean("processEngine")
	}
	
	static getRuntimeService() {
		context.getBean("runtimeService")
	}
	
	static getRepositoryService() {
		context.getBean("repositoryService")
	}														
	
	static getTaskService() {
		context.getBean("taskService")
	}			
	
	static getManagementService() {
		context.getBean("managementService")
	}	
	
	static getHistoryService() {
		context.getBean("historyService")
	}
	
	static getFormService() {
		context.getBean("formService")
	}	
}
