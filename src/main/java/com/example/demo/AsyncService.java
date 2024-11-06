package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Autowired
    private Request request;

    @Autowired
    private AsyncExecutionUtil asyncExecutionUtil;

    public CompletableFuture<Object> processAsyncTask() {
        return asyncExecutionUtil.executeDelayedWithRetries(()-> {
            // TODO обращение к параметрам запроса - работает
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            System.out.println(request.getAttribute("name"));
            // TODO обращение к бину со скоупом request в асинхронной задаче - не работает
            //System.out.println(request.getUser());
            return null;
        }, r-> false, 10, 1000).exceptionally(e ->{
            System.out.println(e.getMessage());
            return null;
        });
    }
}
