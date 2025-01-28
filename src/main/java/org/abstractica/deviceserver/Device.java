package org.abstractica.deviceserver;

public interface Device
{
	long getDeviceId();
	String getDeviceType();
	long getDeviceVersion();
	void setPacketHandler(DevicePacketHandler packetHandler);
	boolean addConnectionListener(DeviceConnectionListener listener);
	boolean removeConnectionListener(DeviceConnectionListener listener);
	boolean isConnected();
	void waitForConnection() throws InterruptedException;
	Response sendPacket(int command,
	                    int arg1,
	                    int arg2,
						int arg3,
						int arg4,
	                    byte[] packet,
	                    boolean blocking,
	                    boolean forceSend) throws InterruptedException;
	default int sendPacketAndWait(int command,
	                      int arg1,
	                      int arg2,
						  int arg3,
						  int arg4,
	                      byte[] packet,
	                      boolean blocking,
	                      boolean forceSend) throws InterruptedException
	{
		Response response = sendPacket(command, arg1, arg2, arg3, arg4, packet, blocking, forceSend);
		return response.getResponse();
	}
}
