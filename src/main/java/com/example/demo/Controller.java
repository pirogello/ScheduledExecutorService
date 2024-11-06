package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.CompletableFuture;

@RestController
public class Controller {
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async")
    @Async
    public CompletableFuture<Object> doit() {
        RequestContextHolder.getRequestAttributes().setAttribute("name", "Name", RequestAttributes.SCOPE_REQUEST);
        return asyncService.processAsyncTask();
    }

}
