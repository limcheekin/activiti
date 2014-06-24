package org.grails.activiti.task

import org.activiti.engine.delegate.TaskListener
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior
import org.activiti.engine.impl.bpmn.parser.AbstractBpmnParseListener
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior
import org.activiti.engine.impl.pvm.process.ActivityImpl
import org.activiti.engine.impl.pvm.process.ScopeImpl
import org.activiti.engine.impl.util.xml.Element

class GlobalUserTaskEventListener extends AbstractBpmnParseListener {
    List<TaskListener> createTaskListeners = []
    List<TaskListener> assignmentTaskListeners = []
    List<TaskListener> completeTaskListeners = []

    @Override
    void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        ActivityBehavior activitybehaviour = activity.getActivityBehavior();
        if (activitybehaviour instanceof UserTaskActivityBehavior){
            UserTaskActivityBehavior userTaskActivity = (UserTaskActivityBehavior) activitybehaviour;
            createTaskListeners.each {
                userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_CREATE, it);
            }
            assignmentTaskListeners.each {
                userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, it);
            }
            completeTaskListeners.each {
                userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_COMPLETE, it);
            }
        }
    }
}
