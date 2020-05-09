package com.ichzh.physicalFitness.conf;

import lombok.Data;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ichzh.physicalFitness.util.JarPathUtil;

import java.io.File;

@Configuration
@ConfigurationProperties(prefix = "https")
@Data
public class HttpsConfig {

    // https 端口
    private Integer port;

    // 是否开启https
    private boolean enable;

    // 证书路径
    private String keyStoreSource;

    // 证书环境信息
    private Ssl ssl;

    // 配置https
    public Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            connector.setScheme("https");
            connector.setSecure(enable);
            connector.setPort(port);
            protocol.setSSLEnabled(enable);
            //this.getClass().getClassLoader().getResource(keyStore).getPath()
            //获取项目所在的绝对路径+/keyStore
            protocol.setKeystoreFile(JarPathUtil.getProjectPath() + File.separator + keyStoreSource);
            protocol.setKeystorePass(ssl.getKeyStorePassword());
            protocol.setKeyAlias(ssl.getKeyAlias());
            protocol.setKeyPass(ssl.getKeyStorePassword());
            return connector;
        }
        catch (Exception ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }


}
