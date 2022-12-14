package lheureux.votodroid.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import lheureux.votodroid.database.Converters;

@Entity(foreignKeys = @ForeignKey(entity = Question.class, parentColumns = "questionId", childColumns = "questionId", onDelete = CASCADE))
public class Vote {
    @PrimaryKey(autoGenerate = true)
    public Long voteId;

    public float value;

    public String voterName;

    @TypeConverters({Converters.class})
    public Date createdAt;

    @ColumnInfo(index = true)
    public Long questionId;

    public Vote() {
        this.createdAt = new Date();
    }

    public Vote(float value, Long questionId, String voterName) {
        this.value = value;
        this.questionId = questionId;
        this.createdAt = new Date();
        this.voterName = voterName;
    }

}
