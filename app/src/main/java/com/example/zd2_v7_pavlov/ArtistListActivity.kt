package com.example.zd2_v7_pavlov

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlin.concurrent.thread

class ArtistListActivity : AppCompatActivity() {
    private lateinit var adapter: ArtistAdapter
    private lateinit var dao: ArtistDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_list)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "music-db").build()
        dao = db.artistDao()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewArtists)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArtistAdapter(emptyList()) { artist ->

            thread {
                dao.delete(artist)
                loadData()
            }
        }
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnAddArtist).setOnClickListener {
            showAddDialog()
        }

        loadData()
    }

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Новый исполнитель")
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Добавить") { _, _ ->
            val name = input.text.toString().trim()
            if (name.isNotEmpty()) {
                thread {
                    dao.insert(Artist(name = name))
                    loadData()
                }
            }
        }
        builder.setNegativeButton("Отмена", null)
        builder.show()
    }

    private fun loadData() {
        thread {
            val list = dao.getAll()
            runOnUiThread { adapter.updateList(list) }
        }
    }
}

class ArtistAdapter(var items: List<Artist>, val onItemClick: (Artist) -> Unit) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<Artist>) {
        items = newItems
        notifyDataSetChanged()
    }
}