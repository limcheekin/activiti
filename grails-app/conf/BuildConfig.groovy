grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits 'global'
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
		mavenRepo name: "Activiti", root: "https://maven.alfresco.com/nexus/content/groups/public"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile ('org.activiti:activiti-engine:5.12.1') {
			excludes 'livetribe-jsr223'
		}
		runtime 'org.activiti:activiti-spring:5.12.1'
		runtime 'javax.mail:mail:1.4.1'
		test 'org.subethamail:subethasmtp-smtp:1.2'
		test 'org.subethamail:subethasmtp-wiser:1.2'
    }
	plugins {
		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }
	}
}
