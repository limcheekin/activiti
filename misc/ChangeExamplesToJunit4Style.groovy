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
import grails.util.GrailsNameUtils

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
includeTargets << grailsScript("Init")

target(main: "Change all JUnit3 style examples to JUnit4") { 
	changeToJUnit4Style()
}

private changeToJUnit4Style() {
	println "Changing example test cases to JUnit4 style..."
	removeImport = "org.activiti.engine.impl.test.PluggableActivitiTestCase"
	addImports = ["org.activiti.engine.test.ActivitiRule",
	"org.activiti.engine.*",
	"org.junit.*",
	"static org.junit.Assert.*"]
	removeExtend = "extends PluggableActivitiTestCase {"
	addActivitiRuleLine = "@Rule public ActivitiRule activitiRule = new ActivitiRule();"
	addTestAnnotation = "@Test"
	addBeforeAnnotation = "@Before"
	addAfterAnnotation = "@After"
	testMethod = "public void test"
	setUpMethod = "void setUp()"
	tearDownMethod = "void tearDown()"
	activitiServices = [identityService:false,
											managementService:false,
											processEngine:false,
											runtimeService:false,
											repositoryService:false,
											taskService:false, 
											formService:false, 
											historyService:false]
	removeLines = ["super.setUp()", "super.tearDown()", "@Override"]
	assertProcessEndedItems = ["pi.getId()":"assertProcessEnded(pi.getId())",
	                           "processInstance.getId()":"assertProcessEnded(processInstance.getId())",
							               "procId":"assertProcessEnded(procId)"]

	new File("${basedir}/test/unit").eachFileRecurse { testFile ->
		if (testFile.isFile() && testFile.text.indexOf(removeImport) > -1) {
			generatedFile = new File("${testFile.absolutePath}.tmp")
			generatedFile.withWriter { file ->
				testFile.eachLine { line ->
					if (!isSkipLine(line, removeLines)) {
						if (line.indexOf(removeImport) > -1) {
							addImports.each {
								file.writeLine( "import $it;" )
							}
						} else if (line.indexOf(removeExtend) > -1) {
							file.writeLine( line.replace(removeExtend, "{\n  $addActivitiRuleLine") )
						} else if (line.indexOf(setUpMethod) > -1) {
							file.writeLine("  $addBeforeAnnotation")
							file.writeLine("  public $setUpMethod throws Exception {")
							resetActivitiServices(activitiServices)
						} else if (line.indexOf(tearDownMethod) > -1) {
							file.writeLine("  $addAfterAnnotation")
							file.writeLine("  public $tearDownMethod throws Exception {")
							resetActivitiServices(activitiServices)
						} else if (line.indexOf(testMethod) > -1) {
							file.writeLine("  $addTestAnnotation")
							file.writeLine(line)
							resetActivitiServices(activitiServices)
						} else if (isAssertProcessEndedFound(line, assertProcessEndedItems)) {	
							file.writeLine '    assertNull("Process ended", activitiRule'
							file.writeLine "               .getRuntimeService()"
							file.writeLine "               .createProcessInstanceQuery()"
							file.writeLine "               .processInstanceId(${isAssertProcessEndedFound(line, assertProcessEndedItems)})"
							file.writeLine "               .singleResult());"
						} else { 
							service = isActivitiServicesFound(line, activitiServices)
							if (service) {
								activitiServices[service] = true 
								serviceClassNameRepresentation = GrailsNameUtils.getClassNameRepresentation(service)
								file.writeLine("    ${serviceClassNameRepresentation} ${service} = activitiRule.get${serviceClassNameRepresentation}();")
							} 
							file.writeLine(line)
						}
					}
				}
			}
			ant.move file:generatedFile.absolutePath, tofile:testFile.absolutePath
		}
	}
	println "Change completed."
}

private resetActivitiServices(def activitiServices) {
	activitiServices.each { k, v -> 
		activitiServices[k] = false
	}
}

private isSkipLine(def line, def removeLines) {
	def isSkipLine = false
	for (removeLine in removeLines)
	if (line.indexOf(removeLine) > -1) {
		isSkipLine = true
		break
	}
	return isSkipLine
}

private isAssertProcessEndedFound(def line, def assertProcessEndedItems) {
	String assertProcessEndedFound = null
	assertProcessEndedItems.each { k, v -> 
		if (assertProcessEndedFound == null && line.indexOf(v) > -1) {
			assertProcessEndedFound = k
		}
	}		
	return assertProcessEndedFound
}

private isActivitiServicesFound(def line, def activitiServices) {
	String serviceFound = null
	activitiServices.each { k, v -> 
		if (!v && serviceFound == null && line.indexOf(k) > -1) {
			serviceFound = k
		}
	}	
	return serviceFound
}


setDefaultTarget(main)
