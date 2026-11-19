package com.example.zd2_v7_pavlov

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class AlbumsActivity : AppCompatActivity() {

    private lateinit var adapter: AlbumAdapter
    private val client = OkHttpClient()


    private val discogsToken = "VsqKxNRAWeeeKjNsMBqeSUogKRfkIBRdJgleWoOH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAlbums)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AlbumAdapter(emptyList())
        recyclerView.adapter = adapter


        fetchAlbumsFromApi()
    }

    private fun fetchAlbumsFromApi() {

        val url = "https://api.discogs.com/database/search?year=2023&type=release&per_page=20"

        val request = Request.Builder()
            .url(url)

            .addHeader("User-Agent", "MusicCatalogApp/1.0")

            .addHeader("Authorization", "Discogs token=$discogsToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AlbumsActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {

                    val gson = Gson()

                    val apiResponse = gson.fromJson(responseBody, DiscogsResponse::class.java)


                    val albumTitles = apiResponse.results.map { it.title }

                    runOnUiThread {
                        adapter.updateList(albumTitles)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AlbumsActivity, "Ошибка API: ${response.code}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}


data class DiscogsResponse(val results: List<DiscogsResult>)
data class DiscogsResult(val title: String)


class AlbumAdapter(var albums: List<String>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val title: android.widget.TextView = view.findViewById(R.id.tvAlbumTitle)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = albums[position]
    }

    override fun getItemCount() = albums.size

    fun updateList(newAlbums: List<String>) {
        albums = newAlbums
        notifyDataSetChanged()
    }
}