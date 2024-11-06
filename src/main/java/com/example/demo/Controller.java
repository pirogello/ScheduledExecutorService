package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async")
    public void doit() {
        asyncService.processAsyncTask();
    }
}
