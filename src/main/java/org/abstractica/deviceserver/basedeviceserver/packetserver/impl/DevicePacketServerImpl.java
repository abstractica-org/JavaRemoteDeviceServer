package org.abstractica.deviceserver.basedeviceserver.packetserver.impl;



import org.abstractica.deviceserver.basedeviceserver.packetserver.DeviceBlockFactory;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketInfo;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketServer;
import org.abstractica.javablocks.blocks.basic.*;
import org.abstractica.javablocks.blocks.udp.UDPSocketBlock;

import java.net.SocketException;
import java.net.UnknownHostException;

public class DevicePacketServerImpl implements DevicePacketServer
{
    private Output<DevicePacketInfo> sendConnection;
    private Input<DevicePacketInfo> receiveConnection;
    private ThreadControl receiveCtrl;
    private ThreadControl sendCtrl;

    public DevicePacketServerImpl(int port, int maxPacketSize, int bufferSize) throws SocketException, UnknownHostException
    {
        DeviceBlockFactory remoteDeviceFactory = DeviceBlockFactoryImpl.getInstance();
        UDPSocketBlock<DevicePacketInfo> socket = remoteDeviceFactory.getDevicePacketSocket(port, maxPacketSize);
        ThreadBlock<DevicePacketInfo> receiveThread = BasicBlocks.getThreadBlock();
        BufferBlock<DevicePacketInfo> receiveBuffer = BasicBlocks.getBufferBlock(bufferSize);
        ThreadBlock<DevicePacketInfo> sendThread = BasicBlocks.getThreadBlock();
        BufferBlock<DevicePacketInfo> sendBuffer = BasicBlocks.getBufferBlock(bufferSize);
        receiveThread.setDebugReporter(null);
        sendThread.setDebugReporter(null);

        //Hookup
        receiveThread.setInput(socket);
        receiveThread.setOutput(receiveBuffer);
        receiveConnection = receiveBuffer;

        sendConnection = sendBuffer;
        sendThread.setInput(sendBuffer);
        sendThread.setOutput(socket);

        receiveCtrl = receiveThread;
        sendCtrl = sendThread;
    }

    @Override
    public void start()
    {
        sendCtrl.start();
        receiveCtrl.start();
    }

    @Override
    public void stop() throws InterruptedException
    {
        receiveCtrl.stop();
        sendCtrl.stop();
    }

    @Override
    public boolean isRunning()
    {
        return sendCtrl.isRunning();
    }

    @Override
    public DevicePacketInfo get() throws Exception
    {
        return receiveConnection.get();
    }

    @Override
    public void put(DevicePacketInfo packet) throws Exception
    {
        sendConnection.put(packet);
    }
}
