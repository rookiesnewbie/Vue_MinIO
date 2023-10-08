package com.example.minio.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author LiTeng
 * @Date 2023/10/5 23:06
 * Version 1.0
 * @Description 线程池配置
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        //  自定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(true), // 公平策略（先进先出（FIFO））
                Executors.defaultThreadFactory(),
// ThreadPoolExecutor自带的拒绝策略如下：
//   1. AbortPolicy(默认)：直接抛出RejectedExecutionException异常阻止系统正常运行
//   2. CallerRunsPolicy：“调用者运行”一种调节机制，该策略既不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
//   3. DiscardOldestPolicy：抛弃队列中等待最久的任务，然后把当前任务加人队列中 尝试再次提交当前任务。
//   4. DiscardPolicy：该策略默默地丢弃无法处理的任务，不予任何处理也不抛出异常。 如果允许任务丢失，这是最好的一种策略。
                new ThreadPoolExecutor.AbortPolicy()
        );
        return threadPoolExecutor;
    }
}