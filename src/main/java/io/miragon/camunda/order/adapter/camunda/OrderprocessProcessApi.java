// Generated by bpmn-to-code
package io.miragon.camunda.order.adapter.camunda;

import java.lang.String;

public final class OrderprocessProcessApi {
  public static final String PROCESS_ID = "orderprocess";

  public static final class Elements {
    public static final String Gateway_12tl2hf = "Gateway_12tl2hf";

    public static final String EndEvent_CancellationSent = "EndEvent_CancellationSent";

    public static final String EndEvent_OrderFullfilled = "EndEvent_OrderFullfilled";

    public static final String Task_SendCancellation = "Task_SendCancellation";

    public static final String Task_CheckAvailability = "Task_CheckAvailability";

    public static final String Task_PrepareOrder = "Task_PrepareOrder";

    public static final String StartEvent_1 = "StartEvent_1";

    public static final String Task_DeliverOrder = "Task_DeliverOrder";
  }

  public static final class Messages {
    public static final String Problem_Received = "Problem_Received";
  }

  public static final class TaskTypes {
    public static final String Task_SendCancellation = "${sendCancellationDelegate}";
  }
}
