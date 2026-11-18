package com.example.zd2_v7_pavlov
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists")
    fun getAll(): List<Artist>

    @Insert
    fun insert(artist: Artist)

    @Delete
    fun delete(artist: Artist)
}