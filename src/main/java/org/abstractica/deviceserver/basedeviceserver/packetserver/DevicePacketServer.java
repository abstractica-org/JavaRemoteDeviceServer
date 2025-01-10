package org.abstractica.deviceserver.basedeviceserver.packetserver;

import org.abstractica.javablocks.blocks.basic.Input;
import org.abstractica.javablocks.blocks.basic.Output;
import org.abstractica.javablocks.blocks.basic.ThreadControl;

public interface DevicePacketServer extends Output<DevicePacketInfo>, Input<DevicePacketInfo>, ThreadControl
{
}
