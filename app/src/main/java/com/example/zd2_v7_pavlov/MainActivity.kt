package com.example.zd2_v7_pavlov

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<LinearLayout>(R.id.btnArtists).setOnClickListener {
            startActivity(Intent(this, ArtistListActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnAlbums).setOnClickListener {
            startActivity(Intent(this, AlbumsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnFavorites).setOnClickListener {

        }
    }
}