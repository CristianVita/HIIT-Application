package ro.pub.cs.systems.eim.project.hiitapplication.network

import retrofit2.http.GET
import ro.pub.cs.systems.eim.project.hiitapplication.dto.HiitExercise

interface HiitApiService {
    @GET("exercises/beginner")
    suspend fun getBeginnerExercises(): List<HiitExercise>

    @GET("exercises/intermediate")
    suspend fun getIntermediateExercises(): List<HiitExercise>

    @GET("exercises/advanced")
    suspend fun getAdvancedExercises(): List<HiitExercise>
}