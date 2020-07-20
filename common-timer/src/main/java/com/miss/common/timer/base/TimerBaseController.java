package com.miss.common.timer.base;


import com.miss.common.timer.manage.TimerTaskManager;
import com.miss.common.timer.po.JobPojo;
import com.miss.common.timer.util.spring.TimerSpringUtil;

public class TimerBaseController {
    /**
     * 启动任务
     *
     * @param jobId
     */
    public void startJob(String jobId) {
        TimerSpringUtil.getBean(TimerTaskManager.class).startJob(jobId);
    }

    /**
     * 暂停任务
     *
     * @param jobId
     */
    public void pauseJob(String jobId) {
        TimerSpringUtil.getBean(TimerTaskManager.class).pauseJob(jobId);
    }

    /**
     * 恢复任务
     *
     * @param jobId
     */
    public void resumeJob(String jobId) {
        TimerSpringUtil.getBean(TimerTaskManager.class).resumeJob(jobId);
    }

    /**
     * 删除任务
     *
     * @param jobId
     */
    public void deleteJob(String jobId) {
        TimerSpringUtil.getBean(TimerTaskManager.class).deleteJob(jobId);
    }

    /**
     * 更新任务
     *
     * @param jobPojo
     */
    public void updateJob(JobPojo jobPojo) {
        TimerSpringUtil.getBean(TimerTaskManager.class).updateJob(jobPojo);
    }

    /**
     * 立即执行一次
     */
    public void immediateRun(String jobId) {
        TimerSpringUtil.getBean(TimerTaskManager.class).immediateRun(jobId);
    }

    /**
     * 添加任务
     * @param jobPojo
     * @return
     */
    public boolean addJob(JobPojo jobPojo) {
        return TimerSpringUtil.getBean(TimerTaskManager.class).addJob(jobPojo);
    }


}
