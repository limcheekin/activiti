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
 
  /**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
 
class VacationRequestBootStrap {
		def identityService
		
     def init = { servletContext ->
		 /* If you are sending email using gmail, you need to uncomment this code block 
			  ["mail.smtp.auth":"true",
				 "mail.smtp.socketFactory.port":"465",
				 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				 "mail.smtp.socketFactory.fallback":"false",
				 "mail.smtp.starttls.required": "true"].each { k, v ->
			     System.setProperty k, v
		         }
		    */    
				environments {			
					production {
						createUsersAndGroups()
					}
					development {
						createUsersAndGroups()
					}
				}				
			
     }
     
     private void createUsersAndGroups() {
 				identityService.with {
					deleteGroup("management")
					deleteGroup("user")
          deleteUser("kermit")						
          deleteUser("fozzie")
          deleteUser("peter")		  			
					saveUser(newUser("kermit"))
					saveUser(newUser("fozzie"))
					saveUser(newUser("peter"))
					saveGroup(newGroup("management"))
					saveGroup(newGroup("user"))
					createMembership("kermit", "user")
					createMembership("fozzie", "management")
					createMembership("peter", "management")
				}	    
     } 
     def destroy = {
     }
} 