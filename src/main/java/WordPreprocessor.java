import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordPreprocessor {

    Map<Character, MutableInt> lengthToWordProbability = new HashMap<>();

    List<Character> wordlist = new ArrayList<>();

    private List<Model> models = Lists.newArrayList();

    public void index(Collection<String>... lists) throws IOException {

        for(Collection<String> list : lists)
        {
            for (String data : list) {
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

    public Character computeGuess(String worder, List<Character> characters, List<Model> models) throws IOException {

        WordPreprocessor wordPreprocessor = new WordPreprocessor();

        String trim = worder.replaceAll("\\*", "").trim();

        System.out.println(trim.length());
        for (int i = 0; i < trim.length(); i++) {
            char index = trim.charAt(i);
            if (characters.contains(index)) {
                characters.remove(Character.valueOf(index));
            }
        }

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
//
//                        //Thresholding..
//                        if(word.length()/trim.length() < 3)
//                        {
//                            String s = worder.replaceAll("\\**", "*");
//                            String[] split = s.split("\\*");
//                            if(split.length == 2)
//                            {
//                              if(split[0].contains("*") || split[0].length() == 0)
//                              {
//                                  if(word.contains(split[1]))
//                                  {
//                                      System.out.println(word);
//                                      return true;
//                                  }
//                                  else
//                                  {
//                                      return false;
//                                  }
//                              }
//                                else if(split[1].contains("*") || split[1].length() == 0)
//                              {
//
//                                  if(word.contains(split[0]))
//                                  {
//                                      System.out.println(word);
//                                      return true;
//                                  }
//                                  else
//                                  {
//                                      return false;
//                                  }
//                              }
//                                else {
//                                  return false;
//                              }
//                            }
//                        }
//                        else
//                        {
//                            for (int i = 0; i < trim.length(); i++) {
//                                char index = trim.charAt(i);
//                                if (!word.contains(String.valueOf(index))) {
//                                    return false;
//                                }
//                            }
//
//                            System.out.println(word);
//                            return true;
//                        }
//
//                        System.out.println(word);
//                        return true;

                    })
                    .collect(Collectors.<String>toList());


            wordPreprocessor.index(model);

            List<Character> characters1 = Lists.newArrayList(wordPreprocessor.getWordlist());


            List<Character> intersection = ListUtils.intersection(characters, characters1);

            if(intersection.size() > 0)
            {
                return intersection.get(0);
            }
            else {
                return characters.get(0);
            }
        }
    }


}
