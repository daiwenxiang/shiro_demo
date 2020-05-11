package cn.dwx.shiro510.controller;

import cn.dwx.shiro510.service.UserService;
import cn.dwx.shiro510.util.Constants;
import cn.dwx.shiro510.util.ReturnUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;

@RestController
public class UserController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private HashedCredentialsMatcher credentialsMatcher;


    @GetMapping("/login")
    public Map login(String name, String password, Boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();  //通过shiro获得当前用户
        UsernamePasswordToken token = new UsernamePasswordToken(name, password); //把用户名和密码做成taken
        token.setRememberMe(rememberMe);
        try {
            subject.login(token);  //调用subject的登陆方法
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return ReturnUtil.failReturn(Constants.USERNAME_ERROR);
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return ReturnUtil.failReturn(Constants.USERPWD_ERROR);
        }
        return ReturnUtil.successReturn();
    }

    @GetMapping("/reg")
    public Map reg(@RequestParam Map map) {
        String password = (String) map.get("password");
        Random random = new Random();
        int num = random.nextInt(9000) + 1000;
        //密码加密    按照shiro的规则进行加密
        //四个参数  凭证管理器 密码 盐值 加密迭代次数
        SimpleHash md5 = new SimpleHash(credentialsMatcher.getHashAlgorithmName(), password, num + "", credentialsMatcher.getHashIterations());
        //把加密后的密码和盐值存入map
        map.put("salt", num + "");
        map.put("password", md5.toString());
        int reg = userService.reg(map);
        if (reg == 1) {
            return ReturnUtil.successReturn();
        } else {
            return ReturnUtil.failReturn();
        }
    }

    @RequiresRoles(value = {"admin", "vip"}, logical = Logical.OR)
    @GetMapping("/test66")
    public Map test66(@RequestParam Map map) {
        System.out.println("test66");
        return null;
    }
}
