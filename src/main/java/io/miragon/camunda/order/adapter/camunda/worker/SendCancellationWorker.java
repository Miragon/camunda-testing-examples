package io.miragon.camunda.order.adapter.camunda.worker;

import io.miragon.camunda.order.application.ports.in.SendMailUseCase;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Map;

@Configuration
@Slf4j
@ExternalTaskSubscription("sendCancellation")
public class SendCancellationWorker implements ExternalTaskHandler {

    private final SendMailUseCase sendMailUseCase;

    @Autowired
    public SendCancellationWorker(SendMailUseCase sendMailUseCase) {
        this.sendMailUseCase = sendMailUseCase;
    }

    public void execute(
            final ExternalTask externalTask,
            final ExternalTaskService externalTaskService) {
        //input
        final String customer = externalTask.getVariable("customer");

        //processing
        sendMailUseCase.sendMail(customer);

        //output
        externalTaskService.complete(externalTask, Map.of("cancellationTimeStamp", Instant.now().getEpochSecond()));
    }
}
