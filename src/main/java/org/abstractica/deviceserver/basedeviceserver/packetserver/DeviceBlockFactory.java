package org.abstractica.deviceserver.basedeviceserver.packetserver;

import org.abstractica.javablocks.blocks.udp.UDPSocketBlock;

import java.net.SocketException;
import java.net.UnknownHostException;


public interface DeviceBlockFactory
{
    UDPSocketBlock<DevicePacketInfo> getDevicePacketSocket(int port, int maxPacketSize) throws SocketException, UnknownHostException;
}
