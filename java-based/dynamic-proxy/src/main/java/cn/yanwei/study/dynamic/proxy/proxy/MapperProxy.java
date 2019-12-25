package cn.yanwei.study.dynamic.proxy.proxy;

import cn.yanwei.study.dynamic.proxy.model.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理实现类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/19 15:31
 */
public class MapperProxy implements InvocationHandler {

    /**
     * 利用Proxy.newProxyInstance 获取代理类
     *
     * @param clz 目标接口
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> clz) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //判断method是否是Object类中定义的方法
        //如 hashCode()、toString()、equals()
        //如果是则将target 指向 this
        //如果直接调用 method.invoke 会报空指针错误
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable ignored) {
            }
        }
        // 投鞭断流
        // mybatis mapper底层实现原理
        return findUserById((Integer) args[0]);
    }

    private User findUserById(Integer id) {
        return new User(id, "zhangsan", 18);
    }
}
