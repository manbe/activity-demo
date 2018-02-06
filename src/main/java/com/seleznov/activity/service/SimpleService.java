package com.seleznov.activity.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SimpleService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("===============");
        System.out.println("source=" + execution.getVariable("source") + " time=" + execution.getVariable("time"));
        execution.setVariable("time", new Date(System.currentTimeMillis()));
        execution.setVariable("source", execution.getCurrentActivityId());
    }
}
