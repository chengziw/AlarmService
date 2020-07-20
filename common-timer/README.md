## 1.引入依赖

```xml
<dependency>
    <groupId>com.miss</groupId>
    <artifactId>common-timer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 2.配置任务

resources目录下创建tasks.json 文件

```json
[
  {
    "jobId": "test",
    "jobName": "testJob",
    "jobGroup": "testGroup",
    "cronExpression": "0/1 * * * * ?",
    "invokeTarget": "timerTest.test()",
    "status": "0",
    "concurrent": "0",
    "misfirePolicy": "0"
  },
  {
    "jobId": "test2",
    "jobName": "testJob2",
    "jobGroup": "testGroup",
    "cronExpression": "0/2 * * * * ?",
    "invokeTarget": "com.miss.common.mq.test.timer.TestTimer.test('test')",
    "status": "0",
    "concurrent": "0",
    "misfirePolicy": "0"
  }
]
```

备注：

如果普通类执行业务 invokeTarget配置全路径+方法

如果使用bean执行业务 invokeTarget 配置bean+方法

## 3.开发示例

### 3.1 业务开发

#### 3.1.1 普通类业务

```java
package com.miss.common.mq.test.timer;

public class TestTimer {
    public void test(String param) {
        System.out.println(">>>>>>>普通类任务执行：" + param);
    }
}
```

#### 3.1.2 bean业务

```java
package com.miss.common.mq.test.timer;

@Component
public class TimerTest {
    public void test() {
        System.out.println(">>>>>>bean任务执行>>>>>");
    }
}
```



### 3.2 开发场景

#### 3.2.1 普通场景

描述：只关注业务开发本身，不提供对外接口，进行任务(开启、暂停、删除等)的状态控制

只开发3.1中业务实现即可

#### 3.2.2 对外场景

描述：除了关注本身业务开发，还需要对外提供接口，进行任务(开启、暂停、删除等)的状态控制

除开发3.1中业务实现，还需要开发对外controller，继承TimerBaseController类

```java
@RestController
@RequestMapping("/job")
public class JobController extends TimerBaseController {
    /**
     * 启动任务
     *
     * @param jobId
     */
    @GetMapping("/start")
    public void startJob(String jobId) {
        super.startJob(jobId);
    }

    /**
     * 暂停任务
     *
     * @param jobId
     */
    @GetMapping("/pause")
    public void pauseJob(String jobId) {
        super.pauseJob(jobId);
    }

    /**
     * 恢复任务
     *
     * @param jobId
     */
    @GetMapping("/resume")
    public void resumeJob(String jobId) {
        super.resumeJob(jobId);
    }

    /**
     * 删除任务
     *
     * @param jobId
     */
    @GetMapping("/delete")
    public void deleteJob(String jobId) {
        super.deleteJob(jobId);
    }

    /**
     * 更新任务
     *
     * @param jobPojo
     */
    @GetMapping("/update")
    public void updateJob(JobPojo jobPojo) {
        super.updateJob(jobPojo);
    }

    /**
     * 立即执行一次
     */
    @GetMapping("/immediate")
    public void immediateRun(String jobId) {
        super.immediateRun(jobId);
    }

    /**
     * 添加任务
     * @param jobPojo
     * @return
     */
    @GetMapping("/add")
    public boolean addJob(JobPojo jobPojo) {
        return super.addJob(jobPojo);
    }
}
```



## 4.任务配置字段说明

| 名称           | 类型   | 备注                                                         |
| -------------- | ------ | ------------------------------------------------------------ |
| jobId          | string | 任务ID                                                       |
| jobName        | string | 任务名称                                                     |
| jobGroup       | string | 任务组名                                                     |
| invokeTarget   | string | 调用目标字符串                                               |
| cronExpression | string | cron执行表达式                                               |
| misfirePolicy  | string | 0：默认，1：立即触发执行，2：触发一次执行，3：不触发立即执行 |
| concurrent     | string | 是否并发执行（0允许 1禁止）                                  |
| status         | string | 任务状态（0正常 1暂停）                                      |

