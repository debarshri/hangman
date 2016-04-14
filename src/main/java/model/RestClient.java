package model;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;

public class RestClient {

    private static final String URL = "https://strikingly-hangman.herokuapp.com/game/on";

    public String post(JSONObject body) throws IOException, GameHasEndedException {
        Response execute = Request.Post(URL).bodyString(body.toString(), ContentType.APPLICATION_JSON).execute();

        try {
            HttpResponse httpResponse = execute.returnResponse();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                System.out.println("Game has ended..");
                throw new GameHasEndedException();
            }
        } catch (NoHttpResponseException e) {
            System.out.println("Game has ended due to no response");
            throw new GameHasEndedException();
        }

    }

}
