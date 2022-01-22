package hu.bme.aut.android.fit2be.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseItem::class], version = 1)
abstract class ExerciseListDatabase : RoomDatabase() {
    abstract fun exerciseItemDAO(): ExerciseItemDao

    companion object {
        fun getDatabase(applicationContext: Context): ExerciseListDatabase {
            return Room.databaseBuilder(applicationContext, ExerciseListDatabase::class.java, "exercises.db")
                .createFromAsset("database/exercises.db").build()
        }
    }
}