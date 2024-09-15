package sz.lab.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class LinkShortUtil {
    public String shortenUrl(String longUrl) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 构建TinyURL API请求
            String tinyUrlApi = "https://tinyurl.com/api-create.php?url=" + URLEncoder.encode(longUrl, StandardCharsets.UTF_8);
            HttpGet request = new HttpGet(tinyUrlApi);

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 从响应中获取短链接
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new RuntimeException("Failed to get a short URL from TinyURL. Status code: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            System.err.println("Error while shortening URL: " + e.getMessage());
            return null;
        }
    }
}

