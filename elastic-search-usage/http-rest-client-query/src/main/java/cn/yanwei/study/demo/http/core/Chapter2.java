package cn.yanwei.study.demo.http.core;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * httpCore 第二章的内容：阻塞I / O模型
 * I / O代表了高效且便捷的I / O模型
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/11/4 9:36
 */
@SuppressWarnings("unused")
public class Chapter2 {
    /**
     * 使用socket建立网最为简单的网路通信
     * 创建一个socket的服务端 基于JDK1.4版本以前的socket的编程
     * 源码分析：
     * 实际使用
     * impl
     * ==> new ServerSocket(8080);
     * ==> new SocksSocketImpl();
     * <p>
     * socket.getInputStream();
     * ==> new SocksSocketImpl().getInputStream();
     * ==> java.net.PlainSocketImpl.getInputStream();
     * ==> synchronized()
     * {
     * new java.net.SocketInputStream(this);
     * }
     * <p>
     * inputStream.read(buffer);
     * ==> n = socketRead(fd, b, off, length, timeout);
     * ==> private native int socketRead0(FileDescriptor fd,byte b[], int off, int len,int timeout)
     */
    private static void socketServer() throws IOException {
        ServerSocket server = new ServerSocket(8080);
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        serverSocketChannel.bind(new InetSocketAddress(8080));
//        ServerSocket server = serverSocketChannel.socket();
        try {
            Socket socket = server.accept();
            try {
                //返回的是inputStream的超类java.net.SocketInputStream.read()实际是使用的是 reader.readLine()阻塞了当前线程。
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                //abstract int read()
                //This method
                //blocks until input data is available
                while (inputStream.read(buffer) != -1) {
                    System.out.println(new String(buffer));
                }
            } finally {
                socket.close();
            }
        } finally {
            server.close();
        }
    }

    /**
     * 创建一个socket的客户端
     * impl
     * ==> new Socket("127.0.0.1", 8080);
     * ==> new SocksSocketImpl();
     * client.getOutputStream();
     * ==> impl.getOutputStream();
     * ==> new java.net.SocketOutputStream(this);
     * output.println(line);
     * ==> new PrintWriter(new java.net.SocketOutputStream(this), true).println(line);
     * ==>  out.write(s, off, len);
     * <p>
     * out.write(s, off, len);
     * ==> new BufferedWriter(new OutputStreamWriter(out)).write(s,off,len)
     */
    private static void tcpClient() throws IOException {
        Socket client = new Socket("127.0.0.1", 8080);
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                output.println(line);
                System.out.println("输入的数据：" + line);
            }
            scanner.close();
        } finally {
            client.close();
        }
    }

    /**
     * 构建connection相关信息
     * conn.bind(socket);
     * ==> AtomicReference<Socket> socketHolder = socket;
     * conn.isOpen();
     * ==> this.socketHolder.get();
     * ==> socket.get();
     */
    private static void connections() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        conn.bind(socket);
        System.out.println(conn.isOpen());
        HttpConnectionMetrics metrics = conn.getMetrics();
        System.out.println(metrics.getRequestCount());
        System.out.println(metrics.getResponseCount());
        System.out.println(metrics.getReceivedBytesCount());
        System.out.println(metrics.getSentBytesCount());
    }


    /**
     * 使用httpCore实现简单的client发送请求
     * 客户端上执行请求的过程过于简化，可能看起来像这样
     * 客户端和服务器的HTTP连接接口分两个阶段
     * 发送和接收消息
     * 首先发送消息头。根据消息头的属性，可能跟随消息主体
     * SessionInputBufferImpl inBuffer;
     * org.apache.http.impl.io.SessionInputBufferImpl.inStream = socket.getInputStream();
     * SessionOutputBufferImpl outbuffer;
     * org.apache.http.impl.io.SessionOutputBufferImpl.outbuffer = socket.getOutputStream();
     * <p>
     * conn.sendRequestHeader(request);
     * ==> this.inBuffer.bind(getSocketInputStream(socket));
     * ==> this.outbuffer.bind(getSocketOutputStream(socket));
     * <p>
     * new DefaultBHttpClientConnection(8 * 1024);
     * ==> this(bufferSize, bufferSize, null, null, null, null, null, null, null);
     * <p>
     * HttpMessageParser<HttpResponse> responseParser;
     * HttpMessageWriter<HttpRequest> requestWriter;
     * ==> new DefaultHttpRequestWriterFactory().
     * ==> new DefaultHttpResponseParser(new org.apache.http.impl.io.SessionInputBufferImpl(), lineParser, responseFactory, constraints);
     * <p>
     * conn.sendRequestHeader(request);
     * ==> this.requestWriter.write(request);
     * ==> new DefaultHttpRequestWriter(new SessionOutputBufferImpl(),LineFormatter lineFormatter).write(request);
     * ==> org.apache.http.impl.ioAbstractMessageWriter.write(request);
     * ==> org.apache.http.impl.io.DefaultHttpRequestWriter.writeHeadLine(message);
     * # 序列化request对象
     * ==> new BasicLineFormatter().formatRequestLine(this.lineBuf, message.getRequestLine());
     * # 将序列化的request对象写入流中
     * ==> new SessionOutputBufferImpl().writeLine(this.lineBuf);
     * ==> new SessionOutputBufferImpl().streamWrite(this.buffer.buffer(), 0, len);
     * ==> this.outstream.write(b, off, len);
     * #回到socket编程
     * ==> socket.getOutputStream().write(b, off, len);
     * <p>
     * 获取响应头信息
     * conn.receiveResponseHeader();
     * 类似于conn.sendRequestHeader(request);
     *
     *
     * @throws IOException, HttpException
     */
    private static void httpCoreClient() throws IOException, HttpException {
        //不同于socket编程conn只能绑定Socket，无法绑定ServerSocket
        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        Socket socket = new Socket("127.0.0.1", 8080);
        conn.bind(socket);
        HttpRequest request = new BasicHttpRequest("GET", "/");
        conn.sendRequestHeader(request);
        HttpResponse response = conn.receiveResponseHeader();
        conn.receiveResponseEntity(response);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //对实体做一些有用的事情，完成后确保所有
            //内容已被消耗，因此基础连接
            //可以重复使用
            EntityUtils.consume(entity);
        }
    }

    /**
     * 使用httpCore实现简单的服务器端请求处理过程
     *
     * @throws IOException, HttpException
     */
    private static void httpCoreServer() throws IOException, HttpException {
        ServerSocket server = new ServerSocket(8080);
        Socket socket = server.accept();
        DefaultBHttpServerConnection conn = new DefaultBHttpServerConnection(8 * 1024);
        conn.bind(socket);
        HttpRequest request = conn.receiveRequestHeader();
        if (request instanceof HttpEntityEnclosingRequest) {
            conn.receiveRequestEntity((HttpEntityEnclosingRequest) request);
            HttpEntity entity = ((HttpEntityEnclosingRequest) request)
                    .getEntity();
            if (entity != null) {
                //对实体做一些有用的事情，完成后确保所有
                //内容已被消耗，因此基础连接
                //可以重复使用
                EntityUtils.consume(entity);
            }
        }
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                200, "OK");
        response.setEntity(new StringEntity("Got it"));
        conn.sendResponseHeader(response);
        conn.sendResponseEntity(response);
    }

    /**
     * 阻止HTTP协议处理程序
     * HttpCore构建阻塞I / O模型的代码
     * HttpService 是基于阻塞I / O模型的服务器端HTTP协议处理程序
     * 内置httpProcessor（协议处理器），HttpServerConnection（内置连接器），HttpRequest(http的抽象类)
     */
    private static void blockingHttpHandlers() throws IOException, HttpException {
        HttpProcessor httpProcessor = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("MyServer-HTTP/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl())
                .build();
        //HttpService httpService = <...>
        //HttpServerConnection conn = <...>
        HttpServerConnection conn = new DefaultBHttpServerConnection(8 * 1024);
        //HttpContext上下文= <...>
        HttpContext context = HttpCoreContext.create();

        HttpRequest request = new BasicHttpRequest("GET", "/");
        httpProcessor.process(request, context);
        HttpService httpService = new HttpService(httpProcessor, null);
        boolean active = true;
        try {
            while (active && conn.isOpen()) {
                //可以循环执行此方法以处理持久连接上的多个请求。HttpService#handleRequest()从多个线程执行该 方法是安全的
                httpService.handleRequest(conn, context);
            }
        } finally {
            conn.shutdown();
        }
    }

    /**
     * 是基于阻塞I / O模型的客户端HTTP协议处理程序
     * 构建一个HttpRequestExecutor
     */
    private static void httpRequestExecutor() throws IOException, HttpException {
        //HttpServerConnection conn = <...>
        HttpClientConnection conn = null;
        HttpProcessor processor = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("MyClient/1.1"))
                .add(new RequestExpectContinue(true))
                .build();
        HttpRequestExecutor executor = new HttpRequestExecutor();
        HttpRequest request = new BasicHttpRequest("GET", "/");
        HttpCoreContext context = HttpCoreContext.create();
        executor.preProcess(request, processor, context);
        HttpResponse response = executor.execute(request, conn, context);
        executor.postProcess(response, processor, context);

        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
    }

    /**
     * HTTP/1.1默认情况下，client与server建立的联机是持久性的。需要在头信息的通过发送Keep-Alive或 发送Close值来决定是继续使用当前链接合适重新创建连接
     * 高效的客户端HTTP传输通常需要有效地重新使用持久性连接
     * HttpCore通过提供对持久HTTP连接池的管理支持，促进了连接重用过程
     */
    private static void useHttpConnectPool() throws ExecutionException, InterruptedException {
        HttpHost target = new HttpHost("localhost");
        BasicConnPool pool = new BasicConnPool();
        pool.setMaxTotal(200);
        pool.setDefaultMaxPerRoute(10);
        pool.setMaxPerRoute(target, 20);
        Future<BasicPoolEntry> future = pool.lease(target, null);
        BasicPoolEntry poolEntry = future.get();
        try {
            HttpClientConnection conn = poolEntry.getConnection();
        } finally {
            pool.release(poolEntry, true);
        }
        PoolStats totalStats = pool.getTotalStats();
        System.out.println("total available: " + totalStats.getAvailable());
        System.out.println("total leased: " + totalStats.getLeased());
        System.out.println("total pending: " + totalStats.getPending());
        PoolStats targetStats = pool.getStats(target);
        System.out.println("target available: " + targetStats.getAvailable());
        System.out.println("target leased: " + targetStats.getLeased());
        System.out.println("target pending: " + targetStats.getPending());
        //关闭空闲的链接
        pool.closeExpired();
        pool.closeIdle(1, TimeUnit.MINUTES);
    }

    /**
     * 构建自定义的连接池
     */
    private static void customHttpConnectPool() {
        CustomConnPool customPool = new CustomConnPool();
        customPool.enumAvailable(new PoolEntryCallback<HttpHost, HttpClientConnection>() {

            @Override
            public void process(final PoolEntry<HttpHost, HttpClientConnection> entry) {
                Date creationTime = new Date(entry.getCreated());
                if (creationTime.before(new Date())) {
                    entry.close();
                }
            }

        });
    }

    static class CustomConnPool extends BasicConnPool {

        @Override
        protected void enumAvailable(final PoolEntryCallback<HttpHost, HttpClientConnection> callback) {
            super.enumAvailable(callback);
        }

        @Override
        protected void enumLeased(final PoolEntryCallback<HttpHost, HttpClientConnection> callback) {
            super.enumLeased(callback);
        }

    }

    public static void main(String[] args) {
        @SuppressWarnings("all")
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            try {
                socketServer();
//                httpCoreServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
//                tcpClient();
//                connections();
                httpCoreClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
