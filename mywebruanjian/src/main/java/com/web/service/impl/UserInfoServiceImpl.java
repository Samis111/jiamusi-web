package com.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.domain.UserInfo;
import com.web.service.UserInfoService;
import com.web.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




