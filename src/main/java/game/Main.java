package game;

import model.ClientCommunicator;
import model.GameHasEndedException;
import org.codehaus.jettison.json.JSONException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, JSONException, GameHasEndedException {
        Player player = new Player(new Game(new ClientCommunicator()));
        player.init();
        player.start();
    }
}
