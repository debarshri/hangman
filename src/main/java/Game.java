import model.Abstraction;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    public static void main(String[] args) throws JSONException, IOException, SQLException, ClassNotFoundException {

        for(int i = 0; i < 81; i++)
        start();
    }

    private static void start() throws JSONException, IOException {

        WordPreprocessor wordPreprocessor = new WordPreprocessor();
        wordPreprocessor.index(FileUtils.readLines(new File("test.txt")));

        List<Character> characters = new ArrayList<>();
        characters.addAll(wordPreprocessor.wordlist);

        Abstraction abstraction = new Abstraction();

        File token = new File("token");

        if(!token.exists())
        {
            JSONObject start = abstraction.start();
            System.out.println(start);

            String sessionId2 = start.getString("sessionId");
            FileUtils.writeStringToFile(token,sessionId2);
        }

        String sessionId1 = FileUtils.readFileToString(new File("token"));

        JSONObject sessionId = abstraction.nextWord(sessionId1);

        String wordToSolve = sessionId.getJSONObject("data").getString("word");

        System.out.println(sessionId);

        Character character = characters.get(0);

        int wrongGuessCountOfCurrentWord = 0;
        while (wrongGuessCountOfCurrentWord < 10 && wordToSolve.contains("*")) {

            System.out.println("Now guess.." + character);
            character = wordPreprocessor.computeGuess(wordToSolve,characters, wordPreprocessor.getModels());

            JSONObject jsonObject = abstraction.guessWord(sessionId1,
                    String.valueOf(character));

            JSONObject data = jsonObject.getJSONObject("data");
            wordToSolve = data.getString("word");

            wrongGuessCountOfCurrentWord = data.getInt("wrongGuessCountOfCurrentWord");

            System.out.println(jsonObject);

            characters.remove(character);
        }


        JSONObject result = abstraction.result(sessionId1);

        System.out.println(result);
    }
}
