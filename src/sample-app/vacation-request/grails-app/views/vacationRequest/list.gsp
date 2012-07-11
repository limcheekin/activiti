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
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
			<div class="nav" role="navigation">
			 <ul>  
			    <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
			    <li><g:link class="list" controller="task" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></li>            
			    <li><g:link class="create" action="start"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			  </ul>
			</div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'vacationRequest.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="employeeName" title="${message(code: 'vacationRequest.employeeName.label', default: 'Employee Name')}" />
                        
                            <g:sortableColumn property="numberOfDays" title="${message(code: 'vacationRequest.numberOfDays.label', default: 'Number Of Days')}" />
                        
                            <g:sortableColumn property="vacationDescription" title="${message(code: 'vacationRequest.vacationDescription.label', default: 'Vacation Description')}" />
                        
                            <g:sortableColumn property="approvalStatus" title="${message(code: 'vacationRequest.approvalStatus.label', default: 'Approval Status')}" />
                        
                            <g:sortableColumn property="approvalRemark" title="${message(code: 'vacationRequest.approvalRemark.label', default: 'Approval Remark')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${vacationRequestInstanceList}" status="i" var="vacationRequestInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${vacationRequestInstance.id}">${fieldValue(bean: vacationRequestInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: vacationRequestInstance, field: "employeeName")}</td>
                        
                            <td>${fieldValue(bean: vacationRequestInstance, field: "numberOfDays")}</td>
                        
                            <td>${fieldValue(bean: vacationRequestInstance, field: "vacationDescription")}</td>
                        
                            <td>${fieldValue(bean: vacationRequestInstance, field: "approvalStatus")}</td>
                        
                            <td>${fieldValue(bean: vacationRequestInstance, field: "approvalRemark")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${vacationRequestInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
