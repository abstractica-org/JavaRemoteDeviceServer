package org.abstractica.javablocks.network;


import org.abstractica.javablocks.basic.Input;
import org.abstractica.javablocks.basic.Output;

import java.net.InetAddress;

public interface SocketBlock<E> extends Input<E>, Output<E>
{
    public int getPort();
    public InetAddress getAddress();
}
