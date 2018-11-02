package faceservice.service;



import faceservice.RestTemplateConfig;
import net.sf.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Random;


@Service
public class HttpService {
    private RestTemplate restTemplate;


    private static  String BaseUrl="http://192.168.2.141:8080/";

    public HttpService(RestTemplateConfig restTemplate) {
        this.restTemplate = restTemplate.tokenRetrieveRestTemplate();
    }

    public void  sendForm(String url,Map<String, Object> map) {
        RestTemplate restTemplate=new RestTemplate();

        // 通过 HttpHeaders 设置Form方式提交
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
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
    public ResponseEntity sendPic(MultipartFile file)throws IOException {
        //RestTemplate restTemplate=new RestTemplate();
        File image=new File(ResourceUtils.getURL("classpath:Face/face.jpg").getPath());
        file.transferTo(image);
        FileSystemResource face=new FileSystemResource(image);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("image",face);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BaseUrl+"api/face/v1/add", HttpMethod.POST, httpEntity, String.class);
        return responseEntity;
    }
    public ResponseEntity sendJson(String url,Map<String,String> map){
        //RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();		//定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> responseEntity = null;
        responseEntity = restTemplate.exchange(BaseUrl+url, HttpMethod.POST,httpEntity,String.class);
        return responseEntity;

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
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        System.out.println(data.get("version"));
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
    public byte[] getPic()throws IOException{
        FileSystemResource resource = new FileSystemResource(ResourceUtils.getFile("classpath:face1.jpg"));
        byte[] pic=new byte[4096];
        InputStream inputStream = new FileInputStream(ResourceUtils.getFile("classpath:face1.jpg"));
        inputStream.read(pic);
        inputStream.close();
        return pic;
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

}