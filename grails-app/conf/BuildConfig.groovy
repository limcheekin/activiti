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
		compile ('org.activiti:activiti-engine:5.13') {
			excludes 'livetribe-jsr223', 'spring-beans'
		}
		runtime ('org.activiti:activiti-spring:5.13') {
			excludes 'spring-context', 'spring-jdbc', 'spring-orm', 'slf4j-log4j12', 'commons-dbcp'
		}
		//runtime 'org.springframework:spring-asm:3.1.4.RELEASE'
		runtime 'javax.mail:mail:1.4.1'
		test ('org.subethamail:subethasmtp-smtp:1.2') {
			excludes 'commons-logging'
		}
		test ('org.subethamail:subethasmtp-wiser:1.2') {
			excludes 'commons-logging'
		}
    }
	plugins {
		build ":release:3.0.1"
	}
}
