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
package org.activiti.examples.bpmn.event.timer;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * @author Joram Barrez
 */
public class BoundaryTimerEventTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Deployment
  @Test
  public void testInterruptingTimerDuration() {
    
    // Start process instance
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("escalationExample");

    // There should be one task, with a timer : first line support
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    assertEquals("First line support", task.getName());

    // Manually execute the job
    ManagementService managementService = activitiRule.getManagementService();
    Job timer = managementService.createJobQuery().singleResult();
    managementService.executeJob(timer.getId());

    // The timer has fired, and the second task (secondlinesupport) now exists
    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    assertEquals("Handle escalated issue", task.getName());
  }

}
