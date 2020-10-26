package io.flowsquad.camunda.test;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.delegate.TaskDelegate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.mockito.Mockito.*;

@Deployment(resources = "order-process.bpmn")
public class WorkflowTest {

    public static final String TASK_CHECK_AVAILABILITY = "Task_CheckAvailability";
    public static final String VAR_PRODUCTS_AVAILABLE = "productsAvailable";
    public static final String TASK_PREPARE_ORDER = "Task_PrepareOrder";
    public static final String TASK_DELIVER_ORDER = "Task_DeliverOrder";
    public static final String VAR_ORDER_DELIVERED = "orderDelivered";
    public static final String TASK_CANCEL_ORDER = "Task_CancelOrder";
    public static final String PROCESS_KEY = "orderprocess";
    public static final String TASK_SEND_CANCELLATION = "Task_SendCancellation";
    public static final String END_EVENT_ORDER_FULLFILLED = "EndEvent_OrderFullfilled";
    public static final String END_EVENT_ORDER_CANCELLED = "EndEvent_OrderCancelled";
    public static final String END_EVENT_CANCELLATION_SENT = "EndEvent_CancellationSent";

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    @Mock
    private ProcessScenario testOrderProcess;

    @Before
    public void defaultScenario() {
        MockitoAnnotations.initMocks(this);
        Mocks.register("sendCancellationDelegate", new SendCancellationDelegate());

        //Happy-Path
        when(testOrderProcess.waitsAtUserTask(TASK_CHECK_AVAILABILITY))
                .thenReturn(task -> {
                    task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, true));
                });

        when(testOrderProcess.waitsAtUserTask(TASK_PREPARE_ORDER))
                .thenReturn(TaskDelegate::complete);

        when(testOrderProcess.waitsAtUserTask(TASK_DELIVER_ORDER))
                .thenReturn(task -> {
                    task.complete(withVariables(VAR_ORDER_DELIVERED, true));
                });

        //Further Activities
        when(testOrderProcess.waitsAtUserTask(TASK_CANCEL_ORDER))
                .thenReturn(TaskDelegate::complete);
    }

    @Test
    public void shouldExecuteHappyPath() {
        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testOrderProcess)
                .hasFinished(END_EVENT_ORDER_FULLFILLED);
    }

    @Test
    public void shouldExecuteCancellationSent() {
        when(testOrderProcess.waitsAtUserTask(TASK_CHECK_AVAILABILITY)).thenReturn(task -> {
            task.complete(withVariables(VAR_PRODUCTS_AVAILABLE, false));
        });

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testOrderProcess)
                .hasFinished(END_EVENT_CANCELLATION_SENT);
    }

    @Test
    public void shouldExecuteOrderCancelled() {
        when(testOrderProcess.waitsAtUserTask(TASK_DELIVER_ORDER)).thenReturn(task -> {
            taskService().handleBpmnError(task.getId(), "OrderCancelled");
        });

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testOrderProcess)
                .hasCompleted(TASK_CANCEL_ORDER);
        verify(testOrderProcess)
                .hasFinished(END_EVENT_ORDER_CANCELLED);
    }

    @Test
    public void shouldExecuteDeliverTwice() {
        when(testOrderProcess.waitsAtUserTask(TASK_DELIVER_ORDER)).thenReturn(task -> {
            task.complete(withVariables(VAR_ORDER_DELIVERED, false));
        }, task -> {
            task.complete(withVariables(VAR_ORDER_DELIVERED, true));
        });

        Scenario.run(testOrderProcess)
                .startByKey(PROCESS_KEY)
                .execute();

        verify(testOrderProcess, times(2))
                .hasCompleted(TASK_DELIVER_ORDER);
        verify(testOrderProcess)
                .hasFinished(END_EVENT_ORDER_FULLFILLED);
    }
}
