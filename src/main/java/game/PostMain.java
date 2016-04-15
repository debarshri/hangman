package game;

import model.GameHasEndedException;
import model.RestClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;

public class PostMain {
    public static void main(String[] args) throws JSONException, IOException, GameHasEndedException {

        RestClient restClient = new RestClient();

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("sessionId","0a9e88108ad90ad7afb34fee04c37e03");
        jsonObject.put("action","submitResult");

        String post = restClient.post(jsonObject);

        System.out.println(post);
    }
}
