package game;

import org.codehaus.jettison.json.JSONException;

import java.io.File;
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

    public void init() {
        File file = new File("token");
        if(file.exists())
        {
            file.delete();
        }
    }
}
