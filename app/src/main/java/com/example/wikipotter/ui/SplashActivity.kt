package com.example.wikipotter.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.wikipotter.R
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private val handler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        progressBar = findViewById(R.id.progressBar)
        val imageViewGif = findViewById<ImageView>(R.id.imageViewGif)
        Glide.with(this).asGif().load(R.drawable.gif_splash).into(imageViewGif)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Thread {
            while (progressStatus < 100) {
                progressStatus++
                handler.post {
                    progressBar.progress = progressStatus
                }
                try {
                    Thread.sleep(50) // Simula el tiempo de carga
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            // Una vez completada la carga, procede a la siguiente actividad
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }.start()




    }
}