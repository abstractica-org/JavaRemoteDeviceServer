package org.abstractica.deviceserver.basedeviceserver.impl;


import org.abstractica.javablocks.blocks.basic.Output;
import org.abstractica.deviceserver.basedeviceserver.BaseDeviceServerListener;
import org.abstractica.deviceserver.basedeviceserver.BaseDeviceServerPacketSendCallback;
import org.abstractica.deviceserver.basedeviceserver.packetserver.DevicePacketInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeviceConnectionHandlerImpl implements Output<DevicePacketInfo>
{
    private final Map<Long, BaseDeviceConnectionImpl> map;
    private final BaseDeviceServerListener listener;
    private final Output<DevicePacketInfo> packageSender;
    private final int serverId;

    public BaseDeviceConnectionHandlerImpl(Output<DevicePacketInfo> packageSender,
                                           BaseDeviceServerListener listener,
                                           int serverId)
    {
        this.packageSender = packageSender;
        this.listener = listener;
        this.serverId = serverId;
        this.map = new HashMap<>();
    }

    @Override
    public void put(DevicePacketInfo packet) throws InterruptedException
    {
        long curTime = System.currentTimeMillis();
        Long id = packet.getDeviceId();
        BaseDeviceConnectionImpl deviceConnection;
        synchronized (map)
        {
            deviceConnection = map.get(id);
        }
        if (deviceConnection == null)
        {
            deviceConnection = new BaseDeviceConnectionImpl(packageSender,listener,id);
            if(deviceConnection.onPacket(curTime, packet))
            {
                synchronized (map)
                {
                    map.put(id, deviceConnection);
                }
            }
        }
        else if(!deviceConnection.onPacket(curTime, packet))
        {
            synchronized (map)
            {
                map.remove(id);
            }
        }
    }

    public int sendPacket(long deviceId, int command, int arg1, int arg2, int arg3, int arg4, byte[] load, boolean blocking, boolean forceSend, BaseDeviceServerPacketSendCallback callback) throws InterruptedException
    {
        long curTime = System.currentTimeMillis();
        BaseDeviceConnectionImpl deviceConnection;
        synchronized (map)
        {
            deviceConnection = map.get(deviceId);
        }
        if(deviceConnection == null)
        {
            return -1;
        }
        return deviceConnection.sendPacket(curTime, command, arg1, arg2, arg3, arg4, load, blocking, forceSend, callback);
    }

    public int getCurrentMsgId(long deviceId)
    {
        BaseDeviceConnectionImpl deviceConnection = null;
        synchronized (map)
        {
            deviceConnection = map.get(deviceId);
        }
        if(deviceConnection == null)
        {
            return -1;
        }
        return deviceConnection.getCurrentMsgId();
    }

    public boolean readyToSendPacket(long deviceId)
    {
        BaseDeviceConnectionImpl deviceConnection = null;
        synchronized (map)
        {
            deviceConnection = map.get(deviceId);
        }
        if(deviceConnection == null)
        {
            return false;
        }
        return deviceConnection.readyToSendPacket();
    }

    public long[] getAllDeviceIds()
    {
        synchronized (map)
        {
            long[] res = new long[map.size()];
            int i = 0;
            for (BaseDeviceConnectionImpl con : map.values())
            {
                res[i++] = con.getDeviceId();
            }
            return res;
        }
    }

    public void updateAliveness() throws Exception
    {
        long curTime = System.currentTimeMillis();
        List<Long> doomedDevices = new ArrayList<>();
        synchronized (map)
        {
            for (BaseDeviceConnectionImpl deviceConnection : map.values())
            {
                if (!deviceConnection.updateAliveness(curTime))
                {
                    doomedDevices.add(deviceConnection.getDeviceId());
                }
            }
            for (Long id : doomedDevices)
            {
                map.remove(id);
            }
        }
    }

	public void removeDevice(long deviceId)
	{
        BaseDeviceConnectionImpl con = null;
        synchronized (map)
        {
            con = map.get(deviceId);
            if (con != null)
            {
                map.remove(deviceId);
            }
        }
        if (con != null)
        {
            con.removeDevice();
        }
	}
}
