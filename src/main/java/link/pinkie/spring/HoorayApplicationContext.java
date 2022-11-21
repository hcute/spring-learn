package link.pinkie.spring;

import link.pinkie.hooray.service.UserService;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HoorayApplicationContext
 * @Description: spring 上下文
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
public class HoorayApplicationContext {

    private Class configClass;
    // bean的定义缓存
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池
    private Map<String,Object> singletonObjects = new HashMap<>();
    // BeanPostProcessor 缓存
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public HoorayApplicationContext(Class clazz) {
        this.configClass = clazz;
        // 扫描需要加载的bean
        scan(clazz);

        // 创建单例bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }
    }


    /**
     * 创建bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName,BeanDefinition beanDefinition) {

        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            // 实例化对象
            instance = clazz.getConstructor().newInstance();

            // 依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    // TODO 先根据类型去找，再根据名字去找。这里直接根据名字找
                    field.set(instance,getBean(field.getName()));
                }
            }

            // aware的实现
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }


            // BeanPostProcessor 执行
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 这里扩展AOP来实现
                instance = beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // BeanPostProcessor 执行
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 这里扩展AOP来实现
                instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }


    /**
     * 扫描初始化上下文
     * @param clazz
     */
    private void scan(Class clazz) {
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) clazz.getAnnotation(ComponentScan.class);
            // 扫描的包名
            String value = componentScan.value();
            // 获取classpath的路径
            String path = value.replace(".", "/");
            ClassLoader classLoader = HoorayApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            // 遍历文件夹所有的文件
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String absolutePath = f.getAbsolutePath();
                    // 加载该文件的类，判断是否有@Component注解
                    String packagePath = absolutePath.substring(absolutePath.indexOf("link"),
                            absolutePath.indexOf(".class")).replace("/",".");
                    try {
                        Class<?> aClass = classLoader.loadClass(packagePath);
                        // 当前类是要扫描为bean
                        if (aClass.isAnnotationPresent(Component.class)) {
                            // 处理BeanPostProcessor
                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor)
                                        aClass.getConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                            } else {
                                Component component = aClass.getAnnotation(Component.class);
                                // 判断当前的对象是单例的还是原型的，是单例的bean再进行创建。
                                // 这里spring添加了一个缓存，因为如果不加缓存那么再getBean的时候还需要去判断
                                // 加载bean的定义到beanDefinitionMap
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(aClass);
                                // 处理类的单例和原型
                                if (aClass.isAnnotationPresent(Scope.class)) {
                                    Scope scope = aClass.getAnnotation(Scope.class);
                                    String value1 = scope.value();
                                    beanDefinition.setScope(value1);
                                } else {
                                    beanDefinition.setScope("singleton");
                                }
                                // TODO 处理懒加载

                                // 放在缓存
                                String beanName = component.value().equals("")?
                                        Introspector.decapitalize(aClass.getSimpleName()):component.value();
                                beanDefinitionMap.put(beanName,beanDefinition);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new IllegalArgumentException("beanName is not right");
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        // 单例bean
        if (beanDefinition.getScope().equals("singleton")){
            Object singletonBean = singletonObjects.get(beanName);
            // 这里导致依赖注入为null，因为单例池
            if (null == singletonBean) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName,singletonBean);
            }
            return singletonBean;
        } else { // 原型bean
            return createBean(beanName, beanDefinition);
        }
    }
}
