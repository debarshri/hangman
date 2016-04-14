package game.strategy;

import game.calc.Model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StrategyInterface {

    public Collection<String> compute(String wordToBeGuessed, List<Model> models, int attempt, Set<Character> guessedWord);
}
