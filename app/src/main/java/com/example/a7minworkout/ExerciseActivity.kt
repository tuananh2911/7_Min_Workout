package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityExerciseBinding
import com.example.a7minworkout.databinding.ActivityMainBinding
import com.example.a7minworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var timeDuration: Long = 60000
    private var restProcess = 0
    private var excerciseTimer: CountDownTimer? = null
    private var excerciseProcess = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    private var resTimeDuration: Long = 1
    private var exerciseTimeDuration: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()

        }
        exerciseList = Constants.defaultExcercise()
        tts = TextToSpeech(this, this)


    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecycleView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter((exerciseList!!))
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpExcerciseView() {
        binding?.flProgressExercise?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.ivImagine?.visibility = View.VISIBLE
        binding?.flProgressExercise?.visibility = View.VISIBLE
        binding?.flProgress?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseTitle?.visibility = View.INVISIBLE

        setExcerciseProgressBar()
        setupExerciseStatusRecycleView()
    }

    private fun setRestProgressBar() {


        binding?.flProgressExercise?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.ivImagine?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flProgress?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseTitle?.text =
            exerciseList!![currentExercisePosition + 1].getName()
        binding?.tvUpcomingExerciseTitle?.visibility = View.VISIBLE
        binding?.progressBar?.progress = restProcess
        excerciseTimer = object : CountDownTimer(resTimeDuration * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                restProcess++
                binding?.progressBar?.progress = 10 - restProcess
                binding?.tvTimer?.text = (10 - restProcess).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
//                speakOut(exerciseList!![currentExercisePosition].getName())
                binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
                binding?.ivImagine?.setImageResource(exerciseList!![currentExercisePosition].getImage())
                excerciseProcess = 0
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter?.notifyDataSetChanged()
                setUpExcerciseView()
            }

        }.start()
    }

    private fun setExcerciseProgressBar() {
        binding?.progressBar?.progress = excerciseProcess
        excerciseTimer = object : CountDownTimer(exerciseTimeDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                excerciseProcess++
                binding?.progressBarExercise?.progress = 30 - excerciseProcess
                binding?.tvTimerExercise?.text = (30 - excerciseProcess).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList!!.size - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    restProcess = 0

                    binding?.tvUpcomingExerciseTitle?.text =
                        exerciseList!![currentExercisePosition + 1].getName()
//                    speakOut("Get READY")
                    setRestProgressBar()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    //
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProcess = 0
        }
        if (excerciseTimer != null) {
            excerciseTimer?.cancel()
            excerciseProcess = 0
        }
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts?.setLanguage(Locale.US)
            setRestProgressBar()
//            speakOut("Get Ready")
//            for (i in 0..100000){
//                var j=0
//            }
            try {
                val soundURI =
                    Uri.parse("android.resource://com.example.a7minworkout/" + R.raw.hoaprox)
                player = MediaPlayer.create(applicationContext, soundURI)
                player?.isLooping = false
                player?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }


            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization Failed")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

}