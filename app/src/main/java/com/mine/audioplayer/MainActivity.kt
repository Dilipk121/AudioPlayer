package com.mine.audioplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    //make it nullable using ? mark
    private var mediaPlayer:MediaPlayer? = null // null means no any media-player object into memory or ram

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())

        val play = findViewById<FloatingActionButton>(R.id.play_btn)
        val pause = findViewById<FloatingActionButton>(R.id.pause_btn)
        val stop = findViewById<FloatingActionButton>(R.id.stop_btn)

        play.setOnClickListener{
            if (mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.songs)
                startSeekBar()
            }
            mediaPlayer?.start()
        }

        pause.setOnClickListener{
            mediaPlayer?.pause()
        }

        stop.setOnClickListener{
            mediaPlayer?.stop() // when stop mediaplayer called than make sure realse the memory
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null

            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }

    fun startSeekBar(){
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)mediaPlayer?.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })

        val tvStart = findViewById<TextView>(R.id.tv_start)
        val tvEnd = findViewById<TextView>(R.id.tv_end)

            // !! is not null
            seekBar.max = mediaPlayer!!.duration
            runnable = Runnable {
                seekBar.progress = mediaPlayer!!.currentPosition

                val playedTime = mediaPlayer!!.currentPosition/1000
                tvStart.text = "$playedTime sec"
                val duration = mediaPlayer!!.duration/1000
                val duetime = duration - playedTime
                tvEnd.text = "$duetime sec"

                handler.postDelayed(runnable,500)
            }
        handler.postDelayed(runnable,500)


    }



}