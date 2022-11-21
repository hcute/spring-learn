package link.pinkie.spring;

import com.sun.istack.internal.Nullable;

/**
 * @ClassName BeanPostProcessor
 * @Description: TODO
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
public interface BeanPostProcessor {
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}
