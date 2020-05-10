package cn.dwx.shiro510.service.impl;

import cn.dwx.shiro510.mapper.UserMapper;
import cn.dwx.shiro510.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int reg(Map map) {
        return userMapper.add(map);
    }
}
