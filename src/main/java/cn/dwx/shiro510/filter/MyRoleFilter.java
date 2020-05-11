package cn.dwx.shiro510.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyRoleFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response); //获得当前用户
        String[] roles = (String[]) mappedValue; //把参数mappedValue转成数组
        if (roles == null || roles.length == 0) {
            return true;
        }
        for (String role : roles) {  //循环判断用户是否有角色, 当一有角色的时候, 就返回true, 相当于把and变成了or
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
