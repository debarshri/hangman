package game;

import com.google.common.collect.Lists;
import game.calc.MapSorter;
import game.calc.Model;
import game.strategy.*;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class WordPreprocessor {

    private final Map<Character, MutableInt> lengthToWordProbability;
    private List<Character> wordlist ;
    private List<Model> models;
    private static final Logger logger = Logger.getLogger("Game");
    private StrategyInterface strategy;

    public WordPreprocessor(StrategyInterface startegy) {
        this.strategy = startegy;
        lengthToWordProbability = new HashMap<>();
        wordlist = new ArrayList<>();
        models = Lists.newArrayList();
    }

    public void process(Collection<String>... lists) throws IOException {

        for(Collection<String> list : lists)
        {
            for (String data : list) {

                data = data.replaceAll("'", "");
                models.add(new Model(data.toUpperCase(), data.length(), calc_score(data.toUpperCase())));
                updateProbabilisticLengthwiseWord(data.toUpperCase());
            }

            Map<Character, MutableInt> characterMutableIntMap = MapSorter.sortByValue(lengthToWordProbability);
            Iterator<Map.Entry<Character, MutableInt>> iterator = characterMutableIntMap.entrySet().iterator();
            iterator.forEachRemaining(it -> wordlist.add(it.getKey()));
        }
    }

    public List<Character> getWordlist() {
        return wordlist;
    }

    public List<Model> getModels() {
        return models;
    }

    private void updateProbabilisticLengthwiseWord(String data) {
        for (int i = 0; i < data.length(); i++) {
            char c = data.toUpperCase().charAt(i);

            MutableInt mutableInt = lengthToWordProbability.get(c);

            if (mutableInt == null) {
                mutableInt = new MutableInt(1);
                lengthToWordProbability.put(c, mutableInt);
            } else {
                mutableInt.increment();
            }

        }
    }

    public static int calc_score(String data) {
        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            sum = +data.toUpperCase().charAt(i);
        }
        return sum;
    }

    public Character computeGuess(String worder,
                                  List<Character> characters,
                                  List<Model> models,
                                  Set<Character> guessedWord,
                                  int attempts) throws IOException {

        WordPreprocessor wordPreprocessor = new WordPreprocessor(new TraverseOnceStrategyStrict());
        String trim = worder.replaceAll("\\*", "").trim();

        for (int i = 0; i < trim.length(); i++) {
            char index = trim.charAt(i);
            if (characters.contains(index)) {
                characters.remove(Character.valueOf(index));
            }
        }

        int incomingWordScore = calc_score(trim);

        if (incomingWordScore == 0) {
            return characters.get(0);
        } else {

            Collection<String> model = strategy.compute(worder,models,attempts,guessedWord);

            if(model == null)
                return null;

            if(model.size() == 0)
                return null;

            wordPreprocessor.process(model);

            List<Character> characters1 = Lists.newArrayList(wordPreprocessor.getWordlist());
            List<Character> intersection = ListUtils.intersection(characters, characters1);

            logger.fine("Model is "+model.size()+", " +
                    "Intersection size is "+intersection.size()+", " +
                    "new character list size is "+characters1.size());

            for (int i = 0; i < trim.length(); i++) {
                char index = trim.charAt(i);
                if (characters1.contains(index)) {
                    characters1.remove(Character.valueOf(index));
                }
            }

            for (int i = 0; i < trim.length(); i++) {
                char index = trim.charAt(i);
                if (intersection.contains(index)) {
                    intersection.remove(Character.valueOf(index));
                }
            }

            intersection.removeAll(guessedWord);
            characters1.removeAll(guessedWord);
            characters.removeAll(guessedWord);

            if(intersection.size() > 0 )
            {
                return intersection.get(0);
            }
            else if(characters1.size() > 0){

                return characters1.get(0);
            }
            else
            {
                return characters.get(0);
            }
        }
    }
}
