package cn.yanwei.study.demo.http.core;

import org.apache.http.*;
import org.apache.http.entity.*;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * httpCore 第一章的内容：基本原理【Fundamentals】
 * HTTP消息组成
 * 标头：
 * 请求行和标头字段的集合组成
 * 可选主体组成：
 * (可选)表示请求实体或者响应中的内容实体
 * core 会基于此协议创建相关的对象模型，本章内容大多是介绍相关的对象模型及一些操作
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/11/4 9:36
 */
@SuppressWarnings("unused")
public class Chapter1 {
    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * 展示请求的对象模型
     */
    private static void showRequestModel() {
        HttpRequest request = new BasicHttpRequest("GET", "/",
                HttpVersion.HTTP_1_1);
        System.out.println(request.getRequestLine().getMethod());
        System.out.println(request.getRequestLine().getUri());
        System.out.println(request.getProtocolVersion());
        System.out.println(request.getRequestLine().toString());
    }

    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * 展示响应的对象模型
     */
    private static void showResponseModel() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
    }

    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * 修改,删除，增加 HTTP 消息元素
     */
    private static void operateResponseModel() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);
        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);
        Header[] hs = response.getHeaders("Set-Cookie");
        System.out.println(hs.length);
    }

    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * 使用迭代器展示HTTP消息元素
     */
    private static void iteratorResponseModel() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

        HeaderIterator it = response.headerIterator("Set-Cookie");

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * 显示HTTP消息元素，并提供消息元素实体。
     */
    private static void showResponseMessageModel() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator("Set-Cookie"));

        while (it.hasNext()) {
            HeaderElement elem = it.nextElement();
            System.out.println(elem.getName() + " = " + elem.getValue());
            NameValuePair[] params = elem.getParameters();
            for (NameValuePair param : params) {
                System.out.println(" " + param);
            }
        }
    }


    /**
     * Core创建于此协议相关的对象模型，为HTTP消息元素的序列化（格式化）和反序列化（解析）提供了广泛的支持
     * Core还创建了HTTP Entity 相关的对象模型，
     * 请求使用实体的请求成为封闭实体的请求，HTTP规范定义了两种实体封装方法,POST和PUT。
     * 不同于请求，通常期望响应包含内容实体。
     * 实体存在两种：
     * 实体既可以表示二进制内容又可以表示字符内容，因此它支持字符编码（以支持后者，即字符内容）。
     */
    private void createEntityModel() throws IOException {
        //###请求####
        //ByteArrayEntity 是一个自包含的可重复实体，可以从给定的字节数组中获取其内容。将字节数组提供给构造函数。
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(new byte[]{1, 2, 3});
        //必须关闭与实体关联的内容流。
        byteArrayEntity.getContent().close();

        //StringEntity是一个自包含的，可重复的实体，该实体从java.lang.String对象获取其内容。
        StringEntity stringEntity = new StringEntity("重要消息", "UTF_8");
        //必须关闭与实体关联的内容流。
        stringEntity.getContent().close();

        //FileEntity是一个自包含的可重复实体，可从文件获取其内容。主要用于流式传输不同类型的大文件
        HttpEntity entity = new FileEntity(new File("文件路径"), "application/java-archive");
        entity.getContent().close();

        //###响应####
        //BasicHttpEntity   此基本实体表示基础流。通常，将此类用于从HTTP消息接收的实体
        BasicHttpEntity myEntity = new BasicHttpEntity();
        //inputStream作为HTTP响应内容
        InputStream inputStream = null;
        myEntity.setContent(inputStream);
        // sets the length to 340
        myEntity.setContentLength(340);
        myEntity.getContent().close();

        //inputStream作为HTTP响应内容
        InputStream instream1 = null;
        InputStreamEntity inputStreamEntity = new InputStreamEntity(instream1, 16);
        inputStreamEntity.getContent().close();

        //InputStreamEntity或者BasicHttpEntity，通过提供另一个实体来构造它。它从提供的实体中读取内容，并将其缓冲在内存中。将不可重复使用的实体变更成可重复使用的实体
        BufferedHttpEntity myBufferedEntity = new BufferedHttpEntity(inputStreamEntity);
        myBufferedEntity.getContent().close();
    }

    /**
     * 操作实体内容，读取实体内容，写入实体内容
     */
    private void operateEntityModel() throws IOException {
        //读取实体内容
        StringEntity stringEntity = new StringEntity("重要消息", "UTF_8");
        InputStream inputStream = stringEntity.getContent();
        inputStream.close();
        //写入实体内容
        OutputStream outputStream = null;
        stringEntity.writeTo(outputStream);
        outputStream.close();
    }

    /**
     * 这让我想起实习的时候请求的加密问题
     * HTTP协议拦截器是,协议拦截器应作用于传入消息的一个特定标头或一组相关标头。
     * 1、使用一个特定标头或一组相关标头填充输出消息
     * 2、操纵消息中包含的内容实体
     * 3、透明的内容压缩/解压缩就是一个很好的例子
     * <p>
     * 通常，HTTP协议处理器用于在执行应用程序特定的处理逻辑之前对传入消息进行预处理，并对传出消息进行后处理。
     * <p>
     * RequestContent 请求的最重要的拦截器，根据所包含实体的属性和协议版本 添加Content-Length或 头 来界定内容长度。
     * ResponseContent 响应的最重要的拦截器，根据所包含实体的属性和协议版本 添加Content-Length或 头 来界定内容长度。
     * RequestConnControl 将Connection标头添加 到传出请求中。
     * ResponseConnControl 将Connection标头添加到传出响应中。
     * RequestDate 负责将Date标头添加 到传出请求中。
     * ResponseDate 负责将Date标头添加 到传出响应中。
     * RequestExpectContinue   负责通过添加Expect标头来启用"预期-继续"握手
     * RequestTargetHost    负责添加 Host标题
     * RequestUserAgent     负责添加 User-Agent标题
     * ResponseServer       负责添加 Server标题
     * HttpProcessorBuilder.create();
     * ==> ChainBuilder<HttpRequestInterceptor>  new HttpProcessorBuilder().requestChainBuilder;
     * ==> ChainBuilder<HttpResponseInterceptor> new HttpProcessorBuilder().responseChainBuilder;
     * ==> requestChainBuilder.add(HttpRequestInterceptor e);
     * ==> responseChainBuilder.add(HttpResponseInterceptor e);
     * <p>
     * HttpProcessorBuilder.create().bulid();
     * ==> new ImmutableHttpProcessor(
     * requestChainBuilder != null ? requestChainBuilder.build() : null,
     * responseChainBuilder != null ? responseChainBuilder.build() : null);
     * httpProcessor.process(request, context);
     * <p>
     * # HttpResponse response, HttpContext context
     * ==> requestInterceptor.process(request, context);
     * ==> responseInterceptor.process(response, context);
     * ==> org.apache.http.protocol.RequestContent.process();
     * # 实际功能 请求的最重要的拦截器，根据所包含实体的属性和协议版本 添加Content-Length或 头 来界定内容长度。
     * ==> request.addHeader(HTTP.CONTENT_LEN, Long.toString(entity.getContentLength()));
     */
    private static void useHttpProtocolProcessor() throws IOException, HttpException {
        HttpProcessor httpProcessor = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .build();
        HttpCoreContext context = HttpCoreContext.create();
        HttpRequest request = new BasicHttpRequest("GET", "/");
        httpProcessor.process(request, context);
    }
    /**
     * HTTP执行上下文,HTTP请求本身是无状态的。实际操作过程中可能我们需要一种有状态。
     * HTTP上下文是一种可用于将属性名称映射到属性值的结构，我们可以自定义key和value将无状态的的HTTP请求设置为有状态，如session-id,使用相同的session-id被确定是同一个用户的请求，表达了一种状态
     * 分析如何增加上下文数据
     * httpProcessor.process(request, context);
     * ==> org.apache.http.HttpRequestInterceptor.process(request,context)
     * #我们实例化的过程中已经实现了增加session-id的内容
     * {
     * String id = (String) context.getAttribute("session-id");
     * if (id != null) {
     * request.addHeader("Session-ID", id);
     * }
     * }
     **/
    private void useHttpExecutionContext() throws IOException, HttpException {
        HttpProcessor httpProcessor = HttpProcessorBuilder.create()
                .add((HttpRequestInterceptor) (request, context) -> {
                    String id = (String) context.getAttribute("session-id");
                    if (id != null) {
                        request.addHeader("Session-ID", id);
                    }
                })
                .build();
        HttpCoreContext context = HttpCoreContext.create();
        HttpRequest request = new BasicHttpRequest("GET", "/");
        httpProcessor.process(request, context);
    }


    public static void main(String[] args) throws IOException, HttpException {
//        showRequestModel();
//        showResponseModel();
        useHttpProtocolProcessor();
    }
}
