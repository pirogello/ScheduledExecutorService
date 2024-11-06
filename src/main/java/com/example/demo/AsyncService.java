package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Autowired
    private Request request;

    @Autowired
    private AsyncExecutionUtil asyncExecutionUtil;

    public void processAsyncTask() {
        asyncExecutionUtil.executeDelayedWithRetries(()->{
            // TODO обращение к бину со скоупом request в асинхронной задаче
            System.out.println(request.getUser());
            return null;
        }, r-> false, 100, 4).exceptionally(e ->{
            System.out.println(e.getMessage());
            return null;
        });
    }

}
