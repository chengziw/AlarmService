package com.miss.server.alert.domain.service;

import com.alibaba.fastjson.JSON;
import com.miss.server.alert.domain.repository.IAlertRepository;
import com.miss.server.alert.sdk.bean.AlertParam;
import com.miss.server.alert.sdk.bean.AlertParamExtend;
import com.miss.server.alert.sdk.enums.AlertLevelEnum;
import com.miss.server.alert.sdk.enums.PriorityLevelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.miss.server.alert.domain.model.GroupSubscription;
import com.miss.server.alert.domain.util.ReflectUtil;
import com.miss.server.alert.domain.util.SnowflakeIdWorker;
import com.miss.server.alert.infrastructure.model.AlertGroupSubscriptionLazy;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@DependsOn("com.miss.server.alert.sdk.util.SpringContextUtil")
@Service("alertDomainService")
public class AlertDomainService {

    private static final SnowflakeIdWorker flakeWorker = SnowflakeIdWorker.instance();

    private static Lock initLock = new ReentrantLock();
    private static final String PLACE_HOLDER = "*";

    private boolean isSaveMongoDB = false;
    private boolean isSaveDB = true;

    @Value("${app.environment}")
    private String environment;

    private List<ICustomer> customerList = new ArrayList<>();

    private List<AlertGroupSubscriptionLazy> lazyList = new ArrayList<>();

    private Map<String, List<Long>> lazyMap = new HashMap<>();

    @Resource
    private IAlertRepository alertRepository;

    @PostConstruct
    public void init() {
        if (!initLock.tryLock()) {
            log.debug("报警服务",null,"~~~~初始化进行中，放弃本次操作~~~~");
            return;
        }
        try {
            log.debug("报警服务",null,">>> environment:" + environment);
            // 获取订阅列表
            List<GroupSubscription> subList = alertRepository.findAllGroupSubscriptionList();
            log.debug("报警服务",null,"加载订阅数量>>>" + (null == subList ? 0 : subList.size()));
            if (null != subList && subList.size() > 0) {
                // 移除不使用的订阅
                removeUnusedSub(subList);

                // 添加新订阅
                addNewSub(subList);

                // 更新订阅信息
                updateSubContent(subList);
            }

            // 获取懒发送配置信息
            lazyList = alertRepository.findLazyList();
            log.debug("报警服务",null,"懒发送配置信息数量>>>" + (null == lazyList ? 0 : lazyList.size()));
        } finally {
            initLock.unlock();
        }
    }

    // 移除不使用的订阅
    private void removeUnusedSub(List<GroupSubscription> subList) {
        log.debug("报警服务",null," ------- 移除订阅 ------- ");
        // 操作customerList前，加智能锁
        List<ICustomer> synCustomerList = Collections.synchronizedList(customerList);

        boolean isDel;
        List<ICustomer> customerDelList = new ArrayList<>();
        // 获取多余的订阅
        for (ICustomer customer : customerList) {
            // 默认删除
            isDel = true;
            for (GroupSubscription sub : subList) {
                if (customer.equalsSub(sub)) {
                    isDel = false;
                    break;
                }
            }
            if (isDel) {
                customerDelList.add(customer);
            }
        }

        // 删除多余订阅
        if (customerDelList.size() > 0) {
            synchronized (customerList) {
                for (ICustomer delCustomer : customerDelList) {
                    synCustomerList.remove(delCustomer);
                    log.debug("报警服务",null,"移除订阅 >>> " + JSON.toJSONString(delCustomer.getSubRecord()));
                }
            }
        }
    }

    // 添加新的订阅
    private void addNewSub(List<GroupSubscription> subList) {
        log.debug("报警服务",null," ------- 增加订阅 ------- ");
        // 操作customerList前，加智能锁
        List<ICustomer> synCustomerList = Collections.synchronizedList(customerList);

        boolean isAdd;
        for (GroupSubscription sub : subList) {
            // 默认添加
            isAdd = true;
            for (ICustomer customer : customerList) {
                if (customer.equalsSub(sub)) {
                    isAdd = false;
                    break;
                }
            }

            if (isAdd) {
                SubscriptionCustomer customer = new SubscriptionCustomer(sub); // 订阅者
                CustomerDecorator decorator = new CustomerDecorator(customer); // 包装一下
                SubscriptionBlackDecorator blackD = new SubscriptionBlackDecorator(decorator); // 匹配订阅中的例外
                SubscriptionDecorator subD = new SubscriptionDecorator(blackD); // 匹配订阅

                synchronized (customerList) {
                    synCustomerList.add(subD);
                    log.debug("报警服务",null,"增加订阅 >>> " + JSON.toJSONString(customer.getSubRecord()));
                }
            }
        }
    }

    // 更新订阅信息
    private void updateSubContent(List<GroupSubscription> subList) {
        log.debug("报警服务",null," ------- 更新订阅 ------- ");
        for (ICustomer customer : customerList) {
            for (GroupSubscription sub : subList) {
                if (customer.equalsSub(sub)) {
                    customer.updateGroupSubscription(sub);
                    log.debug("报警服务",null,"更新订阅 >>> " + JSON.toJSONString(customer.getSubRecord()));
                    break;
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param param
     */
    public void sendAlertMsg(AlertParam param) {
        param = pre_dispose(param);

        // 处理消息体，解析发送内容
        AlertParamExtend alertParamExtend = dispose(param);

        // 记录发送消息请求体(MongoDB)
        if (isSaveMongoDB) {

        }
        // 记录发送消息请求体(Mysql)
        if (isSaveDB) {
            alertRepository.saveAlertRecord(alertParamExtend);
        }

        // 懒发送过滤
        if (lazySendFilter(alertParamExtend)) {
            log.warn("报警服务",null,"~~~~懒发送过滤~~~~" + JSON.toJSONString(param));
            return;
        }

        // 操作customerList前，加智能锁
        List<ICustomer> synCustomerList = Collections.synchronizedList(customerList);

        // 预发送
        if (null != customerList && customerList.size() > 0) {
            synchronized (customerList) {
                for (ICustomer customer : synCustomerList) {
                    customer.preSend(alertParamExtend);
                }
            }
        }
    }

    // 参数处理前，规范参数格式
    private AlertParam pre_dispose(AlertParam param) {
        // 报警等级
        if (null == param.getAlertLevel()) {
            param.setAlertLevel(AlertLevelEnum.getEnum(PLACE_HOLDER));
        } else {
            param.setAlertLevel(param.getAlertLevel());
        }
        // 优先等级
        if (null == param.getPriorityLevel()) {
            param.setPriorityLevel(PriorityLevelEnum.getEnum(PLACE_HOLDER));
        } else {
            param.setPriorityLevel(param.getPriorityLevel());
        }
        // 应用名称
        if (StringUtils.isBlank(param.getAppName())) {
            param.setAppName(PLACE_HOLDER);
        } else {
            param.setAppName(param.getAppName().toUpperCase());
        }
        // 模块名称
        if (StringUtils.isBlank(param.getModuleName())) {
            param.setModuleName(PLACE_HOLDER);
        }
        // 事件名称
        if (StringUtils.isBlank(param.getEventName())) {
            param.setEventName(PLACE_HOLDER);
        }

        return param;
    }

    // 处理参数
    private AlertParamExtend dispose(AlertParam alertParam) {
        AlertParamExtend result = null;
        try {
            result = ReflectUtil.fatherToChild(alertParam, AlertParamExtend.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setSystem(environment);
        result.setSerialNumber(String.valueOf(flakeWorker.nextId()));

        // 处理content，提取特征值
        result.setContentKey(extractContentKey(alertParam.getContent()));

        return result;
    }

    // 提取消息特征值
    private String extractContentKey(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }

        // 去除数字
        return content.replaceAll("\\d+", "");
    }

    // 是否通过懒发送拦截
    private boolean lazySendFilter(AlertParamExtend alertParamExtend) {
        // 匹配懒发送
        AlertGroupSubscriptionLazy lazy = getMatchLazy(alertParamExtend);
        if (null == lazy) {
            log.warn("报警服务",null,">>> 匹配懒发送 未匹配");
            return false;
        }

        // 获取消息hash
        String hash = String.valueOf(alertParamExtend.hashCode());

        synchronized (lazyMap) {
            // 添加本次记录
            List<Long> tempList = lazyMap.get(hash);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }

            Long now = System.currentTimeMillis();
            tempList.add(now);

            if (tempList.size() >= lazy.getLazyNum()) {
                // 处理超时记录
                Long interval = lazy.getIntervalInt() * 60 * 1000L;
                List<Long> newList = new ArrayList<>();
                for (Long record : tempList) {
                    if (now - record < interval) {
                        newList.add(record);
                    }
                }

                log.debug("报警服务",null,"......处理后积攒条数：" + newList.size() + "/" + lazy.getLazyNum());
                // 是否满足发送条件
                if (newList.size() >= lazy.getLazyNum()) {
                    lazyMap.remove(hash);
                    log.debug("报警服务",null,">>> 懒发送 满足发送条件");
                    return false;
                }

                lazyMap.put(hash, newList);
            } else {
                lazyMap.put(hash, tempList);
                log.debug("报警服务",null,"......积攒条数：" + tempList.size() + "/" + lazy.getLazyNum());
            }

            return true;
        }
    }

    // 获取匹配的懒发送规则
    private AlertGroupSubscriptionLazy getMatchLazy(AlertParamExtend alertParamExtend) {
        // 操作customerList前，加智能锁
        List<AlertGroupSubscriptionLazy> synLazyList = Collections.synchronizedList(lazyList);
        if (null != lazyList && lazyList.size() > 0) {
            synchronized (lazyList) {
                for (AlertGroupSubscriptionLazy lazy : synLazyList) {
                    if (!alertParamExtend.getAppName().equalsIgnoreCase(lazy.getAppName())
                            && !PLACE_HOLDER.equals(lazy.getAppName())) {
                        continue;
                    }
                    if (!alertParamExtend.getModuleName().equalsIgnoreCase(lazy.getModuleName())
                            && !PLACE_HOLDER.equals(lazy.getModuleName())) {
                        continue;
                    }
                    if (!alertParamExtend.getEventName().equalsIgnoreCase(lazy.getEventName())
                            && !PLACE_HOLDER.equals(lazy.getEventName())) {
                        continue;
                    }
                    if (!alertParamExtend.getAlertLevel().equals(lazy.getAlertLevel())
                            && !PLACE_HOLDER.equals(lazy.getAlertLevel())) {
                        continue;
                    }
                    if (!alertParamExtend.getPriorityLevel().equals(lazy.getPriorityLevel())
                            && !PLACE_HOLDER.equals(lazy.getPriorityLevel())) {
                        continue;
                    }
                    return lazy;
                }
            }
        }

        return null;
    }

}
