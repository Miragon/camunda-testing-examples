package io.miragon.camunda.order.adapter.mail;

import io.miragon.camunda.order.application.ports.out.DeliverMailOutPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailServerAdapter implements DeliverMailOutPort {

    @Override
    public void deliverMail(String customer) {
        log.info("deliverMail customer = " + customer);
    }
}
