package faceservice.service;



import net.sf.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class MyService {

    private static  String BaseUrl="http://192.168.2.141:8080/";
    public void  sendForm(String url,Map<String, Object> map) {
        RestTemplate restTemplate=new RestTemplate();

        // 通过 HttpHeaders 设置Form方式提交
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> responseEntity = null;

        String body=new String();
        try {
            responseEntity = restTemplate.postForEntity(BaseUrl+url,httpEntity,String.class);
            body = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            body=e.getResponseBodyAsString();
        }
        getReturn(body);
    }
    public void sendJson(String url,Map<String,String> map){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();		//定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> responseEntity = null;
        String body=new String();
        try {
            responseEntity = restTemplate.postForEntity(BaseUrl+url,httpEntity,String.class);
            body = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            body=e.getResponseBodyAsString();
        }
        getReturn(body);
    }
    public void get(String url,Map<String,Object> map){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(BaseUrl+url, String.class,map);
        String body = responseEntity.getBody();
        getReturn(body);

    }
    public static void getReturn(String message){
        int level=0;
        JSONObject json=JSONObject.fromObject(message);
        String str=json.toString();
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int index=0;index<str.length();index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = str.charAt(index);
            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        System.out.println(jsonForMatStr);
    }
    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");        }
            return levelStr.toString();
    }
}
