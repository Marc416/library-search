package com.library.config

import mu.KotlinLogging
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor

private val logger = KotlinLogging.logger { }

@Configuration
class AsyncConfig : AsyncConfigurer {
    @Bean("lsExecutor")
    override fun getAsyncExecutor(): Executor? {
        // 설정값은 상황에 따라 설정
        // 애플리케이션이 CPU 집약적 프로그램이면 연산이 많음 -> 코어스레드 수 맥스 스레드 수 같게,
        // IO 집약적 프로그램이면 -> 맥스 풀 사이즈가 코어 풀 사이즈보다 두배 세배
        //
        val executor = ThreadPoolTaskExecutor()
        val cpuCoreCount = Runtime.getRuntime().availableProcessors()
        executor.corePoolSize = 2   // 작업이 없더라도 계속 유지
        executor.maxPoolSize = 4    // 최대 쓰레드 수, 무제한 값으로도 설정 가능. 21억까지 실무에서 사용 가능. 장애상황을 없앤다.
        // 큐사이즈를 설정하지 않으면 Integer.MAX_VALUE 로 설정됨
        executor.queueCapacity = 2  // 대기 하기로 한 큐 수
        executor.keepAliveSeconds = 60  // 최대 쓰레드 수 이상의 여분 쓰레드가 삭제까지 살아 있는 시간
        executor.setWaitForTasksToCompleteOnShutdown(true)  // 앱이 종료되더라도 작업을 할 것인지
        executor.setAwaitTerminationSeconds(60) // 앱이 종료되더라도 작업을 할 것인지 대기 시간
        executor.setThreadNamePrefix("LS-")
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return CustomAsyncExceptionHandler()
    }

    private class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {
        override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any) {
            logger.error("Failed to execute {}", ex.message)
            params.forEach { param -> logger.error("parameter value = {}", param) }
        }
    }
}
