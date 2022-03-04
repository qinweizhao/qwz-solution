package com.qinweizhao.account.service;

import com.qinweizhao.account.utils.Result;
import com.qinweizhao.account.vo.SearchParam;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/23 14:26
 * </p>
 */
public interface EsService {

    Result getAllEsDataToDb();

    Result saveToEs();

    Result searchAccount(SearchParam param);

    Result dbToEs(String account);
}
