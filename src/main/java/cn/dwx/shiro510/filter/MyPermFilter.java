package cn.dwx.shiro510.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyPermFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获得当前用户
        Subject subject = getSubject(request, response);
        //把参数mappedValue转成数组
        String[] perms = (String[]) mappedValue;
        if (perms == null || perms.length == 0) {
            return true;
        }

        //循环判断当前用户是否有权限
        for (String perm : perms) {
            if (subject.isPermitted(perm)) {
                return true;
            }
        }

        return false;
    }
}
