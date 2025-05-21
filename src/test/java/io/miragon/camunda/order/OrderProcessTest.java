package io.miragon.camunda.order;

import io.miragon.camunda.order.adapter.camunda.DeliveryprocessProcessApi;
import io.miragon.camunda.order.adapter.camunda.delegate.SendCancellationDelegate;
import io.miragon.camunda.order.application.ports.in.SendMailUseCase;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.mockito.ProcessExpressions;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.delegate.TaskDelegate;
import org.camunda.community.process_test_coverage.junit5.platform7.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.miragon.camunda.order.adapter.camunda.DeliveryprocessProcessApi.Elements.Task_DeliverOrder;
import static io.miragon.camunda.order.adapter.camunda.OrderprocessProcessApi.Elements.*;
import static io.miragon.camunda.order.adapter.camunda.OrderprocessProcessApi.PROCESS_ID;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.mockito.Mockito.*;

@Deployment(resources = "order-process.bpmn")
@ExtendWith(ProcessEngineCoverageExtension.class)
public class OrderProcessTest {

    public static final String VAR_PRODUCTS_AVAILABLE = "productsAvailable";
    public static final String VAR_CUSTOMER = "customer";

    public static ProcessEngineCoverageExtension extension = ProcessEngineCoverageExtension
            .builder()
            .assertClassCoverageAtLeast(0.9)
            .build();

    private AutoCloseable mocks;

    @Mock
    private ProcessScenario testOrderProcess;

    @Mock
    private ProcessScenario deliveryRequest;

    @Mock
    private SendMailUseCase mailUseCase;

    @BeforeEach
    public void defaultScenario() {
        mocks = MockitoAnnotations.openMocks(this);
        //Happy-Path
        when(testOrderProcess.waitsAtUserTask(Task_CheckAvailability))
                .thenReturn(task -> task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, true)));
        when(testOrderProcess.waitsAtUserTask(Task_PrepareOrder))
                .thenReturn(TaskDelegate::complete);
        //Register call activity via the process engines repository service
        RepositoryService repositoryService = extension.getProcessEngine().getRepositoryService();
        ProcessExpressions.registerCallActivityMock(DeliveryprocessProcessApi.PROCESS_ID)
                .deploy(repositoryService);
        when(testOrderProcess.runsCallActivity(Task_DeliverOrder))
                .thenReturn(Scenario.use(deliveryRequest));
    }

    @DisplayName("Send cancellation email")
    @Test
    public void shouldExecuteCancellationSent() {
        //Register implementation of SendCancellationDelegate (with private member mailingService)
        Mocks.register("sendCancellationDelegate", new SendCancellationDelegate(mailUseCase));

        when(testOrderProcess.waitsAtUserTask(Task_CheckAvailability)).thenReturn(task -> task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, false)));

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();
        verify(mailUseCase, (times(1))).sendMail(any());
        verifyNoMoreInteractions(mailUseCase);
        verify(testOrderProcess)
                .hasFinished(EndEvent_CancellationSent);
    }

    @DisplayName("Happy Path")
    @Test
    public void shouldExecuteHappyPath() {
        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_ID, withVariables(VAR_CUSTOMER, "john"))
                .execute();
        verify(testOrderProcess)
                .hasFinished(EndEvent_OrderFullfilled);
    }

    @AfterEach
    public void defaultScenarioCleanup() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }
}
