package org.abstractica.deviceserver.basedeviceserver.packetserver.impl;

import org.abstractica.javablocks.blocks.udp.UDPSocketBlock;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DeviceBlockFactory;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketInfo;

import java.net.SocketException;
import java.net.UnknownHostException;

public class DeviceBlockFactoryImpl implements DeviceBlockFactory
{
    private static DeviceBlockFactory instance = null;

    public static DeviceBlockFactory getInstance()
    {
        if(instance == null)
        {
            instance = new DeviceBlockFactoryImpl();
        }
        return instance;
    }

    private DeviceBlockFactoryImpl() {}

    @Override
    public UDPSocketBlock<DevicePacketInfo> getDevicePacketSocket(int port, int maxPackageSize) throws SocketException, UnknownHostException
    {
        return new DevicePacketUDPSocketBlockImpl(port, maxPackageSize);
    }
}
