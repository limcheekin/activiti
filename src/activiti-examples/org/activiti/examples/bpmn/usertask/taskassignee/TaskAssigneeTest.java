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
package org.activiti.examples.bpmn.usertask.taskassignee;

import java.util.List;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;


/**
 * Simple process test to validate the current implementation protoype.
 * 
 * @author Joram Barrez 
 */
public class TaskAssigneeTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Deployment
  @Test
  public void testTaskAssignee() {    
    
    // Start process instance
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskAssigneeExampleProcess");

    // Get task list
    TaskService taskService = activitiRule.getTaskService();
    List<Task> tasks = taskService
      .createTaskQuery()
      .taskAssignee("kermit")
      .list();
    assertEquals(1, tasks.size());
    Task myTask = tasks.get(0);
    assertEquals("Schedule meeting", myTask.getName());
    assertEquals("Schedule an engineering meeting for next week with the new hire.", myTask.getDescription());

    // Complete task. Process is now finished
    taskService.complete(myTask.getId());
    // assert if the process instance completed
    assertNull("Process ended", activitiRule
               .getRuntimeService()
               .createProcessInstanceQuery()
               .processInstanceId(processInstance.getId())
               .singleResult());
  }

}
