package ro.pub.cs.systems.eim.project.hiitapplication.repository

import android.util.Log
import ro.pub.cs.systems.eim.project.hiitapplication.dto.HiitExercise
import ro.pub.cs.systems.eim.project.hiitapplication.network.HiitApiClient
import ro.pub.cs.systems.eim.project.hiitapplication.network.HiitApiService

class HiitRepository {
    var client: HiitApiService = HiitApiClient.apiService
    // TODO: if network is not available, get from local database

    suspend fun getBeginnerExercises(): List<HiitExercise> {
        return client.getBeginnerExercises()
    }
    suspend fun getIntermediateExercises(): List<HiitExercise> {
        return client.getIntermediateExercises()
    }
    suspend fun getAdvancedExercises(): List<HiitExercise> {
        return client.getAdvancedExercises()
    }
}