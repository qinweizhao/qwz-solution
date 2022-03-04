package com.qinweizhao.account.service.impl;

import com.qinweizhao.account.entity.Account;
import com.qinweizhao.account.mapper.AccountMapper;
import com.qinweizhao.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Monday_1201
 * @since 2021-03-31
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
