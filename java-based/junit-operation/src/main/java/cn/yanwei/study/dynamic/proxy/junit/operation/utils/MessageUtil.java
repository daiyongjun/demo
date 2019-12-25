package cn.yanwei.study.dynamic.proxy.junit.operation.utils;

/**
 * This class prints the given message on console.
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 15:12
 */
public class MessageUtil {
    private String message;

    /**
     * Constructor
     *
     * @param message to be printed
     */
    public MessageUtil(String message) {
        this.message = message;
    }


    /**
     * prints the message
     */
    public String printMessage() {
        System.out.println(message);
        return message;
    }
}
