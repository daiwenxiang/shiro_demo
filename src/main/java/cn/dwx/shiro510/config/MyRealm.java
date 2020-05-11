package cn.dwx.shiro510.config;

import cn.dwx.shiro510.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;

    @Override
//    提供当前用户的权限信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //角色set
        Set<String> roleSet = new HashSet<>();
        //权限set
        Set<String> permissionSet = new HashSet<>();
        //获得当前用户的角色和权限信息, 填充到以上的set中
        Map userMap = (Map) principalCollection.getPrimaryPrincipal();
        //获得用户id
        int uid = (int) userMap.get("uid");
        //根据uid 获得角色集合
        List<Map> roles = userMapper.getRoleByUid(uid);
        for (Map role : roles) {
            //获得角色id
            int rid = (int) role.get("id");
            //获得角色名
            String roleStr = (String) role.get("role");
            //把角色名填充到roleSet
            roleSet.add(roleStr);
            //根据角色id查询权限集合
            List<Map> permissions = userMapper.getPermissionByRid(rid);
            for (Map permission : permissions) {
                //获得权限名称, 放入permissionSet中
                permissionSet.add((String) permission.get("permission"));
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }

    @Override
//    登陆验证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //方法需要传token参数
        //把token参数 转成usernameandpasswordtoken类型
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();  //获得username

        Map user = userMapper.getByName(username);
        if (user == null) {
            throw new UnknownAccountException(); //没有这个账户 抛一个异常
        }

        //获得盐值
        String salt = (String) user.get("salt");
        ByteSource bytes = ByteSource.Util.bytes(salt);

        //凭证信息    四个参数: 用户, 密码, 颜值, 当前realm的名称 MyRealm
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.get("password"), bytes, getName());
        return info;
    }
}
