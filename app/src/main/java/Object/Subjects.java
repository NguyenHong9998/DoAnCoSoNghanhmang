package Object;

import java.io.Serializable;

public class Subjects implements Serializable {
    private float rating;
    private String name;
    private int num_rate;
    private String level;
    private int num_ques;
    private String time;
    private String comment;

    public Subjects() {
    }

    public Subjects(String name, int numOfRating, String level, int numOfQuestion, float rating, String time, String comment) {
        this.name = name;
        this.num_rate = numOfRating;
        this.level = level;
        this.num_ques = numOfQuestion;
        this.rating = rating;
        this.time = time;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public int getNum_rate() {
        return num_rate;
    }

    public void setNum_rate(int num_rate) {
        this.num_rate = num_rate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getNum_ques() {
        return num_ques;
    }

    public void setNum_ques(int num_ques) {
        this.num_ques = num_ques;
    }

}
