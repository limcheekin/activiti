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

import org.activiti.engine.task.Task
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import grails.util.GrailsNameUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
class ActivitiService {
	
	def runtimeService
	def taskService
	def identityService
	def formService
	String sessionUsernameKey = CH.config.activiti.sessionUsernameKey?:ActivitiConstants.DEFAULT_SESSION_USERNAME_KEY
	String usernamePropertyName = CH.config.grails.plugins.springsecurity.userLookup.usernamePropertyName
	
	def startProcess(Map params) {
		if (params.businessKey) {
		  runtimeService.startProcessInstanceByKey(params.controller, params.businessKey, params)
		} else {
		  runtimeService.startProcessInstanceByKey(params.controller, params)
		}
	}		
	
	private findTasks(String methodName, String username, int firstResult, int maxResults, Map orderBy) {
		def taskQuery = taskService.createTaskQuery()			
		if (methodName) {
			taskQuery."${methodName}"(username)
		}
		if (orderBy) {
			orderBy.each { k, v ->
				taskQuery."orderByTask${GrailsNameUtils.getClassNameRepresentation(k)}"()."${v}"()
			}
		}			
		taskQuery.listPage(firstResult, maxResults)
	}
	
	private Long getTasksCount(String methodName, String username) {
		def taskQuery = taskService.createTaskQuery()
		if (methodName) {
			taskQuery."${methodName}"(username)
		}
		taskQuery.count()
	} 
	
	def getAssignedTasksCount(String username) {
		getTasksCount("taskAssignee", username)
	}
	
	def getUnassignedTasksCount(String username) {
		getTasksCount("taskCandidateUser", username)
	}
	
	def getAllTasksCount() {
		getTasksCount(null, null)
	}
	
	def findAssignedTasks(Map params) {
		def orderBy = CH.config.activiti.assignedTasksOrderBy?:[:]
		if (params.sort) {
			orderBy << ["${params.sort}":params.order]
		}
		findTasks("taskAssignee", params[sessionUsernameKey], getOffset(params.offset), params.max, orderBy)
	}
	
	def findUnassignedTasks(Map params) {
		def orderBy = CH.config.activiti.unassignedTasksOrderBy?:[:]		
		if (params.sort) {
			orderBy << ["${params.sort}":params.order]
		}
		findTasks("taskCandidateUser", params[sessionUsernameKey], getOffset(params.offset), params.max, orderBy)
	}		
	
	def findAllTasks(Map params) {
		def orderBy = CH.config.activiti.allTasksOrderBy?:[:]		
		if (params.sort) {
			orderBy << ["${params.sort}":params.order]
		}
		findTasks(null, null, getOffset(params.offset), params.max, orderBy)
	}
	
	private getOffset(def offset) {
		return offset?Integer.parseInt(offset):0
	}										  			  				
	
	def deleteTask(String taskId, String domainClassName = null) {
		String id = deleteDomainObject(taskId, domainClassName)
		taskService.deleteTask(taskId)
		return id
	}			  
	
	private deleteDomainObject(String taskId, String domainClassName) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult()
		def id = getDomainObjectId(task)
		if (id) {
			def domainClass = AH.getApplication().classLoader.loadClass(domainClassName?:getDomainClassName(task))
			domainClass.get(id)?.delete(flush: true)
		}
		return id
	}
	
	private getDomainObjectId(Task task) {
		runtimeService.getVariable(task.executionId, "id")
	}
	
	private getDomainClassName(Task task) {
		runtimeService.getVariable(task.executionId, "domainClassName")
	}	   
	
	Task getAssignedTask(String username, String processInstanceId) {
		getTask("taskAssignee", username, processInstanceId)
	}
	
	Task getUnassignedTask(String username, String processInstanceId) {
		getTask("taskCandidateUser", username, processInstanceId)
	}	
	
	private getTask(String methodName, String username, String processInstanceId) {
		taskService.createTaskQuery()
		.processInstanceId(processInstanceId)
		."${methodName}"(username)
		.singleResult()
	}
	
	def claimTask(String taskId, String username) {
		taskService.claim(taskId, username)
	}		
	
	def completeTask(String taskId, Map params) {
		String executionId = taskService.createTaskQuery().taskId(taskId).singleResult().executionId
		setIdAndDomainClassName(executionId, params)
		runtimeService.setVariable(executionId, "uri", null)
		taskService.complete(taskId, params)
	}
	
	private setIdAndDomainClassName(String executionId, Map params) {
		if (params.id) {
			runtimeService.setVariable(executionId, "id", params.id)
			runtimeService.setVariable(executionId, "domainClassName", params.domainClassName as String)
		}
	}
	
	def setTaskFormUri(Map params) {
		String executionId = taskService.createTaskQuery().taskId(params.taskId).singleResult().executionId
		setIdAndDomainClassName(executionId, params)
		if (params.controller && params.action && params.id) {
			runtimeService.setVariable(executionId, "uri", "/${params.controller}/${params.action}/${params.id}")
		}
	}						
	
	String getTaskFormUri(String taskId, boolean useFormKey) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult()
		String taskFormUri = runtimeService.getVariable(task.executionId, "uri")
		if (!taskFormUri) {
			def id = getDomainObjectId(task)?:"" 
			
			if (useFormKey) {
				String formKey = formService.getTaskFormData(taskId).formKey
				if (formKey) {
			    taskFormUri = "${formKey}/${id}"
				} else {
				  taskFormUri = "/${runtimeService.getVariable(task.executionId, 'controller')}/${task.taskDefinitionKey}/${id}"
				}
			} else {
			  taskFormUri = "/${runtimeService.getVariable(task.executionId, 'controller')}/${task.taskDefinitionKey}/${id}"
			}
		}
		if (taskFormUri) {
			taskFormUri += "?taskId=${taskId}"
		}
		return taskFormUri
	}
	
	def setAssignee(String taskId, String username) {
		taskService.setAssignee(taskId, username)
	}
	
	def setPriority(String taskId, int priority) {
		taskService.setPriority(taskId, priority)
	}
	
	def getCandidateUserIds(String taskId) {
		def identityLinks = taskService.getIdentityLinksForTask(taskId)
		def userIds = []
		def users
		identityLinks.each { identityLink -> 
			if (identityLink.groupId) {
				users = identityService.createUserQuery()
					      .memberOfGroup(identityLink.groupId)
								.orderByUserId().asc().list()
				if (AH.application.mainContext.pluginManager.hasGrailsPlugin('activitiSpringSecurity')) {				
			    userIds << users?.collect { it."$usernamePropertyName" }
				} else { 
				  userIds << users?.collect { it.id }
				}
			} else {
			  userIds << identityLink.userId
			}
		}  		
		return userIds.flatten().unique()
	}
}