package de.Luca.Packets;

import org.json.JSONException;
import org.json.JSONObject;

public class Packet {
	
	public int packetType;	
	public Object a, b, c, d, e, f, g, h;
	
	public static final int HANDSHAKE = 0;
	public static final int ERROR = 1;
	public static final int SUCCESS = 2;
	public static final int REGISTRATION = 4;
	public static final int LOGIN = 5;
	public static final int PING = 6;
	public static final int KEY = 7;
	public static final int ID = 8;
	public static final int SEARCHING = 9;
	public static final int MATCH_FOUND = 10;
	public static final int MATCH_CANCELLED = 11;
	public static final int CONNECT = 12;
	public static final int PASSWORD_RESET = 13;
	
	public static final int DEMON_HANDSHAKE = 0;
	public static final int DEMON_ERROR = 1;
	public static final int DEMON_SUCCESS = 2;
	public static final int DEMON_CREATE_SERVER = 3;
	public static final int DEMON_INFO = 4;
	public static final int DEMON_KEY = 5;
	public static final int DEMON_SERVER_CREATED = 6;
	public static final int DEMON_STOP_SERVER = 6;
	
	public static final int ERROR_MISSING_HANDSHAKE = 1;
	public static final int ERROR_SERVER = 2;
	public static final int ERROR_USERNAME_EXISTS = 3;
	public static final int ERROR_EMAIL_EXISTS = 4;
	public static final int ERROR_NO_USERNAME_EMAIL = 5;
	public static final int ERROR_WRONG_PASSWORD = 6;
	public static final int ERROR_COULD_NOT_CREATE_SERVER = 7;
	public static final int ERROR_MATCHES_NOT_AVALIABLE = 8;
	public static final int ERROR_PLAYER_QUIT = 9;
	public static final int ERROR_COULD_NOT_RESET_PASSWORD = 10;
	
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
