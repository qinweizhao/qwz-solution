package com.qinweizhao.account.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qinweizhao.account.config.EsConfig;
import com.qinweizhao.account.constant.EsConstant;
import com.qinweizhao.account.entity.Account;
import com.qinweizhao.account.exception.GlobalException;
import com.qinweizhao.account.service.AccountService;
import com.qinweizhao.account.service.EsService;
import com.qinweizhao.account.utils.Result;
import com.qinweizhao.account.vo.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/23 14:26
 * </p>
 */
@Slf4j
@Service
public class EsServiceImpl implements EsService {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    AccountService accountService;


    @Override
    public Result getAllEsDataToDb() {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsConstant.INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(2000);
        searchRequest.source(searchSourceBuilder);
        Result r = null;
        try {
            SearchResponse response = client.search(searchRequest, EsConfig.COMMON_OPTIONS);
            r = buildSearchResult(response);
        } catch (IOException e) {
            return Result.error("从es中获取所有数据错误");
        }


        return r;
    }
    private Result buildSearchResult(SearchResponse response) {
        SearchHit[] hits = response.getHits().getHits();
        long l1 = 0;
        List<Account> listAccount = new ArrayList<>();
        for (SearchHit hit : hits){
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            account.setId(Integer.valueOf(hit.getId()));
            listAccount.add(account);
        }
        long l = System.currentTimeMillis();
        accountService.saveBatch(listAccount);
        long e = System.currentTimeMillis();
        l1 = e - l;
        return Result.ok("成功","消耗时间为"+l1);
    }


    @Override
    public Result saveToEs() {
        List<Account> accounts = accountService.list(null);

        BulkRequest request = new BulkRequest();
        for (Account account : accounts){
            String s = JSON.toJSONString(account);
            request.add(new IndexRequest(EsConstant.INDEX).source(s,XContentType.JSON));
        }
        BulkResponse bulk;
        try {
            bulk = client.bulk(request, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error("批量添加失败原因"+e);
            return Result.error("从ES中添加数据失败");
        }
        boolean b = bulk.hasFailures();
        if (b){
            System.out.println(bulk.buildFailureMessage());
        }
        List<String> ids = Arrays.stream(bulk.getItems()).map(item ->
                item.getId()
        ).collect(Collectors.toList());
        return Result.ok("添加成功").setObj(ids);
    }

    @Override
    public Result searchAccount(SearchParam param) {
        //1.创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices(EsConstant.INDEX);
        //指定DSL，检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1.1 构建检索条件
        //        sourceBuilder.query();
        //        sourceBuilder.from();
        //        sourceBuilder.size();
        //        sourceBuilder.aggregation()
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(param.getAddress())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("address",param.getAddress()));
        }
        if (!StringUtils.isEmpty(param.getGender())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("gender",param.getGender()));
        }
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        String[] s = param.getAge().split("-");
        if (s.length == 2) {
            //区间
            rangeQuery.gte(s[0]).lte(s[1]);
        } else if (s.length == 1) {
            if (param.getAge().startsWith("_")) {
                rangeQuery.lte(s[0]);
            }
            if (param.getAge().endsWith("_")) {
                rangeQuery.gte(s[0]);
            }

        }
        sourceBuilder.query(boolQueryBuilder);


        //排序
        if(!StringUtils.isEmpty(param.getSort())){
            String sort = param.getSort();
            SortOrder sortOrder = sort.equalsIgnoreCase("asc")? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort("account_number",sortOrder);
        }

        //分页
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PAGESIZE);
        sourceBuilder.size(EsConstant.PAGESIZE);


        //聚合
//        TermsAggregationBuilder age_terms = AggregationBuilders.terms("age_terms");
//        age_terms.field("age");
//        TermsAggregationBuilder gender_terms = AggregationBuilders.terms("gender_terms");
//        gender_terms.field("gendeResult.keyword");
//        AvgAggregationBuilder balance_avg = AggregationBuilders.avg("balance_avg");
//        balance_avg.field("blanace");
//        gender_terms.subAggregation(balance_avg);
//
//        sourceBuilder.aggregation(age_terms);
//        sourceBuilder.aggregation(gender_terms);
//        String sa = sourceBuilder.toString();
//        System.out.println("构建的DSL"+sa);
        searchRequest.source(sourceBuilder);
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            return Result.error("出现错误");
        }
        return Result.ok("成功",search.toString());
    }

    @Override
    public Result dbToEs(String account) {
        JSONObject jsonObject = JSON.parseObject(account);
        if (jsonObject!=null){
            IndexRequest indexRequest = new IndexRequest(EsConstant.INDEX);
            indexRequest.source(account,XContentType.JSON);
            IndexResponse index;
            try {
                 index = client.index(indexRequest, EsConfig.COMMON_OPTIONS);

            } catch (IOException e) {
                throw new GlobalException("保存失败");
            }
        }


        return Result.ok("保存成功");
    }


}
