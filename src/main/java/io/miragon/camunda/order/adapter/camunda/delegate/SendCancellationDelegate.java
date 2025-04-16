package io.miragon.camunda.order.adapter.camunda.delegate;

import io.miragon.camunda.order.application.ports.in.SendMailUseCase;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SendCancellationDelegate implements JavaDelegate {

    private final SendMailUseCase sendMailUseCase;

    @Autowired
    public SendCancellationDelegate(SendMailUseCase sendMailUseCase) {
        this.sendMailUseCase = sendMailUseCase;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //input
        final String customer = (String) delegateExecution.getVariable("customer");

        //processing
        sendMailUseCase.sendMail(customer);

        //output
        delegateExecution.setVariable("cancellationTimeStamp", Instant.now().getEpochSecond());
    }
}
