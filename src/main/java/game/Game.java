package game;

import com.google.common.collect.Sets;
import game.strategy.*;
import model.ClientCommunicator;
import model.GameHasEndedException;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static game.calc.MapSorter.*;

public class Game {

    private static final Logger logger = Logger.getLogger("Game");
    private ClientCommunicator clientCommunicator;
    private String sessionId;

    public Game(ClientCommunicator clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }

    public void start() throws JSONException, IOException, GameHasEndedException {

        try {

            List<String> list1 = read("/test.txt");
            List<String> list2 = read("/dictionary.txt");

            WordPreprocessor wordPreprocessor = new WordPreprocessor(new TraverseOnceStrategyStrict());
            wordPreprocessor.process(list1,list2);
            List<Character> characters = new ArrayList<>(wordPreprocessor.getWordlist());

            File token = new File("token");

            if (!token.exists()) {
                JSONObject start;
                start = clientCommunicator.start();

                sessionId = start.getString("sessionId");
                FileUtils.writeStringToFile(token, sessionId);
            }

            sessionId = FileUtils.readFileToString(new File("token"));
            JSONObject sessionId = clientCommunicator.nextWord(this.sessionId);
            String wordToSolve = sessionId.getJSONObject("data").getString("word");

            Set<Character> guessedWords = Sets.newHashSet();
            Character character = characters.get(0);
            int wrongGuessCountOfCurrentWord = 0;

            logger.info("New word for session : "+sessionId);

            while (wrongGuessCountOfCurrentWord < 10 && wordToSolve.contains("*")) {

                guessedWords.add(character);

                character = wordPreprocessor.computeGuess(wordToSolve,
                        characters,
                        wordPreprocessor.getModels(),
                        guessedWords,
                        wrongGuessCountOfCurrentWord);

                if (character == null) {
                    break;
                }

                logger.info("Now guessing " + character + " for word " + wordToSolve);

                JSONObject jsonObject = clientCommunicator.guessWord(this.sessionId,
                        String.valueOf(character));
                JSONObject data = jsonObject.getJSONObject("data");

                wordToSolve = data.getString("word");
                wrongGuessCountOfCurrentWord = data.getInt("wrongGuessCountOfCurrentWord");

                logger.info("status " + wordToSolve + " wrong word count " + wrongGuessCountOfCurrentWord);

                characters.remove(character);
            }

            JSONObject result = clientCommunicator.result(this.sessionId);

            logger.info("Current score :"+result.getJSONObject("data").getInt("score")+", totalWordCount : "+
                    result.getJSONObject("data").getInt("totalWordCount")+", correctWordCount :"+
                    result.getJSONObject("data").getInt("correctWordCount"));

        } catch (GameHasEndedException e) {
            logger.warning("Game has ended");
            JSONObject jsonObject = clientCommunicator.submitResult(this.sessionId);
            logger.info(jsonObject.toString());
            System.exit(0);
        }
    }
}
