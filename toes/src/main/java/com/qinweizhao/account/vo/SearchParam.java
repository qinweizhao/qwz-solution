package com.qinweizhao.account.vo;

import lombok.Data;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/31 14:36
 * </p>
 */
@Data
public class SearchParam {

    private String _queryString;//原生的所有查询条件

    private String address; //地址

    private String age; //年龄  10—20

    private String gender; //性别

    private Integer pageNum = 1;//页码

    private String sort;//排序条件


}
