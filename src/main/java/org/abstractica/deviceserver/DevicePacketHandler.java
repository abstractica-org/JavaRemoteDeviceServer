package org.abstractica.deviceserver;

public interface DevicePacketHandler
{
	int onPacket(int command, int arg1, int arg2, int arg3, int arg4, byte[] load);
}
