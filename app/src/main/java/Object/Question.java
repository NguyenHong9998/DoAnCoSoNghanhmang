package Object;

import java.util.List;
import java.util.Map;

public class Question {
    private String question;
    private Map<Integer, String> answer;
    private Integer correctAnswer;

    public Question() {
    }

    public Question(String question, Map<Integer, String> answer, Integer correctAnswer) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<Integer, String> getAnswer() {
        return answer;
    }

    public void setAnswer(Map<Integer, String> answer) {
        this.answer = answer;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
