package com.miss.common.timer.config;

import com.miss.common.timer.manage.TimerTaskManager;
import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.file.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * json文件加载
 */
@Slf4j
public class JobPool implements CommandLineRunner {
    @Autowired
    private TimerTaskManager timerTaskManager;

    protected static final Map<String, JobPojo> jobMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args)  {
        List<JobPojo> jobs = JsonUtils.readJobsFromClassPath("tasks.json");
        if(jobs.isEmpty()) {
            log.warn("加载任务信息","定时器","未加载到任何任务信息");
            return;
        }

        jobMap.putAll(jobs.stream().collect(Collectors.toMap(JobPojo::getJobId, job -> job)));
        jobs.stream().filter(job -> job.getStatus().equalsIgnoreCase("0")).forEach(job -> timerTaskManager.startJob(job.getJobId()));
    }

    public static JobPojo getJob(String jobId) {
        return jobMap.get(jobId);
    }

    public static void setJob(JobPojo job) {
        jobMap.put(job.getJobId(), job);
    }
}
