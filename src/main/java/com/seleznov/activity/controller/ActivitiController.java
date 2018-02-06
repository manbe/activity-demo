package com.seleznov.activity.controller;

import com.seleznov.activity.service.SimpleService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController("demo/activiti")
public class ActivitiController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SimpleService simpleService;

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping("/start-flow")
    public void startFlow() {

        ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilder("offlineJourney");
        PvmProcessDefinition pvmProcessDefinition = processDefinitionBuilder
                .createActivity("start")
                .initial()
                .behavior((ActivityBehavior) execution -> {
                    System.out.println("Start");
                    execution.setVariable("source", "start");
                    execution.setVariable("time", new Date(System.currentTimeMillis()));
                    moveNext(execution);
                })
                .transition("Day1")
                .endActivity()
                .createActivity("Day1")
                .behavior((ActivityBehavior) execution -> {
                    simpleService.execute(execution);
                    moveNext(execution);


                })
                .transition("Day2")
                .endActivity()
                .createActivity("Day2")
                .behavior((ActivityBehavior) execution -> {
                    simpleService.execute(execution);
                })
                .endTransition()
                .endActivity()
                .buildProcessDefinition();

        PvmProcessInstance processInstance = pvmProcessDefinition.createProcessInstance();
        processInstance.start();

    }

    private void moveNext(ActivityExecution execution) {
        List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
        if(outgoingTransitions.isEmpty()) {
            execution.end();
        } else {
            execution.take(outgoingTransitions.get(0));
        }
    }


}
