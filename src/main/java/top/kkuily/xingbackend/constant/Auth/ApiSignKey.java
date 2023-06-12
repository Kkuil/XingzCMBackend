package top.kkuily.xingbackend.constant.Auth;

/**
 * @author 小K
 * @description 公私钥
 */
public class ApiSignKey {
    // 私钥
    public static final String PRIVATE_KEY = """
            MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAg9834kP7qobNgD0N
            aLbVl1RoN8P2PKAJrKO/6M5nSh/hdav1wlOae8a1mxRiOzKTpW7px1arwoCE+wwo
            OHUOaQIDAQABAkAmRgoFLLAFTTylpJKYusABPOlGjiSQce584cXYPtexpmX/vfc+
            q/JlC88VgEKMO931eOSLchFIvetjE3oQABtRAiEAyY+Lh4zYkgmWjtie7YL2f22U
            N4wfMpDLkV6MSmhbep0CIQCnfTPI0YhscfFufaqM3yADx8jnSAjh9ckVkWXV000D
            PQIgWhtT8ysh/ldO+VkZ43AyplDke9imccKNXtV3viPdEXkCIQCDgCKrVg/1jRSQ
            uquwRPL+5NoQ9Ja8ylwhyfGZi3gYxQIgR4vZIFl1bg6vrOZPn9cgFwoKyd77D7wZ
            r9Jnpei3NfU=
            """;

    // 公钥
    public static final String PUBLIC_KEY = """
            MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIPfN+JD+6qGzYA9DWi21ZdUaDfD9jyg
            Cayjv+jOZ0of4XWr9cJTmnvGtZsUYjsyk6Vu6cdWq8KAhPsMKDh1DmkCAwEAAQ==
            """;

    // 存储在redis中的nonce key
    public static final String NONCE_KEY_IN_CACHE = "api:sign:nonce";

    // 每次请求有效滑动时间窗口(单位：毫秒)
    public static final int LEGAL_TIME_PER_REQ = 300000;
}
