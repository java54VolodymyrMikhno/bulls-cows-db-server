package telran.net.games.model;

import org.json.JSONObject;

public record MoveData(String sequence,Integer bulls,Integer cows) {
private static final String SEQUENCE_FIELD = "sequence"; 
private static final String BULLS_FIELD = "bulls";
private static final String COWS_FIELD = "cows";
@Override
public String toString() {
	return "MoveData [sequence=" + sequence + ", bulls=" + bulls + ", cows=" + cows + "]";
}

public MoveData(JSONObject jsonObject) {
	this(jsonObject.getString(SEQUENCE_FIELD),
			jsonObject.getInt(BULLS_FIELD),jsonObject.getInt(COWS_FIELD));
}

}
