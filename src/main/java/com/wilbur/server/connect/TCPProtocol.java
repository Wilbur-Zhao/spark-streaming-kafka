package com.wilbur.server.connect;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * TCPProtocol 
 * @author Wang Zhao
 * @date 2017年7月5日 下午5:17:11
 *
 */
public interface TCPProtocol {

    public void handleAccept(SelectionKey key) throws IOException;
    
    public void handleRead(SelectionKey key) throws IOException;
    
    public void handleWrite(SelectionKey key) throws IOException;
    
}
