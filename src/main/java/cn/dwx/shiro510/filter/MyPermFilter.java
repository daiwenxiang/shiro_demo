package cn.dwx.shiro510.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyPermFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response); //获得当前用户
        String[] perms = (String[]) mappedValue; //把参数mappedValue转成数组
        if (perms == null || perms.length == 0) {
            return true;
        }
        for (String perm : perms) {  //循环判断用户是否有权限, 当一有权限的时候, 就返回true, 相当于把and变成了or
            if (subject.isPermitted(perm)) {
                return true;
            }
        }
        return false;
    }
}
