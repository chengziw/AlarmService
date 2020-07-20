package com.miss.common.timer.util.job;

import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.job.AbstractQuartzJob;
import com.miss.common.timer.util.job.JobInvokeUtil;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author ruoyi
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, JobPojo jobPojo) throws Exception {
        JobInvokeUtil.invokeMethod(jobPojo);
    }

}