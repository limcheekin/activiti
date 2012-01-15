@artifact.package@import grails.test.*
import org.junit.*
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.matchers.JUnitMatchers.*
import org.activiti.engine.test.*

class @artifact.name@ {
 
    @Rule public ActivitiRule activitiRule = new ActivitiRule()

    @Before void setUp() {
    }

    @After void tearDown() {
    }

    @Test @Deployment void something() {

    }
}
