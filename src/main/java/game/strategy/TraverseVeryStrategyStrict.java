package game.strategy;

import com.google.common.collect.Lists;
import game.calc.Model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by dbasak on 4/14/16.
 */
public class TraverseVeryStrategyStrict implements StrategyInterface {

    @Override
    public Collection<String> compute(String worder, List<Model> models, int attempt, Set<Character> guessedWord) {


        List<String> data = Lists.newArrayList();

        //Word stemming
         models.stream()
                .filter(model1 -> model1.getLength() >= worder.length())
                .map(Model::getWord)
                .filter(word -> {

                    for(int i=0; i < worder.length(); i++)
                    {
                        if(! (worder.charAt(i) == '*'))
                        {
                            if(!(worder.charAt(i) == word.charAt(i)) || !guessedWord.contains(worder.charAt(i)))
                            {
                                return false;
                            }

                            data.add(String.valueOf(word.charAt(i)));
                        }
                    }

                    return true;
                })
                .map(word -> word.substring(0, worder.length())).forEach( word -> {
                    for(int i=0; i < worder.length(); i++)
                    {
                        if(! (worder.charAt(i) == '*'))
                        {
                            if(!(worder.charAt(i) == word.charAt(i)) || !guessedWord.contains(worder.charAt(i)))
                            {
                                //ignore
                            }
                            else
                            {
                                data.add(String.valueOf(word.charAt(i)));
                            }

                        }
                    }

                });

        return data;
    }
}
