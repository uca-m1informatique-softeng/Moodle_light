package tests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import Model.Main;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.test.context.SpringBootTest;
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
            .setSocketTimeout(timeout * 1000)
            .setConnectTimeout(1)
            .build();
    CloseableHttpClient  httpClient =
            HttpClientBuilder.create()
                    .setDefaultRequestConfig(config)
                    .build();
    //pas utiliser pour le moment
    Object executeGetReturnObject(String url, String jwt) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + jwt);
        System.out.println("========DEBUG ======\n"+headers.toString());
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Object> response = this.restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
        System.out.println("========DEBUG ======\n"+response.toString());
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    /**
     * exectute HttpPut avec obj jwt qui peuve etre null si non néssésaire
     * @param url
     * @param obj si null obj default "{}"
     * @param jwt si null pas de jwt
     * @return boolean true : request a reussi
     * @throws UnsupportedEncodingException
     */
    boolean executePut(String url, String jwt, String obj) throws IOException {
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }        if(obj != null){
            request.setEntity(new StringEntity(obj));
        }else{
            request.setEntity(new StringEntity("{}"));
        }
        latestHttpResponse = httpClient.execute(request);
        System.out.println("ENTITY " +obj + "   " + latestHttpResponse.getStatusLine());
        EntityUtils.consumeQuietly(latestHttpResponse.getEntity());
        return false;
    }

    /**
     * exectute HttpPost avec obj jwt qui peuve etre null si non néssésaire
     * @param url
     * @param obj si null obj default "{}"
     * @param jwt si null pas de jwt
     * @return boolean true : request a reussi
     * @throws UnsupportedEncodingException
     */
    boolean executePost(String url,  String jwt, String obj) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        if(obj != null){
            request.setEntity(new StringEntity(obj));
        }else{
            request.setEntity(new StringEntity("{}"));
        }
        latestHttpResponse = httpClient.execute(request);
        EntityUtils.consumeQuietly(latestHttpResponse.getEntity());
        return false;
    }

    /**
     * exectute HttpDelete avec jwt qui peut etre null si non néssésaire
     * @param url
     * @param jwt si null pas de jwt
     * @return boolean true : request a reussi
     * @throws IOException
     */
    boolean executeDelete(String url, String jwt) throws IOException {
        HttpDelete request = new HttpDelete(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
        return false;
    }

    /**
     * exectute HttpGet avec jwt qui peut etre null si non néssésaire
     * @param url
     * @param jwt si null pas de jwt
     * @return boolean true : request a reussi
     */
    boolean executeGet(String url, String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
        return false;
    }

    /////////////////////         methode pour executation post          ///////////////////////////

    /**
     * creer le body dans HttpEntityEnclosingRequestBase
     * si null le body est "{}"
     *  ! HttpEntityEnclosingRequestBase ne peut etre appeler sur HttpPost et HttpPut qui ont un corps
     * @param request
     * @param obj
     * @throws UnsupportedEncodingException
     */
    private void addbody(HttpEntityEnclosingRequestBase request,String obj)throws UnsupportedEncodingException {

    }

    /**
     * rajoute le header json type et l'authontification jwt (Teacher)
     * HttpRequestBase etant la super class de HttpGet,HttpDelete, HttpPost,HttpPut
     * @param request
     * @param jwt
     */
    private void add(HttpRequestBase request,String jwt){

    }
}