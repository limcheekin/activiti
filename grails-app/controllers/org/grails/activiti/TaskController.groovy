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
class TaskController {
	static activiti = true
	
	def index = {
		redirect(action: "myTaskList", params: params)
	}
	
	def startTask = {
		startTask(params.taskId)
	}			
	
	def getForm = {
		getForm(params.taskId)
	}			
	
	def unassignedTaskList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[unassignedTasks: findUnassignedTasks(params), 
		unassignedTasksCount: unassignedTasksCount,
		myTasksCount: assignedTasksCount,
		allTasksCount: allTasksCount]
	}		
	
	def myTaskList = {	
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[myTasks: findAssignedTasks(params), 
		unassignedTasksCount: unassignedTasksCount,
		myTasksCount: assignedTasksCount,
		allTasksCount: allTasksCount]
	}		
	
	def allTaskList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)		
		[allTasks: findAllTasks(params), 
		unassignedTasksCount: unassignedTasksCount,
		myTasksCount: assignedTasksCount,
		allTasksCount: allTasksCount]
	}		
	
	def claimTask = {
		claimTask(params.taskId)
		redirect(action: "unassignedTaskList", params: params)
	}
	
	def revokeTask = {
		revokeTask(params.taskId)
		redirect(action: "myTaskList", params: params)
	}
	
	def deleteTask = {
		deleteTask(params.taskId)
		redirect(action: "allTaskList", params: params)
	}	  
	
	def setAssignee = {
		if (params.assignee.equals("null")) params.assignee = null
		setAssignee(params.taskId, params.assignee)
		redirect(action: "allTaskList", params: params)
	}		  
	
	def changePriority = {
		setPriority(params.taskId, Integer.parseInt(params.priority))
		redirect(action: "allTaskList", params: params)
	}
}
