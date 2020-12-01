package cn.yanwei.study.socket;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 描述java socket相关编程
 * 创建socket，java提供类
 * <p>
 * socket类
 * 1、import java.net.Socket;
 * 2、import java.net.ServerSocket;
 * <p>
 * channel相关类
 * 1、import java.nio.channels.SocketChannel;
 * 2、import java.nio.channels.ServerSocketChannel;
 * <p>
 * socket类转换成channel类
 * java.net.Socket.channel() ====> java.nio.channels.SocketChannel;
 * java.net.ServerSocket.channel() ====> java.nio.channels.ServerSocketChannel.socket();
 * <p>
 * channel类转换成socket类
 * java.nio.channels.SocketChannel.socket() ====> java.net.Socket;
 * java.nio.channels.ServerSocketChannel.socket() ====> java.net.ServerSocket;
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/11/9 16:26
 */
public class Demo {
    /**
     * 基于 java.net.ServerSocket; 构建socket-server服务
     */
    private static void server() throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(8080);
        try {
            java.net.Socket socket = server.accept();
            try {
                //虽然使用的是死循环,但是reader.readLine()阻塞了当前线程。
                //返回的是inputStream的超类java.net.SocketInputStream.read()实际是使用的是
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) != -1) {
                    System.out.println("服务端接受到的数据：" + new String(buffer));
                    Thread.sleep(100000);
                }
            } finally {
                socket.close();
            }
        } finally {
            server.close();
        }
    }

    /**
     * 基于 java.net.socket; 构建socket-client服务
     */
    private static void client() throws IOException {
        Socket client = new Socket("127.0.0.1", 8080);
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                output.println(line);
                System.out.println("服务端输入的数据：" + line);
            }
            scanner.close();
        } finally {
            client.close();
        }
    }

    /**
     * 服务启动
     */
    public static void main(String[] args) {
        ThreadFactory nameFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(128), nameFactory, new ThreadPoolExecutor.AbortPolicy());
        pool.submit(() -> {
            try {
                server();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pool.submit(() -> {
            try {
                client();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

class Demo1 {
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
                    readMsg(channel2);
                }

            }
            iterator.remove();
        }
    }

    private static void readMsg(SelectableChannel channel2) throws IOException {
        SocketChannel channel = (SocketChannel) channel2;
        SocketAddress localAddress = channel.getLocalAddress();
        //设置缓存区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读取数据
        int len;
        byte[] b = new byte[1024];
        while ((len = channel.read(buffer)) > 0) {
            buffer.flip();//刷新缓存区
            buffer.get(b, 0, len);
            System.out.println("服务端接受到数据为:" + new String(b, 0, len));
        }
    }


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
     * 服务启动
     */
    public static void main(String[] args) {
        ThreadFactory nameFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(128), nameFactory, new ThreadPoolExecutor.AbortPolicy());
        pool.submit(() -> {
            try {
                channelServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pool.submit(() -> {
            try {
                channelClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}




