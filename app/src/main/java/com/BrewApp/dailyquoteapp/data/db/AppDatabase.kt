package com.BrewApp.dailyquoteapp.data.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. The Entity (Table)
@Entity(tableName = "favorite_quotes")
data class FavoriteQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val author: String
)

// 2. The DAO (Access Object)
@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_quotes ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoriteQuote>>

    // Checks if a quote exists. Returns true/false via Flow.
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_quotes WHERE text = :text LIMIT 1)")
    fun isFavorite(text: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: FavoriteQuote)

    @Query("DELETE FROM favorite_quotes WHERE text = :text")
    suspend fun deleteByText(text: String)
}

// 3. The Database
@Database(entities = [FavoriteQuote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "daily_quotes_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}