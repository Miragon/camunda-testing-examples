package io.flowsquad.camunda.test;

import org.springframework.stereotype.Service;

@Service
public class MailingService {

    public void sendMail(String customer) {
        System.out.println("Send Mail to " + customer + "...");
    }

}
