package com.payment.wxpay.config;

import com.setting.SettingManager;
import com.hanye.core.wxpay.IWXPayDomain;
import com.hanye.core.wxpay.SldWXPayDomainImpl;
import com.hanye.core.wxpay.WXPayConfig;

import java.io.InputStream;

/**
 * TODO
 *
 * @author by hugoLee 2019/6/18 15:31
 */
public class MobileWXJSPayConfig extends WXPayConfig {
    private byte[] certData;
    private static MobileWXJSPayConfig INSTANCE;
    public static MobileWXJSPayConfig getInstance(){
        if (INSTANCE == null) {
            synchronized (MobileWXJSPayConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MobileWXJSPayConfig();
                }
            }
        } return INSTANCE;
    }

    public MobileWXJSPayConfig(){
        //获取证书(当使用需要证书的api时在此初始化)
//        File file = new ClassPathResource("apiclient_cert.p12").getFile();
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    public String getAppID() {
        return SettingManager.getSetting("wxpay_miniapp_appid");
    }

    public String getMchID() {
        return SettingManager.getSetting("wxpay_miniapp_merchantid");
    }

    public String getKey() {
        return SettingManager.getSetting("wxpay_miniapp_apikey");
    }

    public InputStream getCertStream() {
//        ByteArrayInputStream certBis;
//        certBis = new ByteArrayInputStream(this.certData);
//        return certBis;
        return null;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     *
     * @return
     */
    @Override
    protected IWXPayDomain getWXPayDomain() {
        return SldWXPayDomainImpl.instance();
    }

}
