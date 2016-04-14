package test;

import model.ClientCommunicator;
import model.GameHasEndedException;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private ClientCommunicator clientCommunicator;

    public Game(ClientCommunicator clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }

    public void start() throws JSONException, IOException {

        try {

            WordPreprocessor wordPreprocessor = new WordPreprocessor();
            wordPreprocessor.index(FileUtils.readLines(new File("test.txt")));

            List<Character> characters = new ArrayList<>();
            characters.addAll(wordPreprocessor.wordlist);

            File token = new File("token");

            if (!token.exists()) {
                JSONObject start = null;
                start = clientCommunicator.start();

                System.out.println(start);

                String sessionId2 = start.getString("sessionId");
                FileUtils.writeStringToFile(token, sessionId2);
            }

            String sessionId1 = FileUtils.readFileToString(new File("token"));
            JSONObject sessionId = clientCommunicator.nextWord(sessionId1);
            String wordToSolve = sessionId.getJSONObject("data").getString("word");

            System.out.println(sessionId);

            Character character = characters.get(0);

            int wrongGuessCountOfCurrentWord = 0;
            Character guessedWord = ' ';
            while (wrongGuessCountOfCurrentWord < 10 && wordToSolve.contains("*")) {

                System.out.println("Now guess.." + character);

                character = wordPreprocessor.computeGuess(wordToSolve,
                        characters, wordPreprocessor.getModels(), guessedWord);

                JSONObject jsonObject = clientCommunicator.guessWord(sessionId1,
                        String.valueOf(character));


                JSONObject data = jsonObject.getJSONObject("data");
                wordToSolve = data.getString("word");
                wrongGuessCountOfCurrentWord = data.getInt("wrongGuessCountOfCurrentWord");

                System.out.println(jsonObject);

                characters.remove(character);
            }


            JSONObject result = clientCommunicator.result(sessionId1);

            System.out.println(result);

        } catch (GameHasEndedException e) {
            System.exit(0);
        }
    }
}
