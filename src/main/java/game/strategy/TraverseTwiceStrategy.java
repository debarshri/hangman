package game.strategy;

import game.calc.Model;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by dbasak on 4/14/16.
 */
public class TraverseTwiceStrategy implements StrategyInterface {

    private static final Logger logger = Logger.getLogger("Game");

    @Override
    public List<String> compute(String worder, List<Model> models,int attempt) {

        //Word stemming
        List<String> model = models.stream()
                .filter(model1 -> model1.getLength() == worder.length())
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


        if(model.size() == 0)
        {
            System.out.println("Exact match not found");

            //Word stemming
            model = models.stream()
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

        }
        return model;
    }
}
