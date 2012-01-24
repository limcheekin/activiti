package org.grails.activiti.flow.variable

import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.activiti.engine.RepositoryService

/**
 *
 * @author <a href='mailto:pais@exigenservices.com'>Ilya Drabenia</a>
 *
 * @since 5.8.2
 */
class VariableSerializationTests {
    RuntimeService runtimeService
    RepositoryService repositoryService

    @Test
    void testSettingAndGettingOfProcessVariable() {
        repositoryService.createDeployment()
            .addClasspathResource("org/grails/activiti/flow/variable/SampleProcess.bpmn20.xml")
            .name("sampleProcess")
            .deploy()

        // start process
        def process = runtimeService.startProcessInstanceByKey("sampleProcess")

        // check flow variables
        runtimeService.setVariable(process.processInstanceId, "var1", new FakeFlowVariable(name: 'Matt', age: 21))
        def flowVariable = (FakeFlowVariable) runtimeService.getVariable(process.processInstanceId, "var1")

        // kill process
        runtimeService.deleteProcessInstance(process.id, "Test is completed")
    }
}
