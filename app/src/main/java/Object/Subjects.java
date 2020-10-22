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
    private String id_list_ques;

    public Subjects() {
    }

    public Subjects(String name, int numOfRating, String level, int numOfQuestion, float rating, String time, String comment, String id_list_ques) {
        this.name = name;
        this.num_rate = numOfRating;
        this.level = level;
        this.num_ques = numOfQuestion;
        this.rating = rating;
        this.time = time;
        this.comment = comment;
        this.id_list_ques = id_list_ques;
    }

    public String getId_list_ques() {
        return id_list_ques;
    }

    public void setId_list_ques(String id_list_ques) {
        this.id_list_ques = id_list_ques;
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
