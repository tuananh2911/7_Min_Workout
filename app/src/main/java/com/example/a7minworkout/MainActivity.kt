package com.example.a7minworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var viewBinding: ActivityMainBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
        viewBinding?.flStart?.setOnClickListener{
//            Toast.makeText(this,"Here pressed",Toast.LENGTH_LONG).show()
            var intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }
        viewBinding?.flBMI?.setOnClickListener{
            var intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
        viewBinding?.flHistory?.setOnClickListener {
            var intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null

    }

    override fun onInit(status: Int) {
        TODO("Not yet implemented")
    }
}