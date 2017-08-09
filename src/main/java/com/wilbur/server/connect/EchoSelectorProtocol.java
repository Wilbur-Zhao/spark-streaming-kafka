package com.wilbur.server.connect;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * EchoSelectorProtocol 
 * @author Wang Zhao
 * @date 2017年7月5日 下午5:19:58
 *
 */
public class EchoSelectorProtocol implements TCPProtocol {

    private int bufferSize;
    
    public EchoSelectorProtocol(int bufferSize){
        this.bufferSize = bufferSize;
    }
    
    /* (non-Javadoc)
     * @see com.wilbur.server.connect.TCPProtocol#handleAccept(java.nio.channels.SelectionKey)
     */
    @Override
    public void handleAccept(SelectionKey key) throws IOException  {
       System.out.println("Selecttion Key Accept");
       SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
       socketChannel.configureBlocking(false);
       socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
       socketChannel.close();
    }

    /* (non-Javadoc)
     * @see com.wilbur.server.connect.TCPProtocol#handleRead(java.nio.channels.SelectionKey)
     */
    @Override
    public void handleRead(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.wilbur.server.connect.TCPProtocol#handleWrite(java.nio.channels.SelectionKey)
     */
    @Override
    public void handleWrite(SelectionKey key) throws IOException{
        // TODO Auto-generated method stub

    }

}
