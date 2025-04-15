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

    public static final String VAR_PRODUCTS_AVAILABLE = "productsAvailable";
    public static final String VAR_ORDER_DELIVERED = "orderDelivered";
    public static final String VAR_CUSTOMER = "customer";

    @SuppressWarnings("JUnitMalformedDeclaration")
    @Rule
    @ClassRule
    public static TestCoverageProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder
            .create()
            // NOTE: Each model creates a PROCESS_ID which could be imported only once!
            // SUGGESTION: Could the bpmn-to-code plugin create ORDER_PROCESS_ID, DELIVERY_PROCESS_ID instead of only PROCESS_ID?
            .excludeProcessDefinitionKeys(io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.PROCESS_ID)
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
        ProcessExpressions.registerCallActivityMock(io.flowsquad.camunda.test.DeliveryprocessProcessApiV1.PROCESS_ID)
                .deploy(rule);

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();

        verify(testOrderProcess)
                .hasFinished(EndEvent_OrderFullfilled);

        rule.addTestMethodCoverageAssertionMatcher("shouldExecuteHappyPath", greaterThanOrEqualTo(0.5));
    }
}
