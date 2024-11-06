package com.example.demo;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ContextCopyingDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            System.out.println("requestAttributes is null");
            return runnable;
        }

        final ServletRequestAttributes capturedRequestAttributes = requestAttributes;
        return () -> {
            try {
                System.out.println("decorate try");
                RequestContextHolder.setRequestAttributes(capturedRequestAttributes);
                System.out.println("decorated!");
                runnable.run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}

