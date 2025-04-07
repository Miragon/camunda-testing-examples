package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.Mockito.*;

@Deployment(resources = "deliver-process.bpmn")
public class DeliverProcessTest {

    public static final String PROCESS_KEY = "deliverprocess";
    public static final String TASK_DELIVER_ORDER = "Task_DeliverOrder";
    public static final String VAR_ORDER_DELIVERED = "orderDelivered";
    public static final String END_EVENT_DELIVERY_COMPLETED = "EndEvent_DeliveryCompleted";
    public static final String END_EVENT_DELIVERY_CANCELLED = "EndEvent_DeliveryCancelled";

    @Rule
    public TestCoverageProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder
            .create()
            .assertClassCoverageAtLeast(0.9)
            .build();

    @Mock
    private ProcessScenario testDeliverProcess;

    @Before
    public void defaultScenario() {
        MockitoAnnotations.initMocks(this);

        //Happy-Path
        when(testDeliverProcess.waitsAtUserTask(TASK_DELIVER_ORDER))
                .thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));
    }

    @Test
    public void shouldExecuteHappyPath() {
        Scenario.run(testDeliverProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testDeliverProcess)
                .hasFinished(END_EVENT_DELIVERY_COMPLETED);

        rule.addTestMethodCoverageAssertionMatcher("shouldExecuteHappyPath", greaterThanOrEqualTo(0.5));
    }

    @Test
    public void shouldExecuteOrderCancelled() {
        when(testDeliverProcess.waitsAtUserTask(TASK_DELIVER_ORDER)).thenReturn(task -> taskService().handleBpmnError(task.getId(), "DeliveryCancelled"));

        Scenario.run(testDeliverProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testDeliverProcess)
                .hasFinished(END_EVENT_DELIVERY_CANCELLED);
    }

    @Test
    public void shouldExecuteDeliverTwice() {
        when(testDeliverProcess.waitsAtUserTask(TASK_DELIVER_ORDER)).thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, false)), task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));

        Scenario.run(testDeliverProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testDeliverProcess, times(2))
                .hasCompleted(TASK_DELIVER_ORDER);
        verify(testDeliverProcess)
                .hasFinished(END_EVENT_DELIVERY_COMPLETED);
    }
}
