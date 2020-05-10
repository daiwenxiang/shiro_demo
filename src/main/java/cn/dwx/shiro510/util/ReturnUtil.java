package cn.dwx.shiro510.util;

import java.util.HashMap;
import java.util.Map;

public class ReturnUtil {

    public static Map successReturn() {
        Map map = new HashMap();
        map.put("code", "0");
        map.put("status", "成功");
        return map;
    }

    public static Map successReturn(Object data) {
        Map map = new HashMap();
        map.put("code", "0");
        map.put("data", data);
        return map;
    }

    public static Map failReturn() {
        Map map = new HashMap();
        map.put("code", "-1");
        return map;
    }

    public static Map failReturn(String code) {
        Map map = new HashMap();
        map.put("code", code);
        return map;
    }
}
