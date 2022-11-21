package link.pinkie.spring;

/**
 * @ClassName BeanDefinition
 * @Description: bean的描述类
 * @Author hooray
 * @Date 2022/11/21
 * @Version V1.0
 **/
public class BeanDefinition {
    private Class type;
    private String scope;
    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
