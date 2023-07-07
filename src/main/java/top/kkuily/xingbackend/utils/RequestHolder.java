package top.kkuily.xingbackend.utils;


import top.kkuily.xingbackend.model.dto.RequestInfo;

/**
 * @author 小K
 * @description 请求上下文
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
