package faceservice.restTemplateConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response=execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        String bodyMessage=body.length>200?"图片文件不予显示": new String(body, "UTF-8");
        bodyMessage=request.getHeaders().getContentType().includes(MediaType.APPLICATION_FORM_URLENCODED)
                ?URLDecoder.decode(bodyMessage,"UTF-8"):bodyMessage;
        log.info("===========================request begin================================================");
        log.info("URI         : {}", request.getURI());
        log.info("Method      : {}", request.getMethod());
        log.info("Headers     : {}", request.getHeaders());
        log.info("Request body: {}", bodyMessage);
        log.info("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {

        //为什么在调用response.getstatuscode（）后调用response.getBody（）不抛出IOException，
        // 是因为getstatuscode在内部调用getinputstream，所以调用getBody时，errorstream不为空。
        //String bodyMessage=body.length>200?"图片文件不予显示": new String(body, "UTF-8");

        log.info("============================response begin==========================================");
        log.info("Status code  : {}", response.getStatusCode());
        log.info("Status text  : {}", response.getStatusText());
        log.info("Headers      : {}", response.getHeaders());
        if(response.getBody().available()<300){
            log.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));//WARNING: comment out in production to improve performance
        }else {
            log.info("Response body: 文件内容不予显示");
        }
         log.info("=======================response end=================================================");
    }

}
