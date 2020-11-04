package Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Subjects implements Serializable, Parcelable {
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

    protected Subjects(Parcel in) {
        rating = in.readFloat();
        name = in.readString();
        num_rate = in.readInt();
        level = in.readString();
        num_ques = in.readInt();
        time = in.readString();
        comment = in.readString();
        id_list_ques = in.readString();
    }

    public static final Creator<Subjects> CREATOR = new Creator<Subjects>() {
        @Override
        public Subjects createFromParcel(Parcel in) {
            return new Subjects(in);
        }

        @Override
        public Subjects[] newArray(int size) {
            return new Subjects[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
        dest.writeString(name);
        dest.writeInt(num_rate);
        dest.writeString(level);
        dest.writeInt(num_ques);
        dest.writeString(time);
        dest.writeString(comment);
        dest.writeString(id_list_ques);
    }
}
