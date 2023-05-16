package ro.pub.cs.systems.eim.project.hiitapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ro.pub.cs.systems.eim.project.hiitapplication.data.dao.HiitExerciseDao
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExerciseEntity

@Database(entities = [HiitExerciseEntity::class], version = 2)
abstract class HiitLocalDatabase : RoomDatabase() {
    abstract fun hiitExerciseDao(): HiitExerciseDao

    companion object {
        private const val DATABASE_NAME = "hiit_local_database"

        @Volatile
        private var instance: HiitLocalDatabase? = null

        fun getInstance(context: Context): HiitLocalDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): HiitLocalDatabase {
            return Room.databaseBuilder(context, HiitLocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}