<%--
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
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
 --%>
 
<%@ page import="org.activiti.engine.task.Task" %>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<%@ page import="org.grails.activiti.ActivitiConstants" %>

<g:set var="sessionUsernameKey" value="${ConfigurationHolder.config.activiti.sessionUsernameKey?:ActivitiConstants.DEFAULT_SESSION_USERNAME_KEY}" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="myTasks.title" default="My Tasks of {0}" args="[session[sessionUsernameKey]]"/></title>
    </head>
    <body>
        <div class="nav">
    			<g:render template="navigation" />
    		</div>
        <div class="body">
            <h1><g:message code="myTasks.title" default="My Tasks of {0}" args="[session[sessionUsernameKey]]"/></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'task.id.label', default: 'Id.')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'task.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'task.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="priority" title="${message(code: 'task.priority.label', default: 'Priority')}" />
                            
                            <g:sortableColumn property="createTime" title="${message(code: 'task.createTime.label', default: 'Create Time')}" />
      											
      											<th>Action</th>                                          
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${myTasks}" status="i" var="taskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="getForm" params="[taskId:taskInstance.id]">${fieldValue(bean: taskInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: taskInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: taskInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: taskInstance, field: "priority")}</td>
                            
                            <td><g:formatDate date="${taskInstance.createTime}" /></td>
                            
                            <td>
                             		<g:form action="revokeTask" >
                             				<g:hiddenField name="taskId" value="${taskInstance.id}" />
                             				<span class="button"><g:submitButton style="font-weight:bold" name="revoke" value="${message(code: 'default.button.revoke.label', default: 'Revoke')}" /></span>                           			
                             		</g:form>                  		
                            </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${myTasksCount}" />
            </div>
        </div>
    </body>
</html>
