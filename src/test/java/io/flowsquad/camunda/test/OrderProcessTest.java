package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.mockito.ProcessExpressions;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.delegate.TaskDelegate;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.flowsquad.camunda.test.OrderprocessProcessApiV1.Elements.*;
import static io.flowsquad.camunda.test.OrderprocessProcessApiV1.PROCESS_ID;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.Mockito.*;

@Deployment(resources = "order-process.bpmn")
public class OrderProcessTest {

    //public static final String PROCESS_KEY = "orderprocess";
    public static final String DELIVERY_PROCESS_KEY = "deliveryprocess";
    //public static final String TASK_CHECK_AVAILABILITY = "Task_CheckAvailability";
    public static final String VAR_PRODUCTS_AVAILABLE = "productsAvailable";
    //public static final String TASK_PREPARE_ORDER = "Task_PrepareOrder";
    //public static final String TASK_DELIVER_ORDER = "Task_DeliverOrder";
    public static final String VAR_ORDER_DELIVERED = "orderDelivered";
    //public static final String TASK_CANCEL_ORDER = "Task_CancelOrder";
    //public static final String END_EVENT_ORDER_FULLFILLED = "EndEvent_OrderFullfilled";
    public static final String END_EVENT_ORDER_CANCELLED = "EndEvent_OrderCancelled";
    //public static final String END_EVENT_CANCELLATION_SENT = "EndEvent_CancellationSent";
    public static final String VAR_CUSTOMER = "customer";

    @SuppressWarnings("JUnitMalformedDeclaration")
    @Rule
    @ClassRule
    public static TestCoverageProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder
            .create()
            .excludeProcessDefinitionKeys(DELIVERY_PROCESS_KEY) //Exclude partial process scenario
            .assertClassCoverageAtLeast(0.9)
            .build();

    @Mock
    private ProcessScenario testOrderProcess;

    @Mock
    private MailingService mailingService;

    @Before
    public void defaultScenario() {
        MockitoAnnotations.initMocks(this);

        //Happy-Path
        when(testOrderProcess.waitsAtUserTask(Task_CheckAvailability))
                .thenReturn(task -> task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, true)));

        when(testOrderProcess.waitsAtUserTask(Task_PrepareOrder))
                .thenReturn(TaskDelegate::complete);

        when(testOrderProcess.waitsAtUserTask(Task_DeliverOrder))
                .thenReturn(task -> task.complete(withVariables(VAR_ORDER_DELIVERED, true)));

        //Further Activities
        //when(testOrderProcess.waitsAtUserTask(TASK_CANCEL_ORDER))
        //        .thenReturn(TaskDelegate::complete);
    }

    @Test
    public void shouldExecuteOrderCancelled() {
        //Include partial process scenario
        ProcessExpressions.registerCallActivityMock(DELIVERY_PROCESS_KEY)
                .onExecutionDo(execution -> {
                    throw new BpmnError("deliveryFailed");
                })
                .deploy(rule);

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();

        verify(testOrderProcess)
                .hasFinished(END_EVENT_ORDER_CANCELLED);
        //Jumps back to the corresponding boundary event and verifies the task there has completed
        //verify(testOrderProcess)
        //        .hasCompleted(TASK_CANCEL_ORDER);

        rule.addTestMethodCoverageAssertionMatcher("shouldExecuteOrderCancelled", greaterThanOrEqualTo(0.6));
    }

    @Test
    public void shouldExecuteCancellationSent() {
        //Register implementation of SendCancellationDelegate (with private member mailingService), see Mocks
        Mocks.register("sendCancellationDelegate", new SendCancellationDelegate(mailingService));

        when(testOrderProcess.waitsAtUserTask(Task_CheckAvailability)).thenReturn(task -> task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, false)));

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();

        verify(mailingService, (times(1))).sendMail(any());
        verifyNoMoreInteractions(mailingService);
        verify(testOrderProcess)
                .hasFinished(EndEvent_CancellationSent);

        rule.addTestMethodCoverageAssertionMatcher("shouldExecuteCancellationSent", greaterThanOrEqualTo(0.4));
    }

    @Test
    public void shouldExecuteHappyPath() {
        //Include partial process scenario
        ProcessExpressions.registerCallActivityMock(DELIVERY_PROCESS_KEY)
                .deploy(rule);

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();

        verify(testOrderProcess)
                .hasFinished(EndEvent_OrderFullfilled);

        rule.addTestMethodCoverageAssertionMatcher("shouldExecuteHappyPath", greaterThanOrEqualTo(0.5));
    }
}
