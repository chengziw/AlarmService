package com.miss.common.timer.po;

import com.miss.common.timer.constant.ScheduleConstants;
import com.miss.common.timer.util.job.CronUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务pojo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPojo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略
     */
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    private String concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
    private String status;


    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    /**
     * 开始时间
     */
    private Date startTime;

    private Date stopTime;

    private String jobMessage;
}
