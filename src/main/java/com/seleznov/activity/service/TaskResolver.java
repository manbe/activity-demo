package com.seleznov.activity.service;

import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskResolver implements JavaDelegate {

    @Autowired
    private SimpleService simpleService;

    @Override
    public void execute(DelegateExecution execution) {
        ServiceTask currentFlowElement = (ServiceTask) execution.getCurrentFlowElement();
        String type = currentFlowElement.getFieldExtensions().get(0).getStringValue();

        System.out.println("DELEGATE WITH TYPE = " + type);

        simpleService.execute(execution);

        System.out.println();
        System.out.println();
    }
}
