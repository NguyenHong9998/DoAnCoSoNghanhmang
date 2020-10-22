package Object;

import java.util.List;
import java.util.Map;

public class Question {
    private String question;
    private Map<String, Boolean> answers;

    public Question(String question, Map<String, Boolean> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }
}
