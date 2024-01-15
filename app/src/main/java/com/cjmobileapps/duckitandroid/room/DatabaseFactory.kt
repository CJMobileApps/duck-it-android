package com.cjmobileapps.duckitandroid.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cjmobileapps.duckitandroid.data.model.Posts

@Database(entities = [Posts::class], version = 1)
@TypeConverters(Converters::class)
abstract class DuckItDatabase : RoomDatabase() {

    abstract fun duckItDao(): DuckItDao
}

class DatabaseFactory {

    companion object {
        fun getDuckItDatabase(context: Context): DuckItDatabase {
            return Room.databaseBuilder(
                context,
                DuckItDatabase::class.java,
                "duckit-database"
            ).build()
        }
    }
}
