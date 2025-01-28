package org.abstractica.deviceserver.basedeviceserver.packetserver;

public interface DevicePacket
{
    long getDeviceId();
    int getCommand();
    int getArg1();
    int getArg2();
    int getArg3();
    int getArg4();
    boolean hasLoad();
    byte[] getLoad();
}
