


# Netty介绍

1. Netty是一个异步的、基于事件驱动的网络应用框架，用以快速开发高性能、高可靠的网络IO程序。
2. Netty主要针对在TCP协议下，面向Clients端的高并发应用，或者Pear to Pear场景下的大量数据持续传输的应用。
3. Netty本质是一个NIO框架，适用于服务器通讯相关的多种应用场景。



# I/O模型

1. I/O模型简单的理解就是用什么样的通道进行数据的发送和接收，很大程序上决定了程序通信的性能。
2. Java共支持3种网络编程模型(I/O模型)：BIO、NIO、AIO
    1. Java BIO：同步并阻塞，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务端就需要启动一个线程进行处理，如果这个连接不做任何事情
    会造成不必要的线程开销。适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，但程序
    程序简单易理解。
    2. Java NIO：同步非阻塞，服务器实现模式为一个线程处理多个请求(连接)，即客户端发送的请求都会注册到多路复用器上，
    多路复用器轮询到连接有I/O请求就进行处理。适用于连接数目多且连接较短(轻操作)的架构，比如聊天服务器，弹幕系统，服务器
    间通讯等。编程比较复杂，JDK1.4开始支持。
    3. Java AIO：异步非阻塞，AIO引入异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程，它的特点是先由操作系统
    完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用。适用于连接数目多且连接比较长(重操作)的架构，比如相册服务器，
    充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。


# NIO
1. Java NIO全称java non-blocking IO。
2. Java NIO相关类都被放在java nio包及子包下，并且对原java.io包中的很多类进行改写。
3. NIO有三大核心部分：Channel(通道)、Buffer(缓冲区)、Sellector(选择器)
4. NIO是面向缓冲区、或者面向块编程的。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这就增加了处理过程中的灵活性，
使用它可以提供非阻塞式的高伸缩性网络。


 
## NIO和BIO的比较

1. BIO以流的方式处理数据，而NIO以块的方式处理数据，块IO的效率比流IO高很多。
2. BIO是阻塞的，NIO是非阻塞的。
3. BIO基于字节流和字符流进行操作，而NIO基于Channel和Buffer进行操作。数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
Selector用于监听多个通道的事件，因此使用单个线程就可以监听多个客户端通道。

## Buffer、Channel和Selector的关系

1. 每个Channel都会对应一个Buffer。
1. Selector对应一个线程，一个线程对应多个Channel。
1. 程序切换到哪个Channel是由事件决定的，Event是一个重要的概念。
1. Selector会根据不同的事件，在各个通道上切换。
1. Buffer是一个内存块，底层是一个数组。
1. BIO中要么是输入流，要么是输出流，不能双向。但NIO的Buffer是可以读也可以写，但需要flip方法切换。
1. Channel是双向的，可以返回底层操作系统的情况。


# 缓冲区(Buffer)

1. Buffer类定义的所有的缓冲区都具有的四个属性来提供关于其所包含的数据元素的信息。
    2. capacity：容量
    2. limit：表示缓冲区的当前终点
    2. position：位置，下一个要被读或写的元素的索引
    2. mark：标记


# 通道(Channel)

1. 通道可以同时进行读写，而流只能读或者只能写。
1. 通道可以实现异步读写数据。
1. 通道可以从缓冲读数据，也可以写数据到缓冲。

## 常用的Channel类

1. FileChannel：用于文件的数据读写
1. DatagramChannel：用于UDP的数据读写
1. ServerSocketChannel：用于TCP的数据读写
1. SocketChannel：用于TCP的数据读写


## Buffer和Channal的注意事项

1. ByteBuffer支持类型化的put和get，put放入的是什么数据类型，get就应该使用相应的数据类型来取出，否则可能有BufferUnderflowException异常。
1. 可以将一个变通的Buffer转成只读Buffer。
1. NIO还提供了MappedByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改。


# 选择器(Selector)

1. Java的NIO，用非阻塞的IO方式。可以用一个线程，处理多个客户端连接，就会使用到Selector。
1. Selector能够检测多个注册的通道上是否有事件发生（注意：多个Channel以事件的方式可以注册到同一个Selector），
如果有事件发生，便获取事件然后针对每个事件进行相应的处理。这样就可以只用一个单线程去管理多个通道，也就是管理多个连接和请求。
1. 只有在连接真正有读写事件发生时，才会进行读写，就大大地减少了系统开销，并且不必为每个连接都创建一个线程，不用去维护多个线程。
1. 避免了多线程之间的上下文切换导致的开销。

## SelectionKey

表示Selector和网络通道的注册关系，共四种：
1. OP_ACCEPT：有新的网络连接可以accept，值为16
1. OP_CONNECT：代表连接已经建立，值为8
1. OP_READ：代表读操作，值为1
1. OP_WRITE：代表写操作，值为4


1. ServerSocketChannel在服务端监听新的客户端Socket连接。
1. SocketChannel，网络IO通道，具体负责进行读写操作。NIO把缓冲区的数据写入通道，或者把通道里的数据读到缓冲区。



# 零拷贝

1. 零拷贝是网络编程的关键，很多性能优化都离不开。
1. 在Java程序中，常用 的零拷贝有mmap(内存映射)和sendFile。


## mmap

mmap通过内存映射，将文件映射到内核缓冲区，同时，用户空间可以共享内核空间的数据。这样，在进行网络传输时，就可以减少内核空间
到用户控件的拷贝次数。

## sendFile

Linux2.1版本提供了sendFile函数，其基本原理如下：数据基本不经过用户态，直接从内核缓冲区进入到SocketBuffer，
同时，由于和用户态完全无关，就减少了一次上下文切换。

## mmap和sendFile的区别

1. mmap适合小数据量读写，sendFile适合大文件传输
1. mmap需要4次上下文切换，3次数据拷贝。sendFile需要3次上下文切换，最少2次数据拷贝。
1. sendFile可以利用DMA方式，减少CPU拷贝，mmap则不能，必须从内核拷贝到Socket缓冲区。



# 原生NIO存在的问题

1. NIO的类库和API繁杂，使用麻烦，需要熟练掌握Selector/ServerSocketChannel/SocketChannel/ByteBuffer等。
1. 需要具备其他的额外技能，要熟悉Java多线程编程。
1. 开发工作量和难度都非常大，例如客户端面临断边重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等等。
1. JDK NIO的Bug，例如臭名昭著的Epoll Bug，它会导致Selector空轮询，最终导致CPU 100%。直到JDK1.7版本该问题仍旧
存在，没有根本解决。


# 线程模型

1. 目前存在的线程模型有：
    2. 传统阻塞IO服务模型
    2. Reactor模式
1. 根据Reactor的数量和处理资源池线程的数量不同，有3种典型的实现：
    2. 单Reactor单线程
    2. 单Reactor多线程
    2. 主从Reactor多线程
1. Netty线程模式，Netty主要基于主从Reactor多线程模型做了一定改进，其中主从Reactor多线程模型有多个Reactor。


## Reactor模式

1. IO复用结合线程池，就是Reactor模式基本设计思想。
1. Reactor模式，通过一个或多个输入同时传递给服务处理器的模式，并且是基于事件驱动的。

![](pictures/Reactor模式.png)

## Reactor模式中核心组成

1. Reactor:Reactor在一个单独的线程中运行，负责监听 和分发事件，分发给适当的处理程序来对IO事件做出反应。
1. Handlers：处理程序执行IO事件要完成的实际事件。Reactor通过调度适当的处理程序来响应IO事件，处理程序执行非阻塞操作。


## 单Reactor单线程

![](pictures/单Reactor单线程.jpg)

服务器端用一个线程通过多路复用搞定所有的IO操作，包括连接，读、写等，编码简单，清晰明了，但是如果客户端连接数量较多，将无法支撑。

### 优点
模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成

### 缺点

1. 性能问题，只有一个线程，无法完全发挥多核CPU的性能。Handler在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易
导致性能瓶颈。
1. 可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障。

### 使用场景

客户端的数量有限，业务处理非常快速，比如Redis在业务处理的时间复杂度O(1)的情况。

## 单Reactor多线程

![](pictures/单Reactor多线程.png)

1. 如果建立连接请求，则由Acceptor通过accept处理连接请求，然后创建一个Handler对象处理完成连接后的各种事件。
1. 如果不是连接请求，则由reactor分发调用连接对应的handler来处理。
1. handler只负责响应事件，不做具体的业务处理，通过read读取数据后，会分发给后面的worker线程池的某个线程处理业务。


### 优缺点

1. 可以充分的利用多核cpu的处理能力
1. 多线程数据共享和访问比较复杂，reactor处理所有的事件的监听和响应，在单线程运行，在高并发场景容易出现性能瓶颈。

# Netty模型

![](pictures/Netty%20Reactor工作架构图.jpeg)

1. Netty抽象出两组线程池，BossGroup专门负责接收客户端的连接，WorkerGroup专门负责网络的读写。
1. BossGroup和WorkerGroup类型都是NioEventLoopGroup。
1. NioEventLoopGroup相当于一个事件循环组，这个组中含有多个事件循环，每一个事件循环是NioEventLoop。
1. NioEventLoop表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个selector，用于监听
绑定在其上的socket的网络通讯。
1. NioEventLoopGroup可以有多个线程，即可以含有多个NioEventLoop。
1. 每个Boss NioEventLoop执行的步骤有3步：
    2. 轮询accept事件
    2. 处理accept事件，与client建立连接，生成NioSocketChannel，并将其注册到某个worker NioEventLoop上的
    selector
    2. 处理任务队列的任务，即runAllTasks
1. 每个Worker NioEventLoop循环执行的步骤：
    2. 轮询read/write事件 
    2. 处理IO事件，即read/write事件，在对应的NioSocketChannel处理
    2. 处理任务队列的任务，即runAllTasks。
1. 每个Worker NioEventLoop处理业务时，会使用pipeline，pipeline中包含了channel，即通过pipeline可以
获取到对应管理中的处理器。

