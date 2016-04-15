package game.strategy;

import game.calc.Model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dbasak on 4/14/16.
 */
public class TraverseTwiceStrategyStrict implements StrategyInterface {

    //todo
    @Override
    public Collection<String> compute(String worder, List<Model> models, int attempt, Set<Character> guessedWord) {

        //Word stemming
        Set<String> model = models.stream()
                .filter(model1 -> model1.getLength() == worder.length())
                .map(Model::getWord)
                .filter(word -> {

                    for(int i=0; i < worder.length(); i++)
                    {
                        if(! (worder.charAt(i) == '*'))
                        {
                            if(! (worder.charAt(i) == word.charAt(i)) || !guessedWord.contains(worder.charAt(i)))
                            {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .map(word -> word.substring(0, worder.length()))
                .collect(Collectors.<String>toSet());


        if (model.size() == 0)
        {
           model = models.stream()
                .filter(model1 -> model1.getLength() >= worder.length())
                .map(Model::getWord)
                .filter(word -> {

                    for(int i=0; i < worder.length(); i++)
                    {
                        if(! (worder.charAt(i) == '*'))
                        {
                            if(! (worder.charAt(i) == word.charAt(i)) || !guessedWord.contains(worder.charAt(i)))
                            {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .map(word -> word.substring(0, worder.length()))
                .collect(Collectors.<String>toSet());
        }

        return model;
    }
}
