package com.seleznov.activity.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SimpleService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        Date currentTime = new Date(System.currentTimeMillis());
        System.out.println("===============");
        System.out.println("source=" + execution.getVariable("source")
                + " previousTime=" + execution.getVariable("time")
                + " currentTime=" + currentTime);

        execution.setVariable("time", currentTime);
        execution.setVariable("source", execution.getCurrentActivityId());
    }
}
