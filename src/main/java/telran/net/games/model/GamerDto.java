package telran.net.games.model;

import org.json.JSONObject;

import java.time.LocalDate;

public record GamerDto(String username, LocalDate birthdate) {
    private static final String USERNAME_FIELD = "username";
    private static final String BIRTHDATE_FIELD = "birthdate";

    @Override
    public    String toString(){
        JSONObject jo = new JSONObject();
        jo.put(USERNAME_FIELD,username);
        jo.put(BIRTHDATE_FIELD,birthdate);
        return jo.toString();
    }
    public GamerDto(JSONObject jsonObject ) {
        this(jsonObject.getString(USERNAME_FIELD),
                LocalDate.parse(LocalDate.parse(jsonObject.getString(BIRTHDATE_FIELD)).toString()));
    }
}
