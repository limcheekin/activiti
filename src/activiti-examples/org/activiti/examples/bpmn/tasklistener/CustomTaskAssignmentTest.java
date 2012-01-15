/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.activiti.examples.bpmn.tasklistener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;


/**
 * @author Joram Barrez
 * @author Falko Menge <falko.menge@camunda.com>
 * @author Frederik Heremans
 */
public class CustomTaskAssignmentTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();
  
  @Before
  public void setUp() throws Exception {
    
    IdentityService identityService = activitiRule.getIdentityService();
    identityService.saveUser(identityService.newUser("kermit"));
    identityService.saveUser(identityService.newUser("fozzie"));
    identityService.saveUser(identityService.newUser("gonzo"));
    
    identityService.saveGroup(identityService.newGroup("management"));
    
    identityService.createMembership("kermit", "management");
  }
  
  @After
  public void tearDown() throws Exception {
    IdentityService identityService = activitiRule.getIdentityService();
    identityService.deleteUser("kermit");
    identityService.deleteUser("fozzie");
    identityService.deleteUser("gonzo");
    identityService.deleteGroup("management");
  }
  
  @Deployment
  @Test
  public void testCandidateGroupAssignment() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("customTaskAssignment");
    TaskService taskService = activitiRule.getTaskService();
    assertEquals(1, taskService.createTaskQuery().taskCandidateGroup("management").count());
    assertEquals(1, taskService.createTaskQuery().taskCandidateUser("kermit").count());
    assertEquals(0, taskService.createTaskQuery().taskCandidateUser("fozzie").count());
  }
  
  @Deployment
  @Test
  public void testCandidateUserAssignment() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("customTaskAssignment");
    TaskService taskService = activitiRule.getTaskService();
    assertEquals(1, taskService.createTaskQuery().taskCandidateUser("kermit").count());
    assertEquals(1, taskService.createTaskQuery().taskCandidateUser("fozzie").count());
    assertEquals(0, taskService.createTaskQuery().taskCandidateUser("gonzo").count());
  }
  
  @Deployment
  @Test
  public void testAssigneeAssignment() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("setAssigneeInListener");
    TaskService taskService = activitiRule.getTaskService();
    assertNotNull(taskService.createTaskQuery().taskAssignee("kermit").singleResult());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("fozzie").count());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("gonzo").count());
  }
  
  @Deployment
  @Test
  public void testOverwriteExistingAssignments() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("overrideAssigneeInListener");
    TaskService taskService = activitiRule.getTaskService();
    assertNotNull(taskService.createTaskQuery().taskAssignee("kermit").singleResult());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("fozzie").count());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("gonzo").count());
  }

  @Deployment
  @Test
  public void testOverwriteExistingAssignmentsFromVariable() {
    // prepare variables
    Map<String, String> assigneeMappingTable = new HashMap<String, String>();
    assigneeMappingTable.put("fozzie", "gonzo");
   
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("assigneeMappingTable", assigneeMappingTable);

    // start process instance
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("customTaskAssignment", variables);
    
    // check task lists
    TaskService taskService = activitiRule.getTaskService();
    assertNotNull(taskService.createTaskQuery().taskAssignee("gonzo").singleResult());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("fozzie").count());
    assertEquals(0, taskService.createTaskQuery().taskAssignee("kermit").count());
  }
  
  @Deployment
  @Test
  public void testReleaseTask() throws Exception {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("releaseTaskProcess");
    
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().taskAssignee("fozzie").singleResult();
    assertNotNull(task);
    String taskId = task.getId();
    
    // Set assignee to null
    taskService.setAssignee(taskId, null);
    
    task = taskService.createTaskQuery().taskAssignee("fozzie").singleResult();
    assertNull(task);
    
    task = taskService.createTaskQuery().taskId(taskId).singleResult();
    assertNotNull(task);
    assertNull(task.getAssignee());
  }

}
