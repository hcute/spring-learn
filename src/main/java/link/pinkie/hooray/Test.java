package link.pinkie.hooray;

import link.pinkie.hooray.service.IPersonService;
import link.pinkie.hooray.service.PersonService;
import link.pinkie.hooray.service.UserService;
import link.pinkie.spring.HoorayApplicationContext;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
public class Test {

    public static void main(String[] args) {
        HoorayApplicationContext hoorayApplicationContext = new HoorayApplicationContext(AppConfig.class);

//        UserService userService = (UserService) hoorayApplicationContext.getBean("userService");
//        userService.test();

        System.out.println("==============================");
        IPersonService personService = (IPersonService) hoorayApplicationContext.getBean("personService");
        personService.test();
    }
}
