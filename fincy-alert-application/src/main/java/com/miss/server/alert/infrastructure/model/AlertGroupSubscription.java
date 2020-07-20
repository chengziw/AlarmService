package com.miss.server.alert.infrastructure.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 报警订阅信息
 *
 * @Auther: liq
 * @Date: 2019/6/12 14:25
 * @Description:
 */
@Accessors(chain = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("alert_group_subscription")
public class AlertGroupSubscription extends BaseAlertInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提醒类型
     */
    private String alertType;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 应用唯一标识
     */
    private String appId;

    /**
     * 发送间隔
     */
    private Integer intervalInt;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

}
