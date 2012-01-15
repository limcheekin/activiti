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
@import@

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.4
 */

class VacationRequestBootStrap {
	def springSecurityService
	
	def init = { servletContext ->
		/* If you are sending username using gmail, you need to uncomment this code block 
		 ["mail.smtp.auth":"true",
		 "mail.smtp.socketFactory.port":"465",
		 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
		 "mail.smtp.socketFactory.fallback":"false",
		 "mail.smtp.starttls.required": "true"].each { k, v ->
		 System.setProperty k, v
		 }
		 */    
		environments {			
			production { createUsersAndGroups() }
			development { createUsersAndGroups() }
		}
	}
	
	private void createUsersAndGroups() {
		def userRole = Role.findByAuthority('ROLE_USER') 
		if (!userRole) {
		 userRole = new Role(authority: 'ROLE_USER', name: 'User')
		 userRole.id = 'ROLE_USER'
		 userRole.save(failOnError: true)
		}
		def managerRole = Role.findByAuthority('ROLE_MANAGER') 
		if (!managerRole) {
		 managerRole = new Role(authority: 'ROLE_MANAGER', name: 'Manager')
		 managerRole.id = 'ROLE_MANAGER'
		 managerRole.save(failOnError: true)
		}
		def kermit = User.findByUsername('kermit') ?: new User(
				username: 'kermit',
				email: 'kermit@activiti.org',
				firstName: 'Kermit',
				lastName: 'User',
				password: springSecurityService.encodePassword('kermit'),
				enabled: true).save(failOnError: true)
		def fozzie = User.findByUsername('fozzie') ?: new User(
				username: 'fozzie',
				email: 'fozzie@activiti.org',
				firstName: 'Fozzie',
				lastName: 'Management',
				password: springSecurityService.encodePassword('fozzie'),
				enabled: true).save(failOnError: true)
		def peter = User.findByUsername('peter') ?: new User(
				username: 'peter',
				email: 'peter@activiti.org',
				firstName: 'Peter',
				lastName: 'Management',
				password: springSecurityService.encodePassword('peter'),
				enabled: true).save(failOnError: true)
		
		if (!kermit.authorities.contains(userRole)) {
			UserRole.create kermit, userRole
		}
		
		if (!fozzie.authorities.contains(managerRole)) {
			UserRole.create fozzie, managerRole
		}
		
		if (!peter.authorities.contains(managerRole)) {
			UserRole.create peter, managerRole
		}
	} 
	
	def destroy = {
	}
} 