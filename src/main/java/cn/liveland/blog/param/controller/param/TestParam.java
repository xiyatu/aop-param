package cn.liveland.blog.param.controller.param;

import cn.liveland.blog.param.aspect.ClearBlank;

/**
 * @author xiyatu
 * @date 2019/4/26 16:18
 * Description
 */
public class TestParam {

    @ClearBlank(isAll = true)
    private String param1;
    @ClearBlank
    private String param2;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Override
    public String toString() {
        return "TestParam{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                '}';
    }
}
