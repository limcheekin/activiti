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
package org.activiti.examples.taskforms;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskFormsTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Before
  public void setUp() throws Exception {
    IdentityService identityService = activitiRule.getIdentityService();
    identityService.saveUser(identityService.newUser("fozzie"));
    identityService.saveGroup(identityService.newGroup("management"));
    identityService.createMembership("fozzie", "management");
  }

  @After
  public void tearDown() throws Exception {
    IdentityService identityService = activitiRule.getIdentityService();
    identityService.deleteGroup("management");
    identityService.deleteUser("fozzie");
  }

  @Deployment(resources = { 
    "org/activiti/examples/taskforms/VacationRequest.bpmn20.xml", 
    "org/activiti/examples/taskforms/approve.form", 
    "org/activiti/examples/taskforms/request.form", 
    "org/activiti/examples/taskforms/adjustRequest.form" })
  @Test
  public void testTaskFormsWithVacationRequestProcess() {

    // Get start form
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    String procDefId = repositoryService.createProcessDefinitionQuery().singleResult().getId();
    FormService formService = activitiRule.getFormService();
    Object startForm = formService.getRenderedStartForm(procDefId);
    assertNotNull(startForm);
    
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
    String processDefinitionId = processDefinition.getId();
    assertEquals("org/activiti/examples/taskforms/request.form", formService.getStartFormData(processDefinitionId).getFormKey());

    // Define variables that would be filled in through the form
    Map<String, String> formProperties = new HashMap<String, String>();
    formProperties.put("employeeName", "kermit");
    formProperties.put("numberOfDays", "4");
    formProperties.put("vacationMotivation", "I'm tired");
    formService.submitStartFormData(procDefId, formProperties);

    // Management should now have a task assigned to them
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
    assertEquals("Vacation request by kermit", task.getDescription());
    Object taskForm = formService.getRenderedTaskForm(task.getId());
    assertNotNull(taskForm);

  }

  @Deployment
  @Test
  public void testTaskFormUnavailable() {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    String procDefId = repositoryService.createProcessDefinitionQuery().singleResult().getId();
    FormService formService = activitiRule.getFormService();
    assertNull(formService.getRenderedStartForm(procDefId));

    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("noStartOrTaskForm");
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().singleResult();
    assertNull(formService.getRenderedTaskForm(task.getId()));
  }

}
