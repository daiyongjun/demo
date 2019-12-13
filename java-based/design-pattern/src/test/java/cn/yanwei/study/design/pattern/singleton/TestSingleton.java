package cn.yanwei.study.design.pattern.singleton;

import org.junit.jupiter.api.Test;

/**
 * 5、增加单元测试-单例模式
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 16:15
 */
@SuppressWarnings("unused")
class TestSingleton {
    @Test
    void run() {
        Singleton singleton = Singleton.getInstance();
        singleton.showMessage();
    }
}
