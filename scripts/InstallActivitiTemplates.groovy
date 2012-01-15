/*
 * Copyright 2004-2005 the original author or authors.
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
 * Gant script that installs artifact and scaffolding templates
 *
 * @author Marcel Overdijk
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
import grails.util.BuildSettingsHolder as build

includeTargets << grailsScript("Init")

target (main: "Installs the artifact and scaffolding templates") {
    depends(checkVersion, parseArguments)
    templatesDirName = build.settings.config.grails.templates.dir.name?:'activiti-templates'
    targetDir = "${basedir}/src/${templatesDirName}"
    overwrite = false

    // only if template dir already exists in, ask to overwrite templates
    if (new File(targetDir).exists()) {
        if (!isInteractive || confirmInput("Overwrite existing templates? [y/n]","overwrite.templates")) {
            overwrite = true
        }
    }
    else {
        ant.mkdir(dir: targetDir)
    }
	
    ant.copy (todir:targetDir, overwrite:overwrite) {
			fileset dir:"${activitiPluginDir}/src/activiti-templates"
		}
	
    event("StatusUpdate", [ "Templates installed successfully"])
}

setDefaultTarget(main)
