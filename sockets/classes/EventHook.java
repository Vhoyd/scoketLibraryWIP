package sockets.classes;

public abstract class EventHook {
	private String name;
	
	public EventHook (String packetName) {
		name = packetName;
	}
	
	public boolean isListeningFor(String packetName) {
		return packetName.equals(name);
	}
	
	abstract public void execute(Packet packet);
}
