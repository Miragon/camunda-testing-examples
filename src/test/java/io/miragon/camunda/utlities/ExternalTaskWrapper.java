package io.miragon.camunda.utlities;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class ExternalTaskWrapper implements ExternalTask {

    private final org.camunda.bpm.engine.externaltask.ExternalTask task;
    private final RuntimeService runtimeService;

    @Override
    public String getActivityId() {
        return task.getActivityId();
    }

    @Override
    public String getActivityInstanceId() {
        return task.getActivityInstanceId();
    }

    @Override
    public String getErrorMessage() {
        return task.getErrorMessage();
    }

    @Override
    public String getErrorDetails() {
        return task.getErrorMessage();
    }

    @Override
    public String getExecutionId() {
        return task.getExecutionId();
    }

    @Override
    public String getId() {
        return task.getId();
    }

    @Override
    public Date getLockExpirationTime() {
        return task.getLockExpirationTime();
    }

    @Override
    public Date getCreateTime() {
        return task.getCreateTime();
    }

    @Override
    public String getProcessDefinitionId() {
        return task.getProcessDefinitionId();
    }

    @Override
    public String getProcessDefinitionKey() {
        return task.getProcessDefinitionKey();
    }

    @Override
    public String getProcessDefinitionVersionTag() {
        return task.getProcessDefinitionVersionTag();
    }

    @Override
    public String getProcessInstanceId() {
        return task.getProcessInstanceId();
    }

    @Override
    public Integer getRetries() {
        return task.getRetries();
    }

    @Override
    public String getWorkerId() {
        return task.getWorkerId();
    }

    @Override
    public String getTopicName() {
        return task.getTopicName();
    }

    @Override
    public String getTenantId() {
        return task.getTenantId();
    }

    @Override
    public long getPriority() {
        return task.getPriority();
    }

    @Override
    public <T> T getVariable(String variableName) {
        return (T) runtimeService.getVariable(task.getExecutionId(), variableName);
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String variableName) {
        return runtimeService.getVariableTyped(task.getExecutionId(), variableName);
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String variableName, boolean deserializeObjectValue) {
        return runtimeService.getVariableTyped(task.getExecutionId(), variableName, deserializeObjectValue);
    }

    @Override
    public Map<String, Object> getAllVariables() {
        return runtimeService.getVariables(task.getExecutionId());
    }

    @Override
    public VariableMap getAllVariablesTyped() {
        return runtimeService.getVariablesTyped(task.getExecutionId());
    }

    @Override
    public VariableMap getAllVariablesTyped(boolean deserializeObjectValues) {
        return runtimeService.getVariablesTyped(task.getExecutionId(), deserializeObjectValues);
    }

    @Override
    public String getBusinessKey() {
        return task.getBusinessKey();
    }

    @Override
    public String getExtensionProperty(String propertyKey) {
        return task.getExtensionProperties().get(propertyKey);
    }

    @Override
    public Map<String, String> getExtensionProperties() {
        return task.getExtensionProperties();
    }
}
