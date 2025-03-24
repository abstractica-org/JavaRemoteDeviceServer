package org.abstractica.deviceserver.basedeviceserver.impl;

import org.abstractica.javablocks.blocks.basic.BasicBlocks;
import org.abstractica.javablocks.blocks.basic.ThreadBlock;
import org.abstractica.javablocks.blocks.basic.ThreadControl;
import org.abstractica.deviceserver.basedeviceserver.BaseDeviceServerPacketSendCallback;
import org.abstractica.deviceserver.basedeviceserver.BaseDeviceServer;
import org.abstractica.deviceserver.basedeviceserver.BaseDeviceServerListener;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketInfo;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketServer;
import org.abstractica.deviceserver.basedeviceserver.packetserver.impl.DevicePacketServerImpl;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class BaseDeviceServerImpl implements BaseDeviceServer, Runnable
{
    private final DevicePacketServer packetServer;
    private final BaseDeviceConnectionHandlerImpl connectionHandler;
    private final ThreadControl threadControl;
    private final long updateInterval;
    private final int port;
    private final InetAddress address;
    //private RemoteDeviceServerListener listener;
    private Thread alivenessThread;
    private volatile boolean running;

    public BaseDeviceServerImpl(int port,
                                int serverId,
                                int maxPacketSize,
                                int bufferSize,
                                long updateInterval,
                                BaseDeviceServerListener listener) throws SocketException, UnknownHostException
    {
        this.packetServer = new DevicePacketServerImpl(port, maxPacketSize, bufferSize);
        this.connectionHandler = new BaseDeviceConnectionHandlerImpl(packetServer, listener, serverId);
        ThreadBlock<DevicePacketInfo> threadBlock = BasicBlocks.getThreadBlock();
        threadBlock.setDebugReporter(null);
        threadBlock.setInput(packetServer);
        threadBlock.setOutput(connectionHandler);
        threadControl = threadBlock;
        this.updateInterval = updateInterval;
        this.address = InetAddress.getLocalHost();
        this.port = port;
        this.running = false;
    }

    @Override
    public boolean readyToSendPacket(long deviceId)
    {
        return connectionHandler.readyToSendPacket(deviceId);
    }

    @Override
    public int sendPacket( long deviceId,
                           int command,
                           int arg1,
                           int arg2,
                           int arg3,
                           int arg4,
                           byte[] load,
                           boolean blocking,
                           boolean forceSend,
                           BaseDeviceServerPacketSendCallback callback) throws InterruptedException
    {
        return connectionHandler.sendPacket(deviceId, command, arg1, arg2, arg3, arg4, load, blocking, forceSend, callback);
    }

    @Override
    public void removeDevice(long deviceId)
    {
        connectionHandler.removeDevice(deviceId);
    }

    @Override
    public long[] getAllDeviceIds()
    {
        return connectionHandler.getAllDeviceIds();
    }

    @Override
    public void start()
    {
        if (!running)
        {
            running = true;
            threadControl.start();
            packetServer.start();
            alivenessThread = new Thread(this);
            alivenessThread.start();
            System.out.println(this.getClass().getSimpleName() + " -> Remote device server started on: " + address + ":" + port);
        }
    }

    @Override
    public void stop() throws InterruptedException
    {
        if (running)
        {
            running = false;
            synchronized(this)
            {
                alivenessThread.interrupt();
                packetServer.stop();
                threadControl.stop();
            }
            alivenessThread.join();
            System.out.println("Remote device server stopped gracefully.");
        }
    }



    @Override
    public boolean isRunning()
    {
        return running;
    }

    @Override
    public void run()
    {
        while (running)
        {
            try
            {
                connectionHandler.updateAliveness();
                wait(updateInterval);
            } catch (Exception e)
            {

            }
        }
    }
}
