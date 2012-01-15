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

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;


/**
 * @author Joram Barrez
 */
public class TaskListenerTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();
  
  @Deployment(resources = {"org/activiti/examples/bpmn/tasklistener/TaskListenerTest.bpmn20.xml"})
  @Test
  public void testTaskCreateListener() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("taskListenerProcess");
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().singleResult();
    assertEquals("Schedule meeting", task.getName());
    assertEquals("TaskCreateListener is listening!", task.getDescription());
  }
  
  @Deployment(resources = {"org/activiti/examples/bpmn/tasklistener/TaskListenerTest.bpmn20.xml"})
  @Test
  public void testTaskCompleteListener() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskListenerProcess");
    assertEquals(null, runtimeService.getVariable(processInstance.getId(), "greeting"));
    assertEquals(null, runtimeService.getVariable(processInstance.getId(), "expressionValue"));
    
    // Completing first task will change the description 
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().singleResult();
    taskService.complete(task.getId());
    
    assertEquals("Hello from The Process", runtimeService.getVariable(processInstance.getId(), "greeting"));
    assertEquals("Act", runtimeService.getVariable(processInstance.getId(), "shortName"));
  }
  
  @Deployment(resources = {"org/activiti/examples/bpmn/tasklistener/TaskListenerTest.bpmn20.xml"})
  @Test
  public void testTaskListenerWithExpression() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskListenerProcess");
    assertEquals(null, runtimeService.getVariable(processInstance.getId(), "greeting2"));
    
    // Completing first task will change the description 
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().singleResult();
    taskService.complete(task.getId());
    
    assertEquals("Write meeting notes", runtimeService.getVariable(processInstance.getId(), "greeting2"));
  }

}
