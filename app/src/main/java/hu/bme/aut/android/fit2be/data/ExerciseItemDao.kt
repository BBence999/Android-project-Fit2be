package hu.bme.aut.android.fit2be.data

import androidx.room.*

@Dao
interface ExerciseItemDao {

    @Query("SELECT * FROM exercises")
    fun getAll(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE (difficulty='Beginner')")
    fun getBeginnerExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE (difficulty='Intermediate')")
    fun getIntermediateExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE (difficulty='Advanced')")
    fun getAdvancedExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE (muscle_group=:muscleGroup and current_plan=0)")
    fun getExercisesByMuscleGroup(muscleGroup: String): List<ExerciseItem>

    @Query("UPDATE exercises SET current_plan=1 WHERE id=:id")
    fun setMyPlan(id: Long)

    @Query("UPDATE exercises SET current_plan=0 WHERE id=:id")
    fun removeFromMyPlan(id: Long)

    @Query("SELECT * FROM exercises WHERE current_plan=1")
    fun getMyPlan(): List<ExerciseItem>

    @Query("UPDATE exercises SET current_plan=0")
    fun resetMyPlan()

    @Query("SELECT * FROM exercises WHERE current_plan=1 and (muscle_group='Chest' or muscle_group='Triceps')")
    fun getMondayExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE current_plan=1 and (muscle_group='Back' or muscle_group='Biceps')")
    fun getTuesdayExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE current_plan=1 and (muscle_group='Calves' or muscle_group='Thighs')")
    fun getThursdayExercises(): List<ExerciseItem>

    @Query("SELECT * FROM exercises WHERE current_plan=1 and (muscle_group='Shoulders' or muscle_group='ABS' or muscle_group='Forearms')")
    fun getFridayExercises(): List<ExerciseItem>

    @Update
    fun update(exerciseItem: List<ExerciseItem>)

    @Delete
    fun deleteItem(exerciseItem: ExerciseItem)
}