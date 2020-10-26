package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendCancellationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //do nothing
    }
}
