package com.seleznov.activity.controller;

import com.seleznov.activity.service.SimpleService;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController("demo/activiti")
public class ActivitiController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SimpleService simpleService;

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping("/save-flow")
    public void startFlow() {
        BpmnModel model = createModel();
        Deployment deployment = repositoryService.createDeployment()
                .name("demo deployment")
                .addBpmnModel("dynamic-model.bpmn", model)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        runtimeService.startProcessInstanceById(processDefinition.getId());
    }

    private BpmnModel createModel() {
        BpmnModel model = new BpmnModel();

        Process process = new Process();
        process.setId("demo_flow");
        model.addProcess(process);

        process.addFlowElement(createStartEvent());
        process.addFlowElement(createServiceTask("task1", "task1name", "inapp"));
        process.addFlowElement(createServiceTask("task2", "task2name", "bonus"));
        process.addFlowElement(createEndEvent());
        process.addFlowElement(createTimerEvent("timer1"));

        process.addFlowElement(createSequenceFlow("start", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "timer1"));
        process.addFlowElement(createSequenceFlow("timer1", "task2"));
        process.addFlowElement(createSequenceFlow("task2", "end"));

        return model;
    }



    private StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    protected ServiceTask createServiceTask(String id, String name, String type) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setName(name);
        serviceTask.setId(id);
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation("${taskResolver}");
        CustomProperty customProperty = new CustomProperty();
        customProperty.setName("type");
        customProperty.setSimpleValue(type);
        serviceTask.setCustomProperties(Collections.singletonList(customProperty));
        return serviceTask;
    }

    private EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

    private SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    private IntermediateCatchEvent createTimerEvent(String id) {
        IntermediateCatchEvent timerEvent = new IntermediateCatchEvent();
        timerEvent.setId(id);
        timerEvent.setAsynchronous(false);
        TimerEventDefinition timerEventDefinition = new TimerEventDefinition();
        timerEventDefinition.setTimeDuration("PT5HZ!");
        timerEvent.addEventDefinition(timerEventDefinition);
        return timerEvent;
    }




}
