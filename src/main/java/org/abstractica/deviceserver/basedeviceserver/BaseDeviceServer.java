package org.abstractica.deviceserver.basedeviceserver;
import org.abstractica.javablocks.blocks.basic.ThreadControl;

public interface BaseDeviceServer extends ThreadControl
{
    long[] getAllDeviceIds();
    boolean readyToSendPacket(long deviceId);
    int sendPacket(   long deviceId,
                      int command,
                      int arg1,
                      int arg2,
                      byte[] packet,
                      boolean blocking,
                      boolean forceSend,
                      BaseDeviceServerPacketSendCallback callback   ) throws Exception;
    void removeDevice(long deviceId);
}
