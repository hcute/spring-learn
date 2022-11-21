package link.pinkie.hooray.service;

import link.pinkie.spring.Autowired;
import link.pinkie.spring.Component;
import link.pinkie.spring.InitializingBean;
import link.pinkie.spring.Scope;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
@Component
@Scope("singleton")
public class UserService implements InitializingBean {


    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("init");
    }
}
