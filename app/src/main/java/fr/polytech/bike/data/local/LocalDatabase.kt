package fr.polytech.bike.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.polytech.bike.data.model.Converters
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.data.model.Utilisateur

@Database(entities = [JwtResponse::class, Utilisateur::class, Sortie::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract val JWTDao: JWTDao
    abstract val userDao: UserDao
    abstract val sortieDao: SortieLocalDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        fun getInstance(context: Context): LocalDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "local_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}