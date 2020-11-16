package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SendCancellationDelegate implements JavaDelegate {

    private final MailingService mailingService;

    @Autowired
    public SendCancellationDelegate(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //input
        final String customer = (String) delegateExecution.getVariable("customer");

        //processing
        mailingService.sendMail(customer);

        //output
        delegateExecution.setVariable("cancellationTimeStamp", Instant.now().getEpochSecond());
    }
}
