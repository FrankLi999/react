package mycamel.spring.boot.camel.support;

import feign.hc5.ApacheHttp5Client;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

public class HttpClientUtils {
    public static ApacheHttp5Client httpClient(int maxConnection) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnection);
        cm.setDefaultMaxPerRoute(maxConnection);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build();
        return new ApacheHttp5Client(client);
    }

    public static ApacheHttp5Client httpClient(int maxConnection, long connectionTineout, long readTimeout) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnection);
        cm.setDefaultMaxPerRoute(maxConnection);

        RequestConfig connConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionTineout, TimeUnit.MILLISECONDS)
                .setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(connConfig)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return new ApacheHttp5Client(client);
    }
}
