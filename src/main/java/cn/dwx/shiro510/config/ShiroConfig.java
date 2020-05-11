package cn.dwx.shiro510.config;

import cn.dwx.shiro510.filter.MyPermFilter;
import cn.dwx.shiro510.filter.MyRoleFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(2000);
        return redisManager;
    }
    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }
    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager redisSessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }


    @Bean(name = "shiroFilter")
    //配置拦截的规则
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /*shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.html");*/

        shiroFilterFactoryBean.setLoginUrl("/unlogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");


        // 自定义拦截器,roles[经理2,vip] perms[查询信息,add]的关系改成或者
        Map<String, Filter> map = new HashMap<>();
        map.put("roles", new MyRoleFilter());
        map.put("perms", new MyPermFilter());
        shiroFilterFactoryBean.setFilters(map);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:(必须登录)所有url都必须认证通过才可以访问; anon:所有url都可以匿名访问-->
        //roles 需要对应的角色 perms 需要对应的权限
        //logout 注销 user remember me权限
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/static/js/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/aa", "user");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/", "user");
        filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
        filterChainDefinitionMap.put("/my/**", "roles[经理]");
        filterChainDefinitionMap.put("/bb/**", "roles[admin]");

        filterChainDefinitionMap.put("/admin/**", "roles[经理2,vip]");
        filterChainDefinitionMap.put("/abc/**", "perms[查询信息,add]");

        filterChainDefinitionMap.put("/user/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/*", "anon");
        //filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

//    @Bean
//    public DefaultWebSecurityManager  securityManager(DefaultWebSessionManager redisSessionManager) {
//        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
//        //设置realm realm是提供验证数据（比如用户信息，角色权限信息）
//        defaultSecurityManager.setRealm(customRealm());
//        defaultSecurityManager.setSessionManager(redisSessionManager);
////        defaultSecurityManager.setCacheManager(redisCacheManager);
//        // 使用记住我
//        defaultSecurityManager.setRememberMeManager(rememberMeManager());
//        return defaultSecurityManager;
//    }

    @Bean
    public DefaultWebSecurityManager  securityManager(DefaultWebSessionManager redisSessionManager, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        //设置realm realm是提供验证数据（比如用户信息，角色权限信息）
        defaultSecurityManager.setRealm(customRealm());
        defaultSecurityManager.setSessionManager(redisSessionManager);
        defaultSecurityManager.setCacheManager(redisCacheManager);
        // 使用记住我
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultSecurityManager;
    }

//    @Bean
//    public DefaultWebSecurityManager securityManager() {
//        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
//        //设置realm realm是提供验证数据（比如用户信息，角色权限信息）
//        defaultSecurityManager.setRealm(customRealm());
////        defaultSecurityManager.setSessionManager(redisSessionManager);
////        defaultSecurityManager.setCacheManager(redisCacheManager);
//        // 使用记住我
//        defaultSecurityManager.setRememberMeManager(rememberMeManager());
//        return defaultSecurityManager;
//    }

    @Bean
    public MyRealm customRealm() {
        //自定义的realm
        MyRealm customRealm = new MyRealm();
        // 告诉realm,使用credentialsMatcher加密算法类来验证密文
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        //设置缓存开启
        customRealm.setCachingEnabled(true);
        return customRealm;
    }


    //凭证管理器
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //记住我cookie生效时间,单位是秒
        simpleCookie.setMaxAge(600);

        return simpleCookie;
    }

    /*
     * cookie管理器;
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
}
