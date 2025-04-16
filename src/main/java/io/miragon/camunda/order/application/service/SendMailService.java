package io.miragon.camunda.order.application.service;

import io.miragon.camunda.order.application.ports.in.SendMailUseCase;
import io.miragon.camunda.order.application.ports.out.DeliverMailOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailService implements SendMailUseCase {

    private final DeliverMailOutPort deliverMailOutPort;

    @Override
    public void sendMail(String customer) {
        deliverMailOutPort.deliverMail(customer);
    }

}
