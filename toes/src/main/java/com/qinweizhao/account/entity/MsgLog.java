package com.qinweizhao.account.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息投递日志
 * </p>
 *
 * @author Monday_1201
 * @since 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MsgLog对象", description="消息投递日志")
public class MsgLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息唯一标识")
    private String msgId;

    @ApiModelProperty(value = "消息体, json格式化")
    private String msg;

    @ApiModelProperty(value = "交换机")
    private String exchange;

    @ApiModelProperty(value = "路由键")
    private String routingKey;

    @ApiModelProperty(value = "状态: 0投递中 1投递成功 2投递失败 3已消费")
    private Integer status;

    @ApiModelProperty(value = "重试次数")
    private Integer tryCount;

    @ApiModelProperty(value = "下一次重试时间")
    private LocalDateTime nextTryTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
