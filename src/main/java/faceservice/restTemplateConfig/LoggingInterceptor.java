package faceservice.restTemplateConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response=execution.execute(request, body);
        traceResponse(response);
        return execution.execute(request, body);
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        String bodyMessage=body.length>200?"图片文件不予显示": new String(body, "UTF-8");
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
        response.getStatusCode();
        String bodyMessage=null;
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
                if(inputStringBuilder.length()<300){
                    continue;
                }else
                    break;
            }
            if(inputStringBuilder.length()>300){
                bodyMessage="文件过大不予显示";
            }else
                bodyMessage=inputStringBuilder.toString();
        }
        log.info("============================response begin==========================================");
        log.info("Status code  : {}", response.getStatusCode());
        log.info("Status text  : {}", response.getStatusText());
        log.info("Headers      : {}", response.getHeaders());
        log.info("Response body: {}", bodyMessage);//WARNING: comment out in production to improve performance
        log.info("=======================response end=================================================");
    }

}
