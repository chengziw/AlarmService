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
 * 报警请求参数记录
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
@TableName("alert_param_record")
public class AlertParamRecord extends BaseAlertInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 应用唯一标识
     */
    private String appId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息内容特征值
     */
    private String contentKey;

    /**
     * 事件时间戳
     */
    private Date eventTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

}
