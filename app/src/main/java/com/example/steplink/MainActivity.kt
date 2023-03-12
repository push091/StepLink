package com.example.steplink

import android.Manifest

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log

import android.view.View

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

import com.example.steplink.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var bindig: ActivityMainBinding


    private var sensorManager: SensorManager?=null

    private var running = false

    private var totalcup:Int = 0
    private var totalSteps = 0f
    private var prevSteps = 0f
    private lateinit var pLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bindig= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindig.root)

        registerPermission()
        checkSensor()
        waterCalculate()
        resetSteps()
        loadData()

       /// editsleep ()
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }
    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor=sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if ( stepSensor== null) {
            Toast.makeText(this, "No sensor detected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Sensor detected", Toast.LENGTH_SHORT).show()
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)

        }

    }






    private fun checkSensor(){
        when{
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED ->{
                Toast.makeText(this,"Adapter run", Toast.LENGTH_SHORT).show()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)->{
                Toast.makeText(this,"Need permission", Toast.LENGTH_SHORT).show()
            }
            else -> {
                pLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
    }
    private  fun registerPermission(){
        pLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                Toast.makeText(this,"Adapter run", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this,"Denied", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            if (event != null) {
                totalSteps = event.values[0]

                val currentSteps = totalSteps.toInt() - prevSteps.toInt()
               bindig.tvStepsTaken.text = ("$currentSteps")

                bindig.progressBar.progress=currentSteps
            }
        }

    }


    private fun resetSteps() {
        bindig.podiatryFi.setOnClickListener {
            Toast.makeText(this, "Long tap  ", Toast.LENGTH_SHORT).show()
        }

        bindig.podiatryFi.setOnLongClickListener {
            Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show()
            prevSteps = totalSteps
            bindig.tvStepsTaken.text = 0.toString()
            bindig.progressBar.progress=0
            saveSteps()
            true
        }

    }

    fun saveSteps() {
        val sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", prevSteps)

        editor.apply()

    }
    private fun savecup() {
        val sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key2", totalcup)

        editor.apply()

    }

    private  fun editsleep (){
        bindig.rectangle6.setOnClickListener{
            val input = bindig.textView2.editableText
        }

        }



    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        val savedcup = sharedPreferences.getInt("key2", 0)

        Log.d("MainActivity", "$savedNumber")
        Log.d("MainActivity", "$savedcup")
        prevSteps = savedNumber
        totalcup=savedcup
        bindig.waterCup.text=("$totalcup")

    }


    private fun waterCalculate(){
        bindig.addFill.setOnClickListener {

            totalcup += 1
            bindig.waterCup.text=("$totalcup")
            savecup()
        }
        bindig.removeFill.setOnClickListener {

            if (totalcup!=0){
                totalcup -= 1


            }
            bindig.waterCup.text=("$totalcup")
            savecup()
        }

    }


    fun to_steps (view: View){
        val intent=Intent(this,steps::class.java)
        intent.putExtra("TOTALSTEPS",totalSteps)
        intent.putExtra("PREVSTEPS", prevSteps)
        startActivity(intent)
    }
    fun to_sleep (view: View){
        val intent=Intent(this,sleep::class.java)
        startActivity(intent)
    }
    fun to_weght (view: View){
        val intent=Intent(this,weght::class.java)
        startActivity(intent)
    }
}