package cn.dwx.shiro510.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void getPermissionByRid() {
        List<Map> permissionByRid = userMapper.getPermissionByRid(1);
        System.out.println("permissionByRid = " + permissionByRid);
    }
}