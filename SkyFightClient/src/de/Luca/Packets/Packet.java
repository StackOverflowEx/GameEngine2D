package de.Luca.Packets;

import org.json.JSONException;
import org.json.JSONObject;

public class Packet {
	
	public int packetType;	
	public Object a, b, c, d, e, f, g, h;
	
	public static final int HANDSHAKE = 0;
	public static final int ERROR = 1;
	public static final int REGISTRATION = 2;
	public static final int LOGIN = 3;
	public static final int PING = 4;
	
	public static final int ERROR_MISSING_HANDSHAKE = 0;
	
	public Packet() {}
	
	public Packet(String input) {
		init(input);
	}
	
	private void init(String input) {
		JSONObject obj = new JSONObject(input);
		packetType = obj.getInt("packetType");
		JSONObject fields = obj.getJSONObject("fields");
		try {
			a = fields.get("a");
			b = fields.get("b");
			c = fields.get("c");
			d = fields.get("d");
			e = fields.get("e");
			f = fields.get("f");
			g = fields.get("g");
			h = fields.get("h");
		}catch (JSONException e) {}
	}
	
	public String toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("packetType", packetType);
		JSONObject fields = new JSONObject();
		fields.put("a", a);
		fields.put("b", b);
		fields.put("c", c);
		fields.put("d", d);
		fields.put("e", e);
		fields.put("f", f);
		fields.put("g", g);
		fields.put("h", h);
		obj.put("fields", fields);
		return obj.toString();
	}

}
