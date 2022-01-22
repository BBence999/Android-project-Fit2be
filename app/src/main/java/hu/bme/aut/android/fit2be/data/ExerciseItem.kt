package hu.bme.aut.android.fit2be.data

import androidx.room.*

@Entity(tableName = "exercises")
data class ExerciseItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "body_part") var body_part: BodyPart,
    @ColumnInfo(name = "exercise_name") var exercise_name: String,
    @ColumnInfo(name = "difficulty") var difficulty: String,
    @ColumnInfo(name = "muscle_group") var muscle_group: String,
    @ColumnInfo(name = "sets") var sets: Int,
    @ColumnInfo(name = "reps") var reps: Int,
    @ColumnInfo(name = "current_plan",defaultValue = "0") var current_plan: Int,
) {
    enum class BodyPart {
        LOWER, UPPER, FULL;
    }
}


