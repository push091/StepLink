package com.example.steplink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.steplink.databinding.ActivityStepsBinding
import kotlin.math.roundToInt

class steps : AppCompatActivity() {
    private var totalSteps = 0f
    private var prevSteps = 0f
    lateinit var bindig: ActivityStepsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig= ActivityStepsBinding.inflate(layoutInflater)
        setContentView(bindig.root)


    }

    override fun onResume() {
        super.onResume()
        onSensorChanged()
    }
   private fun onSensorChanged() {
       totalSteps= intent.getFloatExtra("TOTALSTEPS",0f)
       prevSteps =intent.getFloatExtra("PREVSTEPS",0f)
       val currentSteps = totalSteps.toInt() - prevSteps.toInt()
            bindig.tvStepsTaken.text = ("$currentSteps")

            bindig.circularProgressBar.apply {
            setProgressWithAnimation(currentSteps.toFloat())
       }
       wayCounter(currentSteps)
   }
    private fun wayCounter(currentSteps: Int) {
        var v = 1400f
        val way =(( currentSteps /v)*100.0).roundToInt()/100.0
        bindig.textView10.text= way.toString()

    }

    fun to_main(view: View){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}



