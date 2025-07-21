package com.xyt.cloudAtlas.business.manager;

import jakarta.websocket.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManager {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<Session, Future<?>> sessionTasks = new ConcurrentHashMap<>();

    public void submitTask(Session session, Runnable task) {
        // 提交任务到线程池，并保存Future
        Future<?> future = executorService.submit(task);
        sessionTasks.put(session, future);
    }

    public void stopTask(Session session) {
        Future<?> future = sessionTasks.get(session);
        if (future != null) {
            future.cancel(true); // 中断任务
            sessionTasks.remove(session);
        }
    }

    // 关闭线程池
    public void shutdown() {
        executorService.shutdown();
    }
}
