grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
		    mavenRepo name: "Activiti", root: "https://maven.alfresco.com/nexus/content/groups/public"

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
		compile ('org.activiti:activiti-engine:5.12') {
			excludes 'livetribe-jsr223'
		}
		runtime 'org.activiti:activiti-spring:5.12'
		runtime 'javax.mail:mail:1.4.1'
		test 'org.subethamail:subethasmtp-smtp:1.2'
		test 'org.subethamail:subethasmtp-wiser:1.2'
    }
	plugins {
		build ":release:2.0.3"
	}
}
