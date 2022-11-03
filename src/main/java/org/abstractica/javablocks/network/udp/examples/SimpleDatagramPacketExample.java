package org.abstractica.javablocks.network.udp.examples;


import org.abstractica.javablocks.basic.*;
import org.abstractica.javablocks.basic.impl.BasicBlockFactoryImpl;
import org.abstractica.javablocks.network.udp.UDPBlockFactory;
import org.abstractica.javablocks.network.udp.impl.UDPBlockFactoryImpl;

import java.net.DatagramPacket;

public class SimpleDatagramPacketExample
{
	public static void main(String[] args)
	{
		BasicBlockFactory basicFactory = BasicBlockFactoryImpl.getInstance();
		UDPBlockFactory udpFactory = UDPBlockFactoryImpl.getInstance();
		Input<String> keyboard = basicFactory.getKeyboardBlock();
		Output<String> console = basicFactory.getConsoleBlock();
		ThreadBlock thread = basicFactory.getThreadBlock();
		PullBlock<String, DatagramPacket> stringToDatagramPacket = basicFactory.getPullBlock(udpFactory.getStringToDatagramPacketFunction());
		PushBlock<DatagramPacket, String> datagramPacketToString = basicFactory.getPushBlock(udpFactory.getDatagramPacketToStringFunction());

		//Hook it up
		stringToDatagramPacket.setInput(keyboard);
		thread.setInput(stringToDatagramPacket);
		thread.setOutput(datagramPacketToString);
		datagramPacketToString.setOutput(console);

		//Start the thread
		thread.start();
	}
}
