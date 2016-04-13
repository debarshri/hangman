package model;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;

public class RestClient {

    private static final String URL="https://strikingly-hangman.herokuapp.com/game/on";

    public String post(JSONObject body) throws IOException {
       return Request.Post(URL).bodyString(body.toString(), ContentType.APPLICATION_JSON).execute().returnContent().asString();
    }

}
