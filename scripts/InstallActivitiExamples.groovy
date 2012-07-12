/* Copyright 2010-2012 the original author or authors.
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
 * Gant script that unzip examples-source.jar to test/unit directory.
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */
 
includeTargets << grailsScript("Init")

target(main: "Copy Activiti examples to test/unit directory") {
		ant.copy (todir:"${basedir}/test/unit", overwrite: true) {
			fileset dir:"${activitiPluginDir}/src/activiti-examples"
		}							
		event('StatusFinal', ['Activiti examples installed successfully.'])					
		event('StatusFinal', ['You can run the installed examples with "grails test-app -unit".'])					
}

setDefaultTarget(main)
