package game.strategy;

import game.calc.Model;

import java.util.Collection;
import java.util.List;

public interface StrategyInterface {

    public Collection<String> compute(String wordToBeGuessed, List<Model> models, int attempt);
}
