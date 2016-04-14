package test;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WordPreprocessor {

    Map<Character, MutableInt> lengthToWordProbability = new HashMap<>();

    List<Character> wordlist = new ArrayList<>();

    private List<Model> models = Lists.newArrayList();

    public void index(Collection<String>... lists) throws IOException {

        for(Collection<String> list : lists)
        {
            for (String data : list) {

                data = data.replaceAll("'", "");

                models.add(new Model(data.toUpperCase(), data.length(), calc_score(data.toUpperCase())));

                updateProbabilisticWordToLength(data.toUpperCase());
            }

            Map<Character, MutableInt> characterMutableIntMap = MapSorter.sortByValue(lengthToWordProbability);

            Iterator<Map.Entry<Character, MutableInt>> iterator = characterMutableIntMap.entrySet().iterator();

            iterator.forEachRemaining(it -> wordlist.add(it.getKey()));
        }
    }

    public Map<Character, MutableInt> getLengthToWordProbability() {
        return lengthToWordProbability;
    }

    public List<Character> getWordlist() {
        return wordlist;
    }

    public List<Model> getModels() {
        return models;
    }

    private void updateProbabilisticWordToLength(String data) {
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

    private int calc_score(String data) {

        int sum = 0;

        for (int i = 0; i < data.length(); i++) {
            sum = +data.toUpperCase().charAt(i);
        }
        return sum;
    }

    public Character computeGuess(String worder, List<Character> characters, List<Model> models, Character guessedWord) throws IOException {

        WordPreprocessor wordPreprocessor = new WordPreprocessor();

        String trim = worder.replaceAll("\\*", "").trim();

        System.out.println(trim.length());
        for (int i = 0; i < trim.length(); i++) {
            char index = trim.charAt(i);
            if (characters.contains(index)) {
                characters.remove(Character.valueOf(index));
            }
        }

        characters.remove(guessedWord);

        int key = calc_score(trim);

        System.out.println("For word, " + trim + "," + key);

        if (key == 0) {
            return characters.get(0);
        } else {

            List<String> model = models.stream()
                    .filter(model1 -> model1.getLength() == worder.length())
                    .filter(model1 -> model1.getScore() > key)
                    .map(Model::getWord)
                    .filter(word -> {

                        for(int i=0; i < worder.length(); i++)
                        {
                           if(! (worder.charAt(i) == '*'))
                           {
                               if(! (worder.charAt(i) == word.charAt(i)))
                               {
                                   return false;
                               }
                           }
                        }

                        return true;
                    })
                    .collect(Collectors.<String>toList());



            wordPreprocessor.index(model);

            List<Character> characters1 = Lists.newArrayList(wordPreprocessor.getWordlist());

            List<Character> intersection = ListUtils.intersection(characters, characters1);

            System.out.println("Intersection size "+intersection.size());
            System.out.println("characters1 size "+characters1.size());

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

            if(intersection.size() > 0 )
            {

                intersection.remove(guessedWord);
                return intersection.get(0);
            }
            else if(characters1.size() > 0){


                characters1.remove(guessedWord);

                return characters1.get(0);
            }
            else
            {
                return characters.get(0);
            }
        }
    }


}
