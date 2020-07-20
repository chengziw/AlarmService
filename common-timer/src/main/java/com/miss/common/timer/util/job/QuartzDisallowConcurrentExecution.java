package com.miss.common.timer.util.job;

import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.job.AbstractQuartzJob;
import com.miss.common.timer.util.job.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author ruoyi
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, JobPojo jobPojo) throws Exception {
        JobInvokeUtil.invokeMethod(jobPojo);
    }

}