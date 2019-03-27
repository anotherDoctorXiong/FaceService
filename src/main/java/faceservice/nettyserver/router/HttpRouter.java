package faceservice.nettyserver.router;

import faceservice.model.Response;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpRouter extends ClassLoader {
    private Map<HttpLabel, Action<Response>> httpRouterAction = new HashMap<>();
    private Map<String, Object> controllerBeans = new HashMap<>();
    public void addRouter(String controller){
        try {
            Class<?> cls=loadClass(controller);
            Method[] methods=cls.getDeclaredMethods();
            for(Method method : methods ){
                Annotation[] annotations=method.getAnnotations();
                for(Annotation annotation : annotations){
                    if(annotation.annotationType()==RequestMapping.class){
                        RequestMapping requestMapping = (RequestMapping) annotation;
                        String uri = requestMapping.uri();
                        String httpMethod = requestMapping.method().toUpperCase();
                        if (!controllerBeans.containsKey(cls.getName())) {
                            controllerBeans.put(cls.getName(), cls.newInstance());
                        }
                        Action action = new Action(controllerBeans.get(cls.getName()), method);
                        //如果需要FullHttpRequest，就注入FullHttpRequest对象
                        Class[] params = method.getParameterTypes();
                        if (params.length == 1 && params[0] == FullHttpRequest.class) {
                            System.out.println("set ture");
                            action.setInjectionFullhttprequest(true);
                        }
                        // 保存映射关系
                        httpRouterAction.put(new HttpLabel(uri, new HttpMethod(httpMethod)), action);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public Action getRoute(HttpLabel httpLabel) {
        return httpRouterAction.get(httpLabel);
    }
}
