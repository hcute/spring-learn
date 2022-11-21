package link.pinkie.hooray.service;

import link.pinkie.spring.BeanPostProcessor;
import link.pinkie.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName HoorayBeanPostProcessor
 * @Description: 自定义BeanPostProcessor
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
@Component
public class HoorayBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("\u001B[31m" + "beanPostProcessor:" + beanName+"\u001B[0m");
        if (beanName.equals("personService")) {
            Object proxyInstance = Proxy.newProxyInstance(HoorayBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("personService proxy do....");
                    return method.invoke(bean,args);
                }
            });
            // 返回代理对象
            return proxyInstance;
        }
        return bean;
    }
}
