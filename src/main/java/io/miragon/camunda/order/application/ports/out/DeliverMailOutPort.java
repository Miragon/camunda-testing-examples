package io.miragon.camunda.order.application.ports.out;

public interface DeliverMailOutPort {
    void deliverMail(String customer);
}
