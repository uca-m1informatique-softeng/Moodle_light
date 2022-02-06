
import java.io.IOException;

import Model.Main;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.http.client.methods.HttpPost;


@CucumberContextConfiguration
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    static ResponseResults latestResponse = null;
    protected HttpResponse latestHttpResponse;
    int timeout = 5;
    RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout * 1000)
            .setConnectionRequestTimeout(timeout * 1000)
            .setSocketTimeout(timeout * 1000).build();
    CloseableHttpClient  httpClient =
            HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    void executeGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        latestHttpResponse = httpClient.execute(request);
    }


    boolean executePostObj(String url, String obj) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(obj));
        try{
            latestHttpResponse = httpClient.execute(request);
        }catch(Throwable t){
            System.out.println(t.getLocalizedMessage());
            return false;
        }
        System.out.println(latestHttpResponse);
        return true;
    }

    boolean executePost(String url, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity("{}"));
        try{
            latestHttpResponse = httpClient.execute(request);
        }catch(Throwable t){
            System.out.println(t.getLocalizedMessage());
            return false;
        }
        return true;
    }

}