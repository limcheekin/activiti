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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'vacationRequest.label', default: 'VacationRequest')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
			<div class="nav" role="navigation">
			 <ul>  
            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
						<li><g:link class="list" controller="task" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></li>
            <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			  </ul>
			</div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${vacationRequestInstance}">
            <div class="errors">
                <g:renderErrors bean="${vacationRequestInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="employeeName"><g:message code="vacationRequest.employeeName.label" default="Employee Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: vacationRequestInstance, field: 'employeeName', 'errors')}">
                                    <g:textField name="employeeName" maxlength="50" value="${vacationRequestInstance?.employeeName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numberOfDays"><g:message code="vacationRequest.numberOfDays.label" default="Number Of Days" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: vacationRequestInstance, field: 'numberOfDays', 'errors')}">
                                    <g:select name="numberOfDays" from="${1..14}" value="${fieldValue(bean: vacationRequestInstance, field: 'numberOfDays')}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="vacationDescription"><g:message code="vacationRequest.vacationDescription.label" default="Vacation Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: vacationRequestInstance, field: 'vacationDescription', 'errors')}">
                                    <g:textArea name="vacationDescription" cols="40" rows="5" value="${vacationRequestInstance?.vacationDescription}" />
                                </td>
                            </tr>
                        

                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                    <span class="button"><g:submitButton name="complete" class="save" value="${message(code: 'default.button.complete.label', default: 'Complete')}" /></span>
                </div>
                <g:hiddenField name="taskId" value="${params.taskId}" />
            </g:form>
        </div>
    </body>
</html>
