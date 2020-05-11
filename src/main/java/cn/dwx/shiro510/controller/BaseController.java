package cn.dwx.shiro510.controller;

import cn.dwx.shiro510.util.Constants;
import cn.dwx.shiro510.util.ReturnUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public class BaseController {

    @GetMapping("/unlogin")
    public Map unlogin(){
        return ReturnUtil.failReturn(Constants.UNLOGIN_ERROR);
    }

    @GetMapping("/unauthorized")
    public Map unauthorized(){
        return ReturnUtil.failReturn(Constants.UNAUTHORIZED_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Map authorizationException() {
        return ReturnUtil.failReturn(Constants.UNAUTHORIZED_ERROR);
    }
}
