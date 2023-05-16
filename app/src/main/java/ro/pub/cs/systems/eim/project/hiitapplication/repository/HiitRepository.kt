package ro.pub.cs.systems.eim.project.hiitapplication.repository

import android.content.Context
import ro.pub.cs.systems.eim.project.hiitapplication.data.database.HiitLocalDatabase
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExercise
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExerciseEntity
import ro.pub.cs.systems.eim.project.hiitapplication.network.HiitApiClient
import ro.pub.cs.systems.eim.project.hiitapplication.network.HiitApiService

class HiitRepository(private val context: Context) {
    private var client: HiitApiService = HiitApiClient.apiService
    private var localDatabase: HiitLocalDatabase = HiitLocalDatabase.getInstance(context)

    suspend fun getBeginnerExercises(): List<HiitExercise> {
        return try {
            val exercises = client.getBeginnerExercises()
            val exerciseEntities = exercises.map { exercise ->
                HiitExerciseEntity(
                    name = exercise.name!!,
                    duration = exercise.duration!!,
                    difficulty = "beginner"
                )
            }
            localDatabase.hiitExerciseDao().insertAll(exerciseEntities)
            exercises
        } catch (e: Exception) {
            localDatabase.hiitExerciseDao().getBeginnerExercises()
                .map { HiitExercise(it.name, it.duration) }
        }
    }
    suspend fun getIntermediateExercises(): List<HiitExercise> {
        return try {
            val exercises = client.getIntermediateExercises()
            val exerciseEntities = exercises.map { exercise ->
                HiitExerciseEntity(
                    name = exercise.name!!,
                    duration = exercise.duration!!,
                    difficulty = "intermediate"
                )
            }
            localDatabase.hiitExerciseDao().insertAll(exerciseEntities)
            exercises
        } catch (e: Exception) {
            localDatabase.hiitExerciseDao().getIntermediateExercises()
                .map { HiitExercise(it.name, it.duration) }
        }
    }
    suspend fun getAdvancedExercises(): List<HiitExercise> {
        return try {
            val exercises = client.getAdvancedExercises()
            val exerciseEntities = exercises.map { exercise ->
                HiitExerciseEntity(
                    name = exercise.name!!,
                    duration = exercise.duration!!,
                    difficulty = "advanced"
                )
            }
            localDatabase.hiitExerciseDao().insertAll(exerciseEntities)
            exercises
        } catch (e: Exception) {
            localDatabase.hiitExerciseDao().getAdvancedExercises()
                .map { HiitExercise(it.name, it.duration) }
        }
    }
}