package game;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import game.calc.MapSorter;
import game.strategy.*;
import model.ClientCommunicator;
import model.GameHasEndedException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static game.calc.MapSorter.*;

public class Game {

    private static final Logger logger = Logger.getLogger("Game");
    private ClientCommunicator clientCommunicator;

    public Game(ClientCommunicator clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }

    public void start() throws JSONException, IOException {

        try {


            List<String> list1 = read("/test.txt");
            List<String> list2 = read("/dictionary.txt");
            List<String> list3 = read("/test2.txt");

            WordPreprocessor wordPreprocessor = new WordPreprocessor(new TraverseOnceStrategy());

            wordPreprocessor.index( list1,list2, list3);

            List<Character> characters = new ArrayList<>();
            characters.addAll(wordPreprocessor.getWordlist());

            File token = new File("token");

            if (!token.exists()) {
                JSONObject start;
                start = clientCommunicator.start();

                String sessionId2 = start.getString("sessionId");
                FileUtils.writeStringToFile(token, sessionId2);
            }

            String sessionId1 = FileUtils.readFileToString(new File("token"));
            JSONObject sessionId = clientCommunicator.nextWord(sessionId1);
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
                        guessedWords,wrongGuessCountOfCurrentWord);

                if (character == null) {
                    break;
                }

                logger.info("Now guessing " + character + " for word " + wordToSolve);

                JSONObject jsonObject = clientCommunicator.guessWord(sessionId1,
                        String.valueOf(character));
                JSONObject data = jsonObject.getJSONObject("data");
                wordToSolve = data.getString("word");
                wrongGuessCountOfCurrentWord = data.getInt("wrongGuessCountOfCurrentWord");

                logger.info("status " + wordToSolve + " wrong word count " + wrongGuessCountOfCurrentWord);

                characters.remove(character);
            }

            JSONObject result = clientCommunicator.result(sessionId1);

            logger.info("Current score :"+result.getJSONObject("data").getInt("score")+", totalWordCount : "+
                    result.getJSONObject("data").getInt("totalWordCount")+", correctWordCount :"+
                    result.getJSONObject("data").getInt("correctWordCount"));

        } catch (GameHasEndedException e) {
            System.exit(0);
        }
    }
}
