package cn.dwx.shiro510;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Shiro510ApplicationTests {

    @Resource
    private HashedCredentialsMatcher credentialsMatcher;

    @Test
    void contextLoads() {
        SimpleHash simpleHash = new SimpleHash("md5", "111", "qq", 2);
        System.out.println("simpleHash = " + simpleHash);  //dc904b5613ac05a048a631fbde6ac3b0
    }

    @Test
    void reg1() {
        SimpleHash simpleHash = new SimpleHash(credentialsMatcher.getHashAlgorithmName(),
                "111",
                "qq",
                credentialsMatcher.getHashIterations());
        System.out.println("simpleHash = " + simpleHash);
    }
}
