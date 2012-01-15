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
 
<%@ page import="org.grails.activiti.ActivitiUtils" %>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<%@ page import="org.grails.activiti.ActivitiConstants" %>

<g:set var="sessionUsernameKey" value="${ConfigurationHolder.config.activiti.sessionUsernameKey?:ActivitiConstants.DEFAULT_SESSION_USERNAME_KEY}" />
<html>
    <head>
        <title>Welcome to Grails Activiti Plugin</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">

        #nav {
            margin-top:20px;
            margin-left:30px;
            width:228px;
            float:left;

        }
        .homePagePanel * {
            margin:0px;
        }
        .homePagePanel .panelBody ul {
            list-style-type:none;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody h1 {
            text-transform:uppercase;
            font-size:1.1em;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody {
            background: url(images/leftnav_midstretch.png) repeat-y top;
            margin:0px;
            padding:15px;
        }
        .homePagePanel .panelBtm {
            background: url(images/leftnav_btm.png) no-repeat top;
            height:20px;
            margin:0px;
        }

        .homePagePanel .panelTop {
            background: url(images/leftnav_top.png) no-repeat top;
            height:11px;
            margin:0px;
        }
        h2 {
            margin-top:15px;
            margin-bottom:15px;
            font-size:1.2em;
        }
        #pageBody {
            margin-left:280px;
            margin-right:20px;
        }
        </style>
   
    </head>
    <body>
        <div id="nav">
            <div class="homePagePanel">
                <div class="panelTop"></div>
                <div class="panelBody">
                    <h1>Application Status</h1>
                    <ul>
                        <li>App version: <g:meta name="app.version"></g:meta></li>
                        <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
                        <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                        <li>JVM version: ${System.getProperty('java.version')}</li>
                        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
                    </ul>
                    <h1>Installed Plugins</h1>
                    <ul>
                        <g:set var="pluginManager"
                               value="${applicationContext.getBean('pluginManager')}"></g:set>

                        <g:each var="plugin" in="${pluginManager.allPlugins}">
                            <li>${plugin.name} - ${plugin.version}</li>
                        </g:each>

                    </ul>
                </div>
                <div class="panelBtm"></div>
            </div>
        </div>
        <div id="pageBody">
            <h1>Welcome to Grails Activiti Plugin</h1>
            <p>Congratulations, you have successfully started your first Grails Activiti application! 
            Some files are overwritten during the installation of Grails Activiti Plugin (including the one you are reading now), 
            you need not to worry as the original version of overwritten files are backup as same file name with .bak file extension 
            in the same directory, you can restore to original version of the overwritten file as necessary.
            This is the default page, feel free to modify it to either redirect to a controller or display whatever
            content you may choose.
            </p>
            <br /> 
            <p>
            <g:if test="${pluginManager.hasGrailsPlugin('activitiSpringSecurity')}">
            Please select LoginController and login with valid username/password such as <strong>kermit/kermit, peter/peter</strong> or <strong>fozzie/fozzie</strong>.
            </g:if>
            <g:else>
            Below is a list of activiti users, you need to select an user as identity 
            from the combo box before you can start using Grails Activiti related functionality.
            </g:else>
            </p> 
            <br />
            <p>Next, you will see the Activiti Controllers section, 
            you can click on TaskController to start browsing the task list of the user or you can click on "Start" of other Activiti 
            controllers to start process and working on task form. Further below is list of other controllers, click on each to execute its default action:
            </p>
            <g:if test="${!pluginManager.hasGrailsPlugin('activitiSpringSecurity')}">
             <div id="userList" class="dialog">
                <h2>Activiti Users:</h2>
                				<%
										def userList=[:]
										def identityService = ActivitiUtils.identityService
								    def users = identityService.createUserQuery().orderByUserId().asc().list()
										for (user in users) {
                        def groups = identityService.createGroupQuery().groupMember(user.id).orderByGroupId().asc().list()
                        def groupIds = groups?" ${groups.collect{it.id}}":""
										    userList[user.id]="${user.id}${groupIds}"
                                        }		
								 %>
                <g:set var="${sessionUsernameKey}" value="${params.username}" scope="session" />
                								 
								<g:form>
                	<g:select name="username" from="${userList}" optionKey="key" 
                		optionValue="value" noSelection="['null': '[Select User]']"
                		onchange="this.form.submit();" value="${session[sessionUsernameKey]}"/>	
                </g:form>
            </div>
            </g:if>
            <g:if test="${session[sessionUsernameKey]}">
      			<br />Current User: <strong>${session[sessionUsernameKey]}</strong>
            <div id="controllerList" class="dialog">
                <h2>Activiti Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                    		<g:if test="${c.hasProperty('activiti') && c.clazz.activiti}">
                       	 <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link> 
                       	 	<g:if test="${!c.logicalPropertyName.equals('task')}">
                       	 		[<g:link controller="${c.logicalPropertyName}" action="start">Start</g:link>]
                       	 	</g:if>
                       	</li>
                       	</g:if>
                    </g:each>
                </ul>
            </div>        
             </g:if>    
            <div id="controllerList" class="dialog">
                <h2>Other Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                    		<g:if test="${!c.hasProperty('activiti')}">
                       	 	<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
                       	</g:if>
                    </g:each>
                </ul>
            </div>            

        </div>
    </body>
</html>
