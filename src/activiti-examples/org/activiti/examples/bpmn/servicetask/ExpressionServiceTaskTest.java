package org.activiti.examples.bpmn.servicetask;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

/**
 * @author Christian Stettler
 */
public class ExpressionServiceTaskTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Deployment
  @Test
  public void testSetServiceResultToProcessVariables() {
    Map<String,Object> variables = new HashMap<String, Object>();
    variables.put("bean", new ValueBean("ok"));
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("setServiceResultToProcessVariables", variables);
    assertEquals("ok", runtimeService.getVariable(pi.getId(), "result"));
  }

  @Deployment
  @Test
  public void testBackwardsCompatibleExpression() {
    Map<String,Object> variables = new HashMap<String, Object>();
    variables.put("var", "---");
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("BackwardsCompatibleExpressionProcess", variables);
    assertEquals("...---...", runtimeService.getVariable(pi.getId(), "result"));
  }
}
