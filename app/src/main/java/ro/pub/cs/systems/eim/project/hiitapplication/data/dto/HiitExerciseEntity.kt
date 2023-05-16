package ro.pub.cs.systems.eim.project.hiitapplication.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hiit_exercises", primaryKeys = ["name", "duration", "difficulty"])
data class HiitExerciseEntity(
    val name: String,
    val duration: Int,
    val difficulty: String
)
