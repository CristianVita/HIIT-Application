package ro.pub.cs.systems.eim.project.hiitapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExerciseEntity

@Dao
interface HiitExerciseDao {
    @Query("SELECT * FROM hiit_exercises WHERE difficulty = 'beginner'")
    suspend fun getBeginnerExercises(): List<HiitExerciseEntity>

    @Query("SELECT * FROM hiit_exercises WHERE difficulty = 'intermediate'")
    suspend fun getIntermediateExercises(): List<HiitExerciseEntity>

    @Query("SELECT * FROM hiit_exercises WHERE difficulty = 'advanced'")
    suspend fun getAdvancedExercises(): List<HiitExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<HiitExerciseEntity>)

    @Query("DELETE FROM hiit_exercises")
    suspend fun clearTable()
}