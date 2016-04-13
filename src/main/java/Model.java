public class Model {

    private String word;
    private int length;
    private int score;

    public Model(String word, int length, int score) {
        this.word = word;
        this.length = length;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }
}
