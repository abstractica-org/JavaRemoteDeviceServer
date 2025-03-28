package org.abstractica.deviceserver.examples;

import org.abstractica.deviceserver.*;
import org.abstractica.deviceserver.impl.DeviceServerImpl;

import java.net.SocketException;
import java.net.UnknownHostException;

public class DeviceServerExample implements DeviceServerListener, DevicePacketHandler, DeviceConnectionListener
{
	public static void main(String[] args) throws Exception
	{
		(new DeviceServerExample()).runExample();
	}

	public void runExample() throws Exception
	{
		System.out.println("Creating server...");
		DeviceServer server = new DeviceServerImpl(3377, 0, 1024,10,1000, this);
		System.out.println("Creating device...");
		Device myDevice = server.createDevice(10300466, "Locomotive", 1);
		System.out.println("Adding listener...");
		myDevice.addConnectionListener(this);
		System.out.println("Adding device to server...");
		server.addDevice(myDevice);
		System.out.println("Starting server...");
		server.start();
		System.out.println("Waiting for connection...");
		myDevice.waitForConnection();
		//server.waitForAllDevicesToConnect();
		System.out.println("Device is connected!");
		while(true)
		{
			Thread.sleep(5000);
			System.out.println("Set power: 255");
			Response response = myDevice.sendPacket(
					1003,
					255, 0, 0, 0,
					null, false, true);
			Thread.sleep(10000);
			System.out.println("Set power: 255");
			response = myDevice.sendPacket(
					1003,
					0, 0, 0, 0,
					null, false, true);
		}
	}

	@Override
	public boolean acceptAndInitializeNewDevice(Device device)
	{
		System.out.println("ServerListener: acceptAndInitializeNewDevice -> " + device);
		if(!device.getDeviceType().equals("Locomotive") || !(device.getDeviceVersion() == 1))
		{
			return false;
		}
		device.setPacketHandler(this);
		return true;
	}

	@Override
	public void onDeviceAdded(Device device)
	{
		System.out.println("ServerListener: onDeviceAdded -> " + device);
	}

	@Override
	public void onDeviceRemoved(Device device)
	{
		System.out.println("ServerListener: onDeviceRemoved -> " + device);
	}

	@Override
	public int onPacket(int command, int arg1, int arg2, int arg3, int arg4, byte[] load)
	{
		System.out.print("DevicePacketHandler: onPacket -> ");
		System.out.println("(Command: " + command +
				", arg1: " + arg1 +
				", arg2: " + arg2 +
				", arg3: " + arg3 +
				", arg4: " + arg4 + ")");
		return 0;
	}

	@Override
	public void onCreated()
	{
		System.out.println("DeviceConnectionListener: onCreated()");
	}

	@Override
	public void onConnected()
	{
		System.out.println("DeviceConnectionListener: onConnected()");
	}

	@Override
	public void onDisconnected()
	{
		System.out.println("DeviceConnectionListener: onDisconnected()");
	}

	@Override
	public void onLost()
	{
		System.out.println("DeviceConnectionListener: onLost()");
	}

	@Override
	public void onDestroyed()
	{
		System.out.println("DeviceConnectionListener: onDestroyed()");
	}
}
