package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.function.Supplier;


@Component
public class AsyncExecutionUtil {

    private Executor myExecutor;

    private ScheduledExecutorService SCHEDULER;
    private ContextCopyingDecorator decorator = new ContextCopyingDecorator();

    @Autowired
    public AsyncExecutionUtil(@Qualifier("asyncExecutor") Executor myExecutor) {
        this.myExecutor = myExecutor;
        SCHEDULER = new ScheduledThreadPoolExecutor(2);
    }

    private Executor delayedExecutor(long delay, TimeUnit unit) {
        return r -> {
            Runnable decorated = decorator.decorate(r);
            SCHEDULER.schedule(decorated, delay, unit);
        };
    }


    public <T> CompletableFuture<T> executeDelayed(Supplier<T> task, Predicate<T> condition, long delayInMs) {
        //TODO если поменять строки то работает
        CompletableFuture<T> future = CompletableFuture.supplyAsync(task, delayedExecutor(delayInMs, TimeUnit.MILLISECONDS));
        //CompletableFuture<T> future = CompletableFuture.supplyAsync(task, myExecutor);

        return future.thenCompose(result -> {
            if (condition.test(result)) {
                return CompletableFuture.completedFuture(result);
            } else {
                return executeDelayed(task, condition, delayInMs);
            }
        });
    }

    public <T> CompletableFuture<T> executeDelayedWithRetries(Supplier<T> task, Predicate<T> condition, long delayInMs, int retries) {
        //TODO если поменять строки то работает
        CompletableFuture<T> future = CompletableFuture.supplyAsync(task, delayedExecutor(delayInMs, TimeUnit.MILLISECONDS));
        //CompletableFuture<T> future = CompletableFuture.supplyAsync(task, myExecutor);
        System.out.println("In executeDelayedWithRetries");
        return future.thenCompose(result -> {
            if (condition.test(result)) {
                System.out.println("success in executeDelayedWithRetries");
                return CompletableFuture.completedFuture(result);
            } else if (retries <= 0) {
                System.out.println("retries all in executeDelayedWithRetries");
                CompletableFuture<T> failedFuture = new CompletableFuture<>();
                failedFuture.completeExceptionally(new CompletionException("Retries exhausted: task did not meet the condition.", null));
                return failedFuture;
            } else {
                System.out.println("one more executeDelayedWithRetries: " + retries);
                return executeDelayedWithRetries(task, condition, delayInMs, retries - 1);
            }
        });
    }
}