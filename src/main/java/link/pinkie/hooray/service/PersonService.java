package link.pinkie.hooray.service;

import link.pinkie.spring.BeanNameAware;
import link.pinkie.spring.Component;

/**
 * @ClassName PersonService
 * @Description: TODO
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
@Component
public class PersonService implements IPersonService, BeanNameAware {

    @HoorayValue("hooray")
    private String name;

    private String beanName;

    @Override
    public void test() {
        System.out.println("PersonService do .....");
        System.out.println(name);
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
