package cn.yanwei.study.demo.http.core;

import org.apache.http.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.pool.BasicNIOPoolEntry;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.nio.*;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.nio.reactor.*;
import org.apache.http.protocol.*;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * httpCore 第三章的内容：非阻塞 异步I / O模型
 * HttpCore NIO基于Doug Lea所描述的Reactor模式
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/11/6 9:42
 */
public class Chapter3 {
    /**
     * 基于NIO构建 SocketServer
     */
    private static void channelServer() throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //设置服务端通道非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册选择器 , 设置选择器选择的操作类型
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //selector 处理接入
        while (selector.select() > 0) {
            //阻塞体现在Selector上
            //可能客户到请求一次进来多个,进行判断
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //获取其中的每个请求
                SelectionKey next = iterator.next();
                //判断请求是属于 连接过的还是未连接过的
                if (next.isAcceptable()) {
                    //没连接过,直接跟踪,设置为读取状态
                    SocketChannel accept = serverSocketChannel.accept();
                    //通道允许接入请求
                    accept.configureBlocking(false);
                    //设置非阻塞 设置为读取数据
                    accept.register(selector, SelectionKey.OP_READ);
                }
                if (next.isReadable()) {
                    //连接过,直接读取
                    SelectableChannel channel2 = next.channel();
                    channel2.configureBlocking(false);
                    //读取通道信息
//                    readMsg(channel2);
                }
            }
            iterator.remove();
        }
    }

    /**
     * 基于NIO构建 SocketClient
     */
    private static void channelClient() throws IOException {
        //声明连接地址对象 nio框架使用tcp协议绑定ip和端口
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
        //打开一个和服务端连接的通道
        SocketChannel channel = SocketChannel.open();
        channel.connect(socketAddress);
        //设置通道为非阻塞
        channel.configureBlocking(false);
        //设置缓存区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //控制台的输入数据输出
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //清除缓存区的数据
            buffer.clear();
            //获取控制台的数据
            String data = scanner.nextLine();
            buffer.put(data.getBytes());
            //刷新 缓存区的数据,与IO流的flush类似
            buffer.flip();
            channel.write(buffer);
        }
    }


    /**
     * httpCore NIO基于Doug Lea所描述的Reactor模式
     * <p>
     * 在内部，IOReactor实现封装NIO的功能java.nio.channels.Selector。【你需要拥有java nio的基础】
     * <p>
     * I / O反应器通常使用少量的分派线程（通常少至一个）将I / O事件通知分派给更多的I / O会话或连接（通常多达数千个）;通常建议每个CPU内核有一个调度线程。
     * 查看config的配置 return DefaultMaxIoThreadCount > 0 ? DefaultMaxIoThreadCount : Runtime.getRuntime().availableProcessors();
     **/
    private static void reactor() throws IOReactorException {
        IOReactorConfig config = IOReactorConfig.DEFAULT;
        IOReactor ioreactor = new DefaultConnectingIOReactor(config);
    }

    /**
     * IOEventDispatch适用于监听 更多的I / O会话或连接（通常多达数千个）的状态。这些监听会运行调度线程上，并不会阻塞更多的I/O会话和连接。
     */
    private static void dispatchers() throws IOException {
        IOReactorConfig config = IOReactorConfig.DEFAULT;
        IOReactor reactor = new DefaultConnectingIOReactor(config);
        //IOEventDispatch 接口 定义的通用I / O事件：reactor内置多种事件
        reactor.execute(new IOEventDispatch() {
            @Override
            public void connected(IOSession session) {
                //在创建新会话时触发。

            }

            @Override
            public void inputReady(IOSession session) {
                //在会话有待处理的输入时触发。
            }

            @Override
            public void outputReady(IOSession session) {
                //在会话准备输出时触发。
            }

            @Override
            public void timeout(IOSession session) {
                //会话超时时触发。
            }

            @Override
            public void disconnected(IOSession session) {
                //会话终止时触发。
            }
        });
    }

    /**
     * I / O反应器的关闭是一个复杂的过程，通常可能需要一段时间才能完成。
     */
    private static void closeReactor() throws IOException {
        IOReactorConfig config = IOReactorConfig.DEFAULT;
        IOReactor reactor = new DefaultConnectingIOReactor(config);
        // milliseconds
        long gracePeriod = 3000L;
        reactor.shutdown(gracePeriod);
    }

    /**
     * IOSession接口表示两个端点之间的一系列逻辑相关的数据交换。表示一次会话。
     * 且IOSession中存在java.nio.channels.SelectionKey和 java.nio.channels.SocketChannel两个属性
     * 我们可以设置会话属性,我们也可以为ioSession设置特定掩码
     */
    private static void ioSession() throws IOException {
        IOSession ioSession = null;
        //为session中设置指定信息
        ioSession.setAttribute("state", "someState");
        //为session设置特定监听
        ioSession.setEventMask(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        ReadableByteChannel ch = (ReadableByteChannel) ioSession.channel();
        ByteBuffer dst = ByteBuffer.allocate(2048);
        ch.read(dst);
        System.out.println(ioSession.getAttribute("state"));
        //单独操作特定的事件
        ioSession.setEvent(SelectionKey.OP_READ);
        ioSession.clearEvent(SelectionKey.OP_READ);
        //关闭ioSession会话
        ioSession.shutdown();
    }

    /**
     * ListeningIOReactor可以监听一个或者多个不同connect（多个端口连接）类似创建一个服务端
     */
    private static void listeningIoReactor() throws IOReactorException, InterruptedException {
        //ListeningIOReactor ioreactor = <...>
        ListeningIOReactor reactor = null;
        ListenerEndpoint ep1 = reactor.listen(new InetSocketAddress(8081));
        ListenerEndpoint ep2 = reactor.listen(new InetSocketAddress(8082));
        ListenerEndpoint ep3 = reactor.listen(new InetSocketAddress(8083));
        //等待所有端点启动
        ep1.waitFor();
        ep2.waitFor();
        ep3.waitFor();

        //始接受传入的连接并将I / O活动通知传播到IOEventDispatch 实例
        //可以在运行时获取一组已注册的端点，在运行时查询端点的状态，然后根据需要将其关闭。
        //类似于SelectionKey
        Set<ListenerEndpoint> eps = reactor.getEndpoints();
        for (ListenerEndpoint ep : eps) {
            // Still active?
            System.out.println(ep.getAddress());
            if (ep.isClosed()) {
                // If not, has it terminated due to an exception?
                if (ep.getException() != null) {
                    ep.getException().printStackTrace();
                }
            } else {
                ep.close();
            }
        }

    }

    /**
     * ConnectingIOReactor 代表能够与远程主机建立连接的I / O反应器。 类似创建一个客户端
     */
    private static void connectingReactors() throws InterruptedException {
        //ConnectingIOReactor reactor = <...>
        ConnectingIOReactor reactor = null;

        SessionRequest sessionRequest = reactor.connect(
                new InetSocketAddress("www.google.com", 80),
                null, null, new SessionRequestCallback() {

                    @Override
                    public void completed(SessionRequest request) {
                        System.out.println("new connection to " +
                                request.getRemoteAddress());
                    }

                    @Override
                    public void failed(SessionRequest request) {
                        if (request.getException() != null) {
                            request.getException().printStackTrace();
                        }
                    }

                    @Override
                    public void timeout(SessionRequest request) {

                    }

                    @Override
                    public void cancelled(SessionRequest request) {

                    }
                });
        sessionRequest.setConnectTimeout(1000);
        sessionRequest.waitFor();
        if (sessionRequest.getException() != null) {
            sessionRequest.getException().printStackTrace();
        }
        // Get hold of the new I/O session
        IOSession session = sessionRequest.getSession();

        //关闭会话请求
        if (!sessionRequest.isCompleted()) {
            sessionRequest.cancel();
        }
    }

    /**
     * / O反应器使用与系统有关的配置，在大多数情况下，这些配置应该足够明智
     */
    private void reactorConfiguration() throws IOReactorException {
        //基于默认的配置
        IOReactorConfig config = IOReactorConfig.DEFAULT;
        IOReactor ioreactor = new DefaultListeningIOReactor(config);
        //基于自定义的custom配置
        IOReactorConfig customConfig = IOReactorConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(5000)
                .setSoReuseAddress(true)
                .setConnectTimeout(5000)
                .build();
        ioreactor = new DefaultListeningIOReactor(customConfig);

    }

    /**
     * 基于java nio包中的 java.nio.channels.SelectionKey如果I / O选择器处于执行选择操作的过程中，
     * 会造成写入可能会无限期地阻塞，HttpCore NIO可以配置为以特殊模式运行，其中仅当I / O选择器未参与选择操作时，I / O兴趣集操作才在调度线程上排队并执行。
     */
    private void queuing() {
        IOReactorConfig config = IOReactorConfig.custom()
                .setInterestOpQueued(true)
                .build();
    }


    /**
     * 协议特定的异常以及在与会话通道交互过程中引发的那些I / O异常是可以预料的，
     * 并应由特定的协议处理程序处理。这些异常可能会导致单个会话终止，但不会影响I / O反应器和所有其他活动会话。但是，在某些情况下，当I / O反应器本身遇到内部问题时，例如底层NIO类中的I / O异常或未处理的运行时异常。这些类型的异常通常是致命的，并将导致I / O反应器自动关闭
     * 比如：单个线程内的移仓
     */
    private void customExceptionHandling() {
        DefaultConnectingIOReactor ioreactor = null;
        ioreactor.setExceptionHandler(new IOReactorExceptionHandler() {

            @Override
            public boolean handle(IOException ex) {
                if (ex instanceof BindException) {
                    // bind failures considered OK to ignore
                    return true;
                }
                return false;
            }

            @Override
            public boolean handle(RuntimeException ex) {
                if (ex instanceof UnsupportedOperationException) {
                    // Unsupported operations considered OK to ignore
                    return true;
                }
                return false;
            }

        });
    }

    /**
     * Non-Blocking-Http-Connect 实际是IOSession的HTTP特定功能的包装。
     * 非阻塞HTTP连接是有状态的，并且不是线程安全的。非阻塞HTTP连接上的输入/输出操作应仅限于由I / O事件分派线程触发的分派事件。
     * 如何确保线程安全
     * 非阻塞HTTP连接没有绑定到特定的执行线程，因此它们需要维护自己的执行上下文，来确保现场安全
     * 在任何时间点，都可以获取当前正在通过非阻塞HTTP连接传输的请求和响应对象。如果当前没有传入或传出的消息，则这些对象中的任何一个或两个都可以为null。
     * 非阻塞HTTP连接可以在全双工模式下运行，注意当前请求和当前响应不一定代表相同的消息交换
     */
    private void nonBlockingConnections() throws IOException, HttpException {
        NHttpClientConnection conn = null;
        //请求数据
        Object myStateObject = null;
        HttpContext context = conn.getContext();
        context.setAttribute("state", myStateObject);
        //在任何时间点，我们都可以从conn中获取请求信息
        HttpRequest request = new BasicHttpRequest("GET", "/");
        Object state = context.getAttribute("state");

        System.out.println(conn.isRequestSubmitted());
        if (request != null) {
            System.out.println("Transferring request: " +
                    request.getRequestLine());
        }

        //响应数据
        NHttpServerConnection connection = null;
        // Obtain processing state
        state = context.getAttribute("state");
// Generate a response based on the state information
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContentType("text/plain");
        entity.setChunked(true);
        response.setEntity(entity);
        System.out.println(connection.isResponseSubmitted());
        connection.submitResponse(response);

        if (response != null) {
            System.out.println("Transferring response: " +
                    response.getStatusLine());
        }
    }

    /**
     * HttpCore NIO提供ContentEncoder并 ContentDecoder接口来处理异步内容传输过程
     * <p>
     * 非阻塞HTTP连接将根据消息附带的实体的属性实例化内容编解码器的适当实现。来应对异步传输过程与同步方式的不同
     * 固有的阻塞 java.io.InputStream和java.io.OutputStream 类来表示入站和出站内容流
     **/
    private void nonBlockingTransfer() throws IOException {
        ContentDecoder decoder = null;
        // 读取数据
        ByteBuffer dst = ByteBuffer.allocate(2048);
        decoder.read(dst);
        // 内容实体已完全转移
        if (decoder.isCompleted()) {
            // 完成
        }

        ContentEncoder encoder = null;
        // 准备输出数据
        ByteBuffer src = ByteBuffer.allocate(2048);
        // 写出数据
        encoder.write(src);
        // 完成后将内容实体标记为已完全转移
        encoder.complete();
    }


    /**
     * 将实体封装消息提交到非阻塞HTTP连接时,依然使用HttpEntity，但是异步请求会忽略
     * 忽略固有的阻塞HttpEntity#getContent()和 HttpEntity#writeTo()封闭实体的方法
     */
    private void submittingEntityToConnect() throws IOException, HttpException {
        //响应
        NHttpServerConnection conn = null;
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContentType("text/plain");
        entity.setChunked(true);
        entity.setContent(null);
        response.setEntity(entity);

        conn.submitResponse(response);

        //请求
        NHttpClientConnection client = null;
        //如果使用 HttpEntity#getContent() or HttpEntity#writeTo() 会出现java.lang.IllegalStateException的异常信息
        HttpResponse response1 = client.getHttpResponse();
        HttpEntity entity1 = response1.getEntity();
        if (entity != null) {
            System.out.println(entity.getContentType());
            System.out.println(entity.getContentLength());
            System.out.println(entity.isChunked());
        }
    }

    /**
     * 阻塞内容传输机制
     * Content-Length定界的：  内容实体的结尾由Content-Length标头的值确定 。最大实体长度： Long#MAX_VALUE。
     * 身份编码：  通过关闭基础连接（流条件结束）来区分内容实体的末尾。由于明显的原因，身份编码只能在服务器端使用。最大实体长度：无限制。
     * 块编码：  内容按小块发送。最大实体长度：无限制。
     * <p>
     * 内容代码经过优化
     * 从而直接从底层I / O会话的通道读取数据或将数据直接写入底层I / O会话的通道
     * <p>
     * 那些不执行任何内容转换的Content-Length编解码器（例如，定界和身份编解码器）可以利用NIOjava.nio.FileChannel方法来显着提高入站和出站文件传输操作的性能。
     * 如果实际的内容编码器实现FileContentEncoder 了，则可以利用其方法绕过中间文件直接从文件中写入传出内容java.nio.ByteBuffer。
     */
    private void directChannel() throws IOException {
        ContentDecoder decoder = null;
        //准备文件通道
        FileChannel dst = null;
        if (decoder instanceof FileContentDecoder) {
            long bytesread = ((FileContentDecoder) decoder)
                    .transfer(dst, 0, 2048);
            if (decoder.isCompleted()) {
                // Done
            }
        }

        ContentEncoder encoder = null;
        // Prepare file channel
        FileChannel src = null;
        // Make use of direct file I/O if possible
        if (encoder instanceof FileContentEncoder) {
            // Write data out
            long bytesWritten = ((FileContentEncoder) encoder)
                    .transfer(src, 0, 2048);
            // Mark content entity as fully transferred when done
            encoder.complete();
        }
    }

    /**
     * 非阻塞实体
     */
    private void nonBlockingEntities() {
        //这是一个简单的自包含可重复实体，它从给定的字节数组接收其内容
        NByteArrayEntity entity = new NByteArrayEntity(new byte[]{1, 2, 3});
        //这是一个简单，自包含，可重复的实体，可从java.lang.String对象检索其数据
        NStringEntity stringEntity = new NStringEntity("important message",
                Consts.UTF_8);
        //
        File staticFile = new File("/path/to/myapp.jar");
        NFileEntity fileEntity = new NFileEntity(staticFile, ContentType.create("application/java-archive", (Charset) null));

    }

    /**
     * 异步处理服务
     * HttpAsyncService是基于非阻塞（NIO）I / O模型的完全异步HTTP服务器端协议处理程序
     * HttpAsyncService将通过NHttpServerEventHandler接口触发的单个事件 转换为逻辑上相关的HTTP消息交换。
     */
    private void asynchronousHttpService() throws IOException {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("MyServer-HTTP/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl())
                .build();
        HttpAsyncService protocolHandler = new HttpAsyncService(httpproc, null);
        IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(
                protocolHandler,
                new DefaultNHttpServerConnectionFactory(ConnectionConfig.DEFAULT));
        ListeningIOReactor ioreactor = new DefaultListeningIOReactor();
        ioreactor.execute(ioEventDispatch);
    }

    /**
     * 处理协议
     * HttpAsyncRequestHandler表示用于异步处理特定的非阻塞HTTP请求组的例程
     * <p>
     * 协议处理程序旨在处理协议特定方面
     * 而各个请求处理程序应处理特定于应用程序的HTTP处理。请求处理程序的主要目的是生成带有内容实体的响应对象，以响应给定的请求将其发送回客户端
     */
    private void nonBlockingRequestHandlers() {
        HttpAsyncRequestHandler<HttpRequest> rh = new HttpAsyncRequestHandler<HttpRequest>() {

            @Override
            public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException {
                //在内存中缓冲请求内容
                /**
                 * HttpAsyncRequestConsumer促进HTTP请求的异s步处理过程。
                 * 是HttpAsyncRequestHandlers用来处理传入HTTP请求消息
                 * 并从非阻塞服务器端HTTP连接流式传输其内容的回调接口 。
                 */
                return new BasicAsyncRequestConsumer();
            }

            @Override
            public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException {
                HttpResponse response = httpExchange.getResponse();
                response.setStatusCode(HttpStatus.SC_OK);
                NFileEntity body = new NFileEntity(new File("static.html"),
                        ContentType.create("text/html", Consts.UTF_8));
                response.setEntity(body);
                httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
            }
        };
    }

    /**
     * 处理响应
     * 与非阻塞请求处理程序相比，非阻塞请求处理程序的最根本区别是能够通过将处理HTTP请求的过程委派给工作线程或非工作线程来将HTTP响应的传输推迟回客户端，而不会阻塞I / O线程
     * 能够通过将处理HTTP请求的过程委派给工作线程或非工作线程来将HTTP响应的传输推迟回客户端
     * 下例就是使用非阻塞线程进行处理
     * 请求处理程序必须以线程安全的方式实现。与Servlet相似，请求处理程序不应使用实例变量，除非对这些变量的访问已同步。
     */
    private void nonBlockingRequestExchange() {
        HttpAsyncRequestHandler<HttpRequest> rh = new HttpAsyncRequestHandler<HttpRequest>() {
            @Override
            public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) {
                //为了简单起见，在内存中缓冲请求内容
                return new BasicAsyncRequestConsumer();
            }

            @Override
            public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                        }
                        HttpResponse response = httpExchange.getResponse();
                        response.setStatusCode(HttpStatus.SC_OK);
                        NFileEntity body = new NFileEntity(new File("static.html"),
                                ContentType.create("text/html", Consts.UTF_8));
                        response.setEntity(body);
                        httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
                    }
                }.start();
            }
        };

        //HttpAsyncRequestConsumer
        //HttpAsyncRequestProducer
        //HttpAsyncResponseConsumer
        //BasicAsyncResponseProducer
    }

    /**
     * 连接池
     * 非阻塞连接池与阻塞池非常相似，其显着区别是它们必须回复I / O反应器以建立新连接
     * 由连接池管理的非阻塞连接不能绑定到任意I / O会话。
     */
    private void nonBlockingConnectionPools() {
        HttpHost target = new HttpHost("localhost");
        ConnectingIOReactor ioreactor = null;
        BasicNIOConnPool connpool = new BasicNIOConnPool(ioreactor);
        connpool.lease(target, null,
                10, TimeUnit.SECONDS,
                new FutureCallback<BasicNIOPoolEntry>() {
                    @Override
                    public void completed(BasicNIOPoolEntry entry) {
                        NHttpClientConnection conn = entry.getConnection();
                        System.out.println("Connection successfully leased");
                        // Update connection context and request output
                        conn.requestOutput();
                    }

                    @Override
                    public void failed(Exception ex) {
                        System.out.println("Connection request failed");
                        ex.printStackTrace();
                    }

                    @Override
                    public void cancelled() {
                    }
                });
    }

    //由于异步通信模型的事件驱动性质，很难确保将持久性连接正确释放回池中
    ConnectingIOReactor ioreactor1 = null;
    HttpProcessor httpproc1 = null;
    BasicNIOConnPool connpool1 = new BasicNIOConnPool(ioreactor1);
    HttpAsyncRequester requester = new HttpAsyncRequester(httpproc1);
    Future<HttpResponse> future = requester.execute(
            new BasicAsyncRequestProducer(
                    new HttpHost("localhost"),
                    new BasicHttpRequest("GET", "/")),
            new BasicAsyncResponseConsumer(),
            connpool1);


}

