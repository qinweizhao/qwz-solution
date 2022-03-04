package com.qinweizhao.account.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qinweizhao.account.entity.Account;
import com.qinweizhao.account.service.AccountService;
import com.qinweizhao.account.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
@Api(tags = "账户操作")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @ApiOperation("添加账户")
    @GetMapping("/save")
    public Result save(@RequestBody Account account){
        boolean save = accountService.save(account);
        if (!save){
            return Result.error("保存失败");
        }
        return Result.ok("保存成功");
    }

    @ApiOperation("添加账户")
    @GetMapping("/saveBatch")
    public Result saveBatch(@RequestBody List<Account> accounts){
        boolean save = accountService.saveBatch(accounts);
        if (!save){
            return Result.error("保存失败");
        }
        return Result.ok("保存成功");
    }


    @ApiOperation("删除账户")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        boolean b = accountService.removeByIds(Arrays.asList(ids));
        if (!b){
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }

    @ApiOperation("删除账户")
    @PutMapping("/update")
    public Result update(@RequestBody Account account){
        boolean update = accountService.update(new QueryWrapper<Account>().eq("id",account.getId()));
        if (!update){
            return Result.error("更新失败");
        }
        return Result.ok("更新成功");
    }




}
