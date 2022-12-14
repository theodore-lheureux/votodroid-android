package lheureux.votodroid.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import lheureux.votodroid.database.Converters;

@Entity
public class Question {
    @PrimaryKey(autoGenerate = true)
    public Long questionId;

    public String text;

    @TypeConverters({Converters.class})
    public Date createdAt;

    public Question(String question) {
        this.text = question;
        this.createdAt = new Date();
    }
    public Question() {
        this.createdAt = new Date();
    }
}
