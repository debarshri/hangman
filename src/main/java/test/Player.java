package test;

import model.ClientCommunicator;
import org.codehaus.jettison.json.JSONException;

import java.io.IOException;

public class Player {
    private Game game;

    public Player(Game game) {
        this.game = game;
    }

    public void start() throws IOException, JSONException {
        for(int i = 0; i < 81; i++)
        {
            game.start();
        }

    }
}
