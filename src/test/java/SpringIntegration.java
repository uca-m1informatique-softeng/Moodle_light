
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import Model.Main;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@CucumberContextConfiguration
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    static ResponseResults latestResponse = null;
    protected HttpResponse latestHttpResponse;
    private final RestTemplate restTemplate = new RestTemplate();

    int timeout = 5;
    RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout * 1000)
            .setConnectionRequestTimeout(timeout * 1000)
            .setSocketTimeout(timeout * 1000).build();
    CloseableHttpClient  httpClient =
            HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    boolean executeGet(String url, String jwt)  {
        HttpGet request = new HttpGet(url);

        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        try{
            latestHttpResponse = httpClient.execute(request);
        }catch(Throwable t){

            System.out.println(t.getLocalizedMessage());
            return false;
        }
        return true;
    }

    Object executeGetReturnObject(String url, String jwt) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + jwt);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Object> response = this.restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    List executeGetReturnListObject(String url, String jwt) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + jwt);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<List> response = this.restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    boolean executePostObj(String url, String obj) throws UnsupportedEncodingException {
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
    boolean executePostObj(String url, String obj, String jwt) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(obj));
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
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


    boolean executePut(String url, String jwt) throws IOException {
        HttpPut request = new HttpPut(url);
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


    boolean executeDelete(String url, String jwt) throws IOException {
        HttpDelete request = new HttpDelete(url);
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        try{
            latestHttpResponse = httpClient.execute(request);
        }catch(Throwable t){
            System.out.println(t.getLocalizedMessage());
            return false;
        }
        return true;
    }

}