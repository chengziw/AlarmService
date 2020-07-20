package com.miss.common.timer.util.job;


import com.miss.common.timer.constant.Constants;
import com.miss.common.timer.constant.ScheduleConstants;
import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.string.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobPojo jobPojo = new JobPojo();
        BeanUtils.copyBeanProp(jobPojo, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, jobPojo);
            if (jobPojo != null) {
                doExecute(context, jobPojo);
            }
            after(context, jobPojo, null);
        } catch (Exception e) {
            log.error("任务执行","定时器",e,"任务执行异常");
            after(context, jobPojo, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param jobPojo 计划任务
     */
    protected void before(JobExecutionContext context, JobPojo jobPojo) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  计划任务
     */
    protected void after(JobExecutionContext context, JobPojo sysJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();
        long runMs = new Date().getTime()-startTime.getTime();
        log.debug("执行耗时", Constants.JOB_OPERATOR,runMs,"任务名称：{}",sysJob.getJobName());
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param jobPojo 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, JobPojo jobPojo) throws Exception;
}
