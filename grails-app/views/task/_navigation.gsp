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
<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
<span class="menuButton"><g:link class="list" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></span>
<span class="menuButton"><g:link class="list" action="unassignedTaskList"><g:message code="unassignedTasks.label" default="Unassigned Tasks ({0})" args="[unassignedTasksCount]" /></g:link></span>
<span class="menuButton"><g:link class="list" action="allTaskList"><g:message code="allTasks.label" default="All Tasks ({0})" args="[allTasksCount]" /></g:link></span>

