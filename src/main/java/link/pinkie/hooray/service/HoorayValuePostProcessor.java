package link.pinkie.hooray.service;

import link.pinkie.spring.BeanPostProcessor;
import link.pinkie.spring.Component;
import link.pinkie.spring.ComponentScan;

import java.lang.reflect.Field;

/**
 * @ClassName HoorayValuePostProcessor
 * @Description: 应用BeanPostProcessor
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
@Component
public class HoorayValuePostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(HoorayValue.class)) {
                HoorayValue hoorayValue = field.getAnnotation(HoorayValue.class);
                String value = hoorayValue.value();
                try {
                    field.set(bean,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
