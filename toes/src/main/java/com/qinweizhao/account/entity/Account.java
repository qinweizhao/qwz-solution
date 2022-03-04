package com.qinweizhao.account.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Account对象", description="")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    private Integer accountNumber;

    private Integer balance;

    private String firstname;

    private String lastname;

    private Integer age;

    private String gender;

    private String address;

    private String employer;

    private String email;

    private String city;

    private String state;


}
