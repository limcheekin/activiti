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
 
<%@ page import="vacationRequest.VacationRequest" %>
<%@ page import="org.grails.activiti.ApprovalStatus" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'vacationRequest.label', default: 'VacationRequest')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
			<div class="nav" role="navigation">
			 <ul>  
            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="list" controller="task" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></li>
            <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            <li><g:link class="create" action="start"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			 </ul>
			</div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: vacationRequestInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.employeeName.label" default="Employee Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: vacationRequestInstance, field: "employeeName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.numberOfDays.label" default="Number Of Days" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: vacationRequestInstance, field: "numberOfDays")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.vacationDescription.label" default="Vacation Description" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: vacationRequestInstance, field: "vacationDescription")}</td>
                            
                        </tr>
											                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.approvalStatus.label" default="Approval Status" /></td>
                            
                            <td valign="top" class="value">${vacationRequestInstance?.approvalStatus?.encodeAsHTML()}</td>
                            
                        </tr>
		                   <g:if test="${params.isApproval || vacationRequestInstance?.approvalStatus == ApprovalStatus.REJECTED}">  
		                        <tr class="prop">
		                            <td valign="top" class="name"><g:message code="vacationRequest.approvalRemark.label" default="Approval Remark" /></td>
		                            
		                            <td valign="top" class="value">${fieldValue(bean: vacationRequestInstance, field: "approvalRemark")}</td>
		                            
		                        </tr>
                        </g:if>
                    		<g:if test="${!params.isApproval && vacationRequestInstance?.approvalStatus == ApprovalStatus.REJECTED}">      
		                        <tr class="prop">
		                            <td valign="top" class="name"><g:message code="vacationRequest.resendRequest.label" default="Resend Request" /></td>
		                            
		                            <td valign="top" class="value"><g:formatBoolean boolean="${vacationRequestInstance?.resendRequest}" /></td>
		                        </tr>
                    		</g:if>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${vacationRequestInstance?.dateCreated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="vacationRequest.lastUpdated.label" default="Last Updated" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${vacationRequestInstance?.lastUpdated}" /></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <g:if test="${!params.complete && params.taskId}">
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${vacationRequestInstance?.id}" />
                    <g:hiddenField name="taskId" value="${params.taskId}" />
                    <span class="button"><g:actionSubmit class="edit" action="${params.isApproval?'approval':'edit'}" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
            </g:if>            			
        </div>
    </body>
</html>
