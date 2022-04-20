package sockets.classes;

import java.util.ArrayList;
import java.util.List;

public class PacketEventManager {
	
	private List<EventHook> hooks = new ArrayList<EventHook>();
	
	PacketEventManager() {}

	public void executePacket(Packet packet) {
		for (EventHook hook : hooks) {
			if (hook.isListeningFor(packet.getName())) {
				hook.execute(packet);
			}
		}
	}
	
	public void executePackets(Packet...packets) {
		for (Packet packet : packets) executePacket(packet);
	}
	
	void executePackets(String packetData) {
		Packet[] packets = Packet.getPacketsFromString(packetData);
		for (Packet packet : packets) executePacket(packet);
	}
	
	public void addEventHook(EventHook hook) {
		hooks.add(hook);
	}
	
	public boolean contains(EventHook hook) {
		return hooks.contains(hook);
	}
	
	public void removeHook(EventHook hook) {
		hooks.remove(hook);
	}
}
