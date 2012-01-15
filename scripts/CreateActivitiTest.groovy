/* Copyright 2006-2010 the original author or authors.
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
 * Gant script that create Activiti unit or integration test by using ActivitiTests.groovy template.
 * The ActivitiTests.groovy template is created by refer to a blog post titled
 *  <a href='http://adhockery.blogspot.com/2010/04/grails-junit-4-test-template.html'>
 * A Grails JUnit 4 test template</a>
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */
import grails.util.BuildSettingsHolder as build

includeTargets << grailsScript("_GrailsInit")
includeTargets << new File("${activitiPluginDir}/scripts/_GrailsActivitiCreateArtifacts.groovy")

target ('default': "Creates a new activiti test.") {
	depends(checkVersion, parseArguments)

    promptForName(type: "Activiti test")

    def name = argsMap["params"][0]
	  def testType = argsMap["params"][1]?:"unit"
	  if (!testType.equals("unit") && !testType.equals("integration")) {
		   ant.echo "'${testType}' not supported!"
		   ant.echo "Create Activiti Test command (create-activiti-test) usage:"
		   ant.echo "\tgrails create-activiti-test ${name} unit"
		   ant.echo "\tgrails create-activiti-test ${name} integration"
		   exit 1
	  }
    createActivitiTest(name: name, testType: testType, suffix: "Tests")
}                            

createActivitiTest = { Map args = [:] ->
	createArtifact(name: args["name"], suffix: "${args['suffix']}", type: "ActivitiTests", path: "test/${args['testType']}", templatesDirName: build.settings.config.grails.templates.dir.name)
}
