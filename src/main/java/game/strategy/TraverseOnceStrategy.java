package game.strategy;

import game.calc.Model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dbasak on 4/14/16.
 */
public class TraverseOnceStrategy implements StrategyInterface {

    @Override
    public Collection<String> compute(String worder, List<Model> models, int attempt) {


        //Word stemming
        Set<String> model = models.stream()
                .filter(model1 -> model1.getLength() >= worder.length())
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
                .collect(Collectors.<String>toSet());


        return model;
    }
}
