package ro.pub.cs.systems.eim.project.hiitapplication.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.WorkoutSession

class WorkoutHistoryRepository(private val userId: String) {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionPath: String = "workout_sessions_history"
    private val sessionHistoryPath: String = "sessions_history"

    suspend fun addWorkoutSession(workoutSession: WorkoutSession) {
        // Get the document reference for the user
        val documentRef: DocumentReference = firestore.collection(collectionPath)
            .document("user-$userId")
            .collection(sessionHistoryPath)
            .document()

        // Set the workout session to the document reference with merge option (if the document exists, it will be updated)
        documentRef.set(workoutSession, SetOptions.merge())
            .await()
    }

    suspend fun getWorkoutSessions(): List<WorkoutSession> {
        // Get the query snapshot for the user
        val querySnapshot: QuerySnapshot = firestore.collection(collectionPath)
            .document("user-$userId")
            .collection(sessionHistoryPath)
            .get()
            .await()

        val workoutSessions: ArrayList<WorkoutSession> = ArrayList()
        for (document in querySnapshot.documents) {
            workoutSessions.add(document.toObject(WorkoutSession::class.java)!!)
        }

        // Sort the workout sessions by date
        workoutSessions.sortByDescending { it.date }

        return workoutSessions
    }
}