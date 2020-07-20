package com.miss.common.timer;

import com.miss.common.timer.config.JobPool;
import com.miss.common.timer.manage.TimerTaskManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({TimerTaskManager.class, JobPool.class})
@ComponentScan("com.miss.common.timer")
public class TimeAutoConfig {
    @Bean
    @ConditionalOnMissingBean(JobPool.class)
    public JobPool getJsonLoad(){
        return new JobPool();
    }

    @Bean
    @ConditionalOnMissingBean(TimerTaskManager.class)
    public TimerTaskManager getTimerTaskService() {
        return new TimerTaskManager();
    }
}
