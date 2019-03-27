package faceservice.nettyserver.router;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Data
public class Action<T> {
    private Object object;

    private Method method;

    private boolean injectionFullhttprequest;

    public Action(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public T call(Object... args) {
        try {
            return (T) method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
