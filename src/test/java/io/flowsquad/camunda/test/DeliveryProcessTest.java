package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.community.process_test_coverage.junit5.platform7.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.Elements.EndEvent_DeliveryCompleted;
import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.Elements.Task_DeliverOrder;
import static io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.PROCESS_ID;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.Mockito.*;

@Deployment(resources = "delivery-process.bpmn")
class DeliveryProcessTest {

    public static final String VAR_ORDER_DELIVERED = "orderDelivered";

    @RegisterExtension
    public static ProcessEngineCoverageExtension extension = ProcessEngineCoverageExtension
            // NOTE: Each model creates a PROCESS_ID which could be imported only once! (For partial process scenarios only)
            // SUGGESTION: Could the bpmn-to-code plugin create ORDER_PROCESS_ID, DELIVERY_PROCESS_ID instead of only PROCESS_ID?
            .builder()
            .excludeProcessDefinitionKeys(io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.PROCESS_ID)
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

    @Test
    public void shouldExecuteDeliverTwice() {
        when(testDeliveryProcess.waitsAtUserTask(Task_DeliverOrder)).thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, false)), task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));

        Scenario.run(testDeliveryProcess)
                .startByKey(PROCESS_ID)
                .execute();

        verify(testDeliveryProcess, times(2))
                .hasCompleted(Task_DeliverOrder);
        verify(testDeliveryProcess)
                .hasFinished(EndEvent_DeliveryCompleted);

        //rule.addTestMethodCoverageAssertionMatcher("shouldExecuteDeliverTwice", greaterThanOrEqualTo(0.7));
    }

    @Test
    public void shouldExecuteHappyPath() {
        Scenario.run(testDeliveryProcess)
                .startByKey(PROCESS_ID)
                .execute();

        verify(testDeliveryProcess)
                .hasFinished(EndEvent_DeliveryCompleted);

        extension.addTestMethodCoverageCondition("shouldExecuteHappyPath", greaterThanOrEqualTo(0.5));
    }
}
