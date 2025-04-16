package io.miragon.camunda.utlities;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;

import java.util.Map;

@RequiredArgsConstructor
public class ExternalTaskServiceWrapper implements ExternalTaskService {

    private final org.camunda.bpm.engine.ExternalTaskService service;
    private final RuntimeService runtimeService;

    @Override
    public void lock(String externalTaskId, long lockDuration) {
        this.service.lock(externalTaskId, "worker", lockDuration);
    }

    @Override
    public void lock(ExternalTask externalTask, long lockDuration) {
        this.service.lock(externalTask.getId(), "worker", lockDuration);
    }

    @Override
    public void unlock(ExternalTask externalTask) {
        this.service.unlock(externalTask.getId());
    }

    @Override
    public void complete(ExternalTask externalTask) {
        this.lock(externalTask, 1000000);
        this.service.complete(externalTask.getId(), "worker");
    }

    @Override
    public void setVariables(String processInstanceId, Map<String, Object> variables) {
        this.runtimeService.setVariables(processInstanceId, variables);
    }

    @Override
    public void setVariables(ExternalTask externalTask, Map<String, Object> variables) {
        this.runtimeService.setVariables(externalTask.getId(), variables);
    }

    @Override
    public void complete(ExternalTask externalTask, Map<String, Object> variables) {
        this.lock(externalTask, 1000000);
        this.service.complete(externalTask.getId(), "worker", variables);
    }

    @Override
    public void complete(ExternalTask externalTask, Map<String, Object> variables, Map<String, Object> localVariables) {
        this.lock(externalTask, 1000000);
        this.service.complete(externalTask.getId(), "worker", variables, localVariables);
    }

    @Override
    public void complete(String externalTaskId, Map<String, Object> variables, Map<String, Object> localVariables) {
        this.lock(externalTaskId, 1000000);
        this.service.complete(externalTaskId, "worker", variables, localVariables);
    }

    @Override
    public void handleFailure(ExternalTask externalTask, String errorMessage, String errorDetails, int retries, long retryTimeout) {
        this.lock(externalTask, 1000000);
        this.service.handleFailure(externalTask.getId(), "worker", errorMessage, errorDetails, retries, retryTimeout);
    }

    @Override
    public void handleFailure(String externalTaskId, String errorMessage, String errorDetails, int retries, long retryTimeout) {
        this.lock(externalTaskId, 1000000);
        this.service.handleFailure(externalTaskId, "worker", errorMessage, errorDetails, retries, retryTimeout);
    }

    @Override
    public void handleFailure(String externalTaskId, String errorMessage, String errorDetails, int retries, long retryTimeout, Map<String, Object> variables, Map<String, Object> localVariables) {
        this.lock(externalTaskId, 1000000);
        this.service.handleFailure(externalTaskId, "worker", errorMessage, errorDetails, retries, retryTimeout, variables, localVariables);
    }

    @Override
    public void handleBpmnError(ExternalTask externalTask, String errorCode) {
        this.lock(externalTask, 1000000);
        this.service.handleBpmnError(externalTask.getId(), "worker", errorCode);
    }

    @Override
    public void handleBpmnError(ExternalTask externalTask, String errorCode, String errorMessage) {
        this.lock(externalTask, 1000000);
        this.service.handleBpmnError(externalTask.getId(), "worker", errorCode, errorMessage);
    }

    @Override
    public void handleBpmnError(ExternalTask externalTask, String errorCode, String errorMessage, Map<String, Object> variables) {
        this.lock(externalTask, 1000000);
        this.service.handleBpmnError(externalTask.getId(), "worker", errorCode, errorMessage, variables);
    }

    @Override
    public void handleBpmnError(String externalTaskId, String errorCode, String errorMessage, Map<String, Object> variables) {
        this.lock(externalTaskId, 1000000);
        this.service.handleBpmnError(externalTaskId, "worker", errorCode, errorMessage, variables);
    }

    @Override
    public void extendLock(ExternalTask externalTask, long newDuration) {
        this.lock(externalTask, 1000000);
        this.service.extendLock(externalTask.getId(), "worker", newDuration);
    }

    @Override
    public void extendLock(String externalTaskId, long newDuration) {
        this.service.extendLock(externalTaskId, "worker", newDuration);
    }
}
