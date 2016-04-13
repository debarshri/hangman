package model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by dbasak on 4/12/16.
 */
public class Abstraction {

    private RestClient restClient = new RestClient();


    public JSONObject start() throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put( "playerId","debarshri@gmail.com").put("action", "startGame");

       return new JSONObject(restClient.post(jsonObject));

    }

    public JSONObject nextWord(String sessionId) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId",sessionId).put("action","nextWord");

        return new JSONObject(restClient.post(jsonObject));
    }

    public JSONObject guessWord(String sessionId, String a) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", sessionId).put("action","guessWord").put("guess",a);

        return new JSONObject(restClient.post(jsonObject));
    }

    public JSONObject result(String sessionId) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("sessionId",sessionId).put("action","getResult");
        return new JSONObject(restClient.post(jsonObject));
    }

}
