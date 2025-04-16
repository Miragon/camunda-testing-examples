package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.community.process_test_coverage.junit5.platform7.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.Elements.EndEvent_DeliveryCompleted;
import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.Elements.Task_DeliverOrder;
import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.PROCESS_ID;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.mockito.Mockito.*;

@Deployment(resources = "delivery-process.bpmn")
@ExtendWith(ProcessEngineCoverageExtension.class)
class DeliveryProcessTest {

    public static final String VAR_ORDER_DELIVERED = "orderDelivered";

    @SuppressWarnings("unused")
    public static ProcessEngineCoverageExtension extension = ProcessEngineCoverageExtension
            .builder()
            .assertClassCoverageAtLeast(0.9)
            .build();

    @Mock
    private ProcessScenario testDeliveryProcess;

    @BeforeEach
    public void defaultScenario() {
        MockitoAnnotations.initMocks(this);

        //Happy-Path
        when(testDeliveryProcess.waitsAtUserTask(Task_DeliverOrder))
                .thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));
    }

    @DisplayName("Delivery fails")
    @Test
    public void shouldExecuteDeliverTwice() {
        when(testDeliveryProcess.waitsAtUserTask(Task_DeliverOrder))
                .thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, false)), task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));

        Scenario.run(testDeliveryProcess)
                .startByKey(PROCESS_ID)
                .execute();

        verify(testDeliveryProcess, times(2))
                .hasCompleted(Task_DeliverOrder);
        verify(testDeliveryProcess)
                .hasFinished(EndEvent_DeliveryCompleted);
    }

    @DisplayName("Happy Path")
    @Test
    public void shouldExecuteHappyPath() {
        Scenario.run(testDeliveryProcess)
                .startByKey(PROCESS_ID)
                .execute();

        verify(testDeliveryProcess)
                .hasFinished(EndEvent_DeliveryCompleted);
    }
}
