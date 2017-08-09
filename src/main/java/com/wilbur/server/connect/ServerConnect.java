package com.wilbur.server.connect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * ServerConnect 
 * @author Wang Zhao
 * @date 2017年7月5日 下午4:53:23
 *
 */
public class ServerConnect {

    protected static final int port = 9999;
    
    protected static final int TIME_OUT = 2000;
    
    protected static final int BUF_SIZE = 1024;
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        TCPProtocol protocol = new EchoSelectorProtocol(BUF_SIZE);
        
        while(true){
            if(selector.select(TIME_OUT) == 0){
                System.out.println("time out");
                continue;
            }
            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
            while(keyIter.hasNext()){
                SelectionKey key = keyIter.next();
                if(key.isAcceptable()){
                    System.out.println("come in ......");
                    protocol.handleAccept(key);
                }
                keyIter.remove();
            }
        }

    }

}
