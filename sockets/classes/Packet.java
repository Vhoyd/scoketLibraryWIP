package sockets.classes;

import java.util.ArrayList;
import java.util.List;

public class Packet {
	private final Double[] doubles;
	private final Integer[] ints;
	private final Byte[] bytes;
	private final String[] strings;
	private final Long[] longs;
	private final Float[] floats;
	private final Character[] chars;
	private final Short[] shorts;
	private final Boolean[] booleans;
	private final String name;
	private final String build;
	
	public Packet(String name, Double[] doubles, Integer[] ints, Byte[] bytes, String[] strings, Long[] longs, Float[] floats,
			Character[] chars, Short[] shorts, Boolean[] booleans) {
		this.name = name;
		this.doubles = doubles;
		this.ints = ints;
		this.bytes = bytes;
		this.strings = strings;
		this.longs = longs;
		this.floats = floats;
		this.chars = chars;
		this.shorts = shorts;
		this.booleans = booleans;
		StringBuilder assemble = new StringBuilder(name);
		assemble.append('\u001c');
		for (int i = 0; i < doubles.length; i++) assemble.append(doubles[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < ints.length; i++) assemble.append(ints[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < bytes.length; i++) assemble.append(bytes[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < strings.length; i++) assemble.append(strings[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < longs.length; i++) assemble.append(longs[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < floats.length; i++) assemble.append(floats[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < chars.length; i++) assemble.append(chars[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < shorts.length; i++) assemble.append(shorts[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		for (int i = 0; i < booleans.length; i++) assemble.append(booleans[i]); assemble.append('\u001b');
		assemble.append('\u001c');
		build = assemble.toString();
	}
	
	public Double[] getDoubles() {
		return doubles;
	}

	public Integer[] getInts() {
		return ints;
	}

	public Byte[] getBytes() {
		return bytes;
	}

	public String[] getStrings() {
		return strings;
	}

	public Long[] getLongs() {
		return longs;
	}

	public Float[] getFloats() {
		return floats;
	}

	public Character[] getChars() {
		return chars;
	}

	public Short[] getShorts() {
		return shorts;
	}

	public Boolean[] getBooleans() {
		return booleans;
	}
	
	public double getDouble() {
		return doubles[0];
	}
	
	public int getInt() {
		return ints[0];
	}
	
	public byte getByte() {
		return bytes[0];
	}
	
	public String getString() {
		return strings[0];
	}
	
	public long getLong() {
		return longs[0];
	}
	
	public float getFloat() {
		return floats[0];
	}
	
	public char getChar() {
		return chars[0];
	}
	
	public short getShort() {
		return shorts[0];
	}
	
	public boolean getBoolean() {
		return booleans[0];
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return build;
	}
	
	private static enum Type {
		DOUBLE(0),
		INT(1),
		BYTE(2),
		STRING(3),
		LONG(4),
		FLOAT(5),
		CHAR(6),
		SHORT(7),
		BOOLEAN(8),
		;
		private int value;
		
		private Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static Type assess(int value) {
			return Type.values()[value];
		}
	}
	
	public static Packet[] getPacketsFromString(String string) {
		List<String> map = new ArrayList<String>();
		List<Type> typeMap = new ArrayList<Type>();
		List<Packet> packets = new ArrayList<Packet>();
		StringBuilder builder = new StringBuilder();
		int type = 0;
		boolean nameFound = false;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\u001b' || string.charAt(i) == '\u001c') {
				map.add(builder.toString());
				typeMap.add(Type.assess(type));
				builder = new StringBuilder();
				if (string.charAt(i) == '\u001c') {
					if (nameFound) {
						type++;						
					} else {
						nameFound = true;
					}
				}
			} else {
				builder.append(string.charAt(i));
			}
			if (type == 9) {
				if (builder.length() != 0) map.add(builder.toString());	
				type = 0;
				nameFound = false;
				packets.add(parseData(map, typeMap));
				map.clear();
				typeMap.clear();
			}
		}
		return packets.toArray(new Packet[0]);
	}
	
	private static Packet parseData(List<String> map, List<Type> typeMap) {
		List<Double> doubles = new ArrayList<Double>();
		List<Integer> ints = new ArrayList<Integer>();
		List<Byte> bytes = new ArrayList<Byte>();
		List<String> strings = new ArrayList<String>();
		List<Long> longs = new ArrayList<Long>();
		List<Float> floats = new ArrayList<Float>();
		List<Character> chars = new ArrayList<Character>();
		List<Short> shorts = new ArrayList<Short>();
		List<Boolean> booleans = new ArrayList<Boolean>();
		for (int i = 1; i < map.size(); i++) {
			String s = map.get(i);
			if (s.length() == 0) continue;
			int value = typeMap.get(i).getValue();
			switch (value) {
			case 0:
				doubles.add(Double.parseDouble(s));
				break;
			case 1:
				ints.add(Integer.parseInt(s));
				break;
			case 2:
				bytes.add(Byte.parseByte(s));
				break;
			case 3:
				strings.add(s);
				break;
			case 4:
				longs.add(Long.parseLong(s));
				break;
			case 5:
				floats.add(Float.parseFloat(s));
				break;
			case 6:
				chars.add(s.charAt(0));
				break;
			case 7:
				shorts.add(Short.parseShort(s));
				break;
			case 8:
				booleans.add(Boolean.parseBoolean(s));
				break;
			}
		}
		return new Packet(
				map.get(0),
				doubles.toArray(new Double[0]),
				ints.toArray(new Integer[0]),
				bytes.toArray(new Byte[0]),
				strings.toArray(new String[0]),
				longs.toArray(new Long[0]),
				floats.toArray(new Float[0]),
				chars.toArray(new Character[0]),
				shorts.toArray(new Short[0]),
				booleans.toArray(new Boolean[0])
				);
	}
	
	public static class PacketBuilder {
		private Double[] doubles = new Double[0];
		private Integer[] ints = new Integer[0];
		private Byte[] bytes = new Byte[0];
		private String[] strings = new String[0];
		private Long[] longs = new Long[0];
		private Float[] floats = new Float[0];
		private Character[] chars = new Character[0];
		private Short[] shorts = new Short[0];
		private Boolean[] booleans = new Boolean[0];
		private final String name;
		
		public PacketBuilder(String name) {
			this.name = name;
		}
		
		public PacketBuilder Doubles(Double[] doubles) {
			this.doubles = doubles;
			return this;
		}
		
		public PacketBuilder Ints(Integer[] ints) {
			this.ints = ints;
			return this;
		}
		
		public PacketBuilder Bytes(Byte[] bytes) {
			this.bytes = bytes;
			return this;
		}
		
		public PacketBuilder Strings(String[] strings) {
			this.strings = strings;
			return this;
		}
		
		public PacketBuilder Longs(Long[] longs) {
			this.longs = longs;
			return this;
		}
		
		public PacketBuilder Floats(Float[] floats) {
			this.floats = floats;
			return this;
		}
		
		public PacketBuilder Chars(Character[] chars) {
			this.chars = chars;
			return this;
		}
		
		public PacketBuilder Shorts(Short[] shorts) {
			this.shorts = shorts;
			return this;
		}
		
		public PacketBuilder Booleans(Boolean[] booleans) {
			this.booleans = booleans;
			return this;
		} 
		
		public PacketBuilder Double(double d) {
			this.doubles = new Double[1];
			doubles[0] = d;
			return this;
		}
		
		public PacketBuilder Int(int i) {
			this.ints = new Integer[1];
			ints[0] = i;			
			return this;
		}
		
		public PacketBuilder Byte(byte b) {
			this.bytes = new Byte[1];
			bytes[0] = b;
			return this;
		}
		
		public PacketBuilder String(String s) {
			this.strings = new String[1];
			strings[0] = s;
			return this;
		}
		
		public PacketBuilder Long(long l) {
			this.longs = new Long[1];
			longs[0] = l;
			return this;
		}
		
		public PacketBuilder Float(float f) {
			this.floats = new Float[1];
			floats[0] = f;
			return this;
		}
		
		public PacketBuilder Char(char c) {
			this.chars = new Character[1];
			chars[0] = c;
			return this;
		}
		
		public PacketBuilder Short(short s) {
			this.shorts = new Short[1];
			shorts[0] = s;
			return this;
		}
		
		public PacketBuilder Boolean(boolean b) {
			this.booleans = new Boolean[1];
			booleans[0] = b;
			return this;
		}
		
		public Packet Build() {
			return new Packet(name, doubles, ints, bytes, strings, longs, floats, chars, shorts, booleans);
		}
		
	}
}
