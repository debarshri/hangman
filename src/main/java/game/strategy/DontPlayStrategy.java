package game.strategy;

import game.calc.Model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dbasak on 4/14/16.
 */
public class DontPlayStrategy implements StrategyInterface {
    @Override
    public List<String> compute(String worder, List<Model> models, int attempts, Set<Character> guessedWord) {


        long count = worder.chars().filter(c -> c == '*').count();

        if(count <= (10 - attempts))
        {
            //Word stemming
            List<String> model = models.stream()
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
                    .collect(Collectors.<String>toList());

            return model;
        }
        return null;
    }
}
