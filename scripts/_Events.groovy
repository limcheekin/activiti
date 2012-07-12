/* Copyright 2010-2011 the original author or authors.
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

 /**
 * This event script improved the TestApp.groovy and DeployBar.groovy script 
 * by generating activiti.properties from activiti configurations in Config.groovy.
 * User of this plugin no longer need to maintain activiti.properties file separately. 
  * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
  *
 * @since 5.0.alpha3
  * 
 */
import grails.util.BuildSettingsHolder as build
import groovy.xml.MarkupBuilder
 
includeTargets << grailsScript("_GrailsPackage")

CONFIG_FILE = "activiti.cfg.xml"

eventTestPhasesStart = {
	ant.echo "eventTestPhasesStart invoked."
	ensureAllGeneratedFilesDeleted()	  
	createActivitiConfigFile(build.settings.resourcesDir.toString(), true)
}

eventTestPhasesEnd = { 
  ant.echo "eventTestPhasesEnd invoked."
	ant.delete file:"${build.settings.resourcesDir}/${CONFIG_FILE}" 
}

eventDeployBarStart = { 
	ant.echo "eventDeployBarStart invoked."
	ensureAllGeneratedFilesDeleted()
  createActivitiConfigFile("${activitiPluginDir}/grails-app/conf", false)
}
 
eventDeployBarEnd = { 
	ant.echo "eventDeployBarEnd invoked."
	ant.delete file:"${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}" 
}

private void ensureAllGeneratedFilesDeleted() {
	if (new File("${build.settings.resourcesDir}/${CONFIG_FILE}").exists()) {
		  ant.delete file:"${build.settings.resourcesDir}/${CONFIG_FILE}" 
	}
	if (new File("${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}").exists()) {
		  ant.delete file:"${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}" 
	}	
}

private void createActivitiConfigFile(String activitiConfigFilePath, boolean isTestEnv) {
	createConfig()
	String processEngineConfigurationClassName = isTestEnv ? "org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration" : "org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration"
	def activitiConfigFile = new File(activitiConfigFilePath, CONFIG_FILE)
	activitiConfigFile.withWriter {
		it.writeLine """<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="processEngineConfiguration" class="${processEngineConfigurationClassName}">
  
    <property name="processEngineName" value="${config.activiti.processEngineName}" />
    <property name="databaseType" value="${config.activiti.databaseType}" />
    <property name="jdbcUrl" value="${config.dataSource.url}" />
    <property name="jdbcDriver" value="${config.dataSource.driverClassName}" />
    <property name="jdbcUsername" value="${config.dataSource.username}" />
    <property name="jdbcPassword" value="${config.dataSource.password}" />
    
    <!-- Database configurations -->
    <property name="databaseSchemaUpdate" value="${config.activiti.databaseSchemaUpdate}" />
    
    <!-- job executor configurations -->
    <property name="jobExecutorActivate" value="${config.activiti.jobExecutorActivate}" />
    
    <!-- mail server configurations -->
    <property name="mailServerPort" value="${config.activiti.mailServerPort}" />    
  </bean>

</beans>	
"""
	}
	println "Content of generated ${activitiConfigFile.absolutePath} file:"
  println activitiConfigFile.text
}	


