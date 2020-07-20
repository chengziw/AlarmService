package com.miss.common.timer.manage;

import com.alibaba.fastjson.JSON;
import com.miss.common.timer.config.JobPool;
import com.miss.common.timer.constant.Constants;
import com.miss.common.timer.constant.ScheduleConstants;
import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.job.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时任务服务
 */
@Slf4j
public class TimerTaskManager {

    @Autowired
    private Scheduler scheduler;

    /**
     * 启动任务
     *
     * @param jobId
     */
    public void startJob(String jobId) {
        try {
            JobPojo jobPojo = JobPool.getJob(jobId);
            if(jobPojo == null) {
                log.warn(Constants.JOB_START,Constants.JOB_OPERATOR,"启动任务失败,未获取到任务信息，任务ID：{}",jobId);
                return;
            }

            log.debug(Constants.JOB_START,Constants.JOB_OPERATOR,"开启任务，任务ID：{}",jobId);
            ScheduleUtils.createScheduleJob(scheduler, jobPojo);
        } catch (Exception e) {
            log.error("启动任务","定时器",e,"启动任务异常，任务ID：{}",jobId);
        }
    }

    /**
     * 暂停任务
     *
     * @param jobId
     */
    public void pauseJob(String jobId) {
        try {
            JobPojo jobPojo = JobPool.getJob(jobId);
            if(jobPojo == null) {
                log.warn(Constants.JOB_PAUSE,Constants.JOB_OPERATOR,"暂停任务失败，未获取到任务信息，任务ID：{}",jobId);
                return;
            }

            log.debug(Constants.JOB_PAUSE,Constants.JOB_OPERATOR,"暂停任务，任务ID；{}",jobId);
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobPojo.getJobGroup()));
        } catch (Exception e) {
            log.error(Constants.JOB_PAUSE,Constants.JOB_OPERATOR,e,"暂停任务异常，任务ID：{}",jobId);
        }
    }

    /**
     * 恢复任务
     *
     * @param jobId
     */
    public void resumeJob(String jobId) {
        try {
            JobPojo jobPojo = JobPool.getJob(jobId);
            if(jobPojo == null) {
                log.warn(Constants.JOB_RESUME,Constants.JOB_OPERATOR,"恢复任务失败，未获取到任务信息，任务ID：{}",jobId);
                return;
            }

            log.debug(Constants.JOB_RESUME,Constants.JOB_OPERATOR,"恢复任务，任务ID：{}",jobId);
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobPojo.getJobGroup()));
        } catch (Exception e) {
            log.error(Constants.JOB_RESUME,Constants.JOB_OPERATOR,e,"恢复任务异常，任务ID：{}",jobId);
        }
    }

    /**
     * 删除任务
     *
     * @param jobId
     */
    public void deleteJob(String jobId) {
        try {
            JobPojo jobPojo = JobPool.getJob(jobId);
            if(jobPojo == null) {
                log.warn(Constants.JOB_DELETE,Constants.JOB_OPERATOR,"删除任务失败，未获取到任务信息，任务ID：{}",jobId);
                return;
            }

            log.debug(Constants.JOB_DELETE,Constants.JOB_OPERATOR,"删除任务，任务ID：{}",jobId);
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobPojo.getJobGroup()));
        } catch (Exception e) {
            log.error(Constants.JOB_DELETE,Constants.JOB_OPERATOR,e,"删除任务异常，任务ID：{}",jobId);
        }
    }

    /**
     * 更新任务
     *
     * @param jobPojo
     */
    public void updateJob(JobPojo jobPojo) {
        try {
            JobPool.setJob(jobPojo);

            JobKey jobKey = ScheduleUtils.getJobKey(jobPojo.getJobId(),jobPojo.getJobGroup());
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }

            startJob(jobPojo.getJobId());
        }catch (Exception e) {
            log.error(Constants.JOB_UPDATE,Constants.JOB_OPERATOR,e,"更新任务异常，任务信息：{}",JSON.toJSONString(jobPojo));
        }
    }

    /**
     * 立即执行一次
     */
    public void immediateRun(String jobId) {
        try {
            JobPojo jobPojo = JobPool.getJob(jobId);
            if(jobPojo == null) {
                log.warn(Constants.JOB_IMD,Constants.JOB_OPERATOR,"立即执行任务失败，未获取到任务信息，任务ID：{}",jobId);
                return;
            }

            log.debug(Constants.JOB_IMD,Constants.JOB_OPERATOR,"立即执行任务，任务ID：{}",jobId);
            ScheduleUtils.onceJob(scheduler,jobPojo);
        }catch (Exception e) {
            log.error(Constants.JOB_IMD,Constants.JOB_OPERATOR,e,"立即执行任务异常，任务ID：{}",jobId);
        }
    }

    /**
     * 添加任务
     * @param jobPojo
     * @return
     */
    public boolean addJob(JobPojo jobPojo) {
        JobPojo orig = JobPool.getJob(jobPojo.getJobId());
        if(orig != null) {
            log.warn(Constants.JOB_ADD,Constants.JOB_OPERATOR,"添加任务失败，任务已存在，任务ID：{}",jobPojo.getJobId());
            return false;
        }

        JobPool.setJob(jobPojo);
        if(jobPojo.getStatus().equalsIgnoreCase(ScheduleConstants.Status.NORMAL.getValue())){
            startJob(jobPojo.getJobId());
        }
        return true;
    }
}
