package com.qinweizhao.account.controller;

import com.qinweizhao.account.service.EsService;
import com.qinweizhao.account.utils.Result;
import com.qinweizhao.account.vo.SearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/23 12:16
 * </p>
 */
@Api(tags = "ES操作")
@RestController
@RequestMapping("/es")
public class EsController {

    @Autowired
    EsService esService;


    @ApiOperation("获取ES中所有的数据存入DB")
    @GetMapping("/getAllEsDataToDb")
    public Result getAllEsData(){
        Result r = esService.getAllEsDataToDb();
        return r;
    }



    @ApiOperation("将数据库中数据全量导入ES")
    @PostMapping("/saveToEs")
    public Result saveToEs(){
        Result r = esService.saveToEs();
        return r;
    }

    @ApiOperation("根据条件检索")
    @PostMapping("/search")
    public Result search(@RequestBody SearchParam param){
        Result result = esService.searchAccount(param);
        return result;
    }





}
