package io.miragon.camunda.utlities;

import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.externaltask.ExternalTask;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

public class ExternalTaskMethods {

    public static void completeExternalTask(ExternalTaskHandler externalTaskHandler) {
        completeExternalTask(externalTaskHandler, externalTask());
    }

    public static void completeExternalTask(ExternalTaskHandler externalTaskHandler, ExternalTask externalTask) {
        externalTaskHandler.execute(new ExternalTaskWrapper(externalTask, runtimeService()), new ExternalTaskServiceWrapper(externalTaskService(), runtimeService()));
    }

}
