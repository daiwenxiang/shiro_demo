package cn.dwx.shiro510.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyRoleFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获得当前用户
        Subject subject = getSubject(request, response);
        //把参数mappedValue转成数组
        String[] roles = (String[]) mappedValue;
        if (roles == null || roles.length == 0) {
            return true;
        }

        //循环判断当前用户是否有权限
        //把默认的and关系 变成了 或者 的关系
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }

        return false;
    }
}
