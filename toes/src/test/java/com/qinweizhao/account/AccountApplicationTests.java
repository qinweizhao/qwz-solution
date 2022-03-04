package com.qinweizhao.account;


import com.alibaba.fastjson.JSON;
import com.qinweizhao.account.config.EsConfig;
import com.qinweizhao.account.entity.Account;
import com.qinweizhao.account.service.AccountService;
import com.qinweizhao.account.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
class AccountApplicationTests {


    @Autowired
    RestHighLevelClient client;

    @Autowired
    AccountService accountService;
    @Test
    void contextLoads() {

        Account account = new Account();
        account.setAccountNumber(1);
        account.setAddress("中国");
        boolean save = accountService.save(account);
        if (save){
            System.out.println("OK");
        }else {
            System.out.println("Error");
        }

    }


    public Result testEsSearch() {
       return  null;
    }




    public Result deleteIndexBank() {

        DeleteIndexRequest request = new DeleteIndexRequest("mytest_user");
        try {
            client.indices().delete(request, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error("删除失败原因"+e);
            return Result.error("删除索引bank失败");
        }
        return Result.ok("删除索引bank成功");
    }


    @Test
    public Result deleteEsDataByParam(Integer account_number) {
        DeleteByQueryRequest request = new DeleteByQueryRequest("bank");
        request.setQuery(QueryBuilders.matchQuery("account_number",account_number));
        try {
            client.deleteByQuery(request,EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error("删除失败原因"+e);
            return Result.error("从ES中删除数据失败");
        }
        return Result.ok("删除成功");
    }


    public Result updateESDataByParam(Account account) {

        UpdateRequest request = new UpdateRequest("bank",account.getAccountNumber().toString());
        String s = JSON.toJSONString(account);
        request.doc(s, XContentType.JSON);
        try {
            client.update(request,EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error("更新失败原因"+e);
            return Result.error("从ES中更新数据失败");
        }
        return Result.ok("更新成功");
    }


    public Result addAccountToEs(Account account) {
        IndexRequest request = new IndexRequest("bank");
        request.id(account.getAccountNumber().toString());
        String s = JSON.toJSONString(account);
        request.source(s,XContentType.JSON);
        try {
            client.index(request,EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error("添加失败原因"+e);
            return Result.error("从ES中添加数据失败");
        }
        return Result.ok("添加成功");
    }





}
