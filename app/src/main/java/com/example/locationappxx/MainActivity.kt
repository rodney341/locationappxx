package com.example.locationappxx

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var btnCaptureLocation: Button
    private lateinit var btnStopCapture: Button
    private lateinit var tvLocation: TextView
    private lateinit var tvElapsedTime: TextView
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var handler: Handler
    private var startTimeMillis: Long = 0
    private var capturingLocation = false
    private lateinit var timeFormat: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // btnCaptureLocation = findViewById(R.id.btnCaptureLocation)
        btnCaptureLocation = findViewById(R.id.btnCaptureLocation)
        btnStopCapture = findViewById(R.id.btnStopCapture)
        tvLocation = findViewById(R.id.tvLocation)
        tvElapsedTime = findViewById(R.id.tvElapsedTime)

        databaseHelper = MyDatabaseHelper(this)
        handler = Handler()
        timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        capturingLocation = false

        btnCaptureLocation.setOnClickListener {
            startLocationService()
        }

        btnStopCapture.setOnClickListener {
            stopLocationService()
        }
    }

    private fun startLocationService() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            if (!capturingLocation) {
                val intent = Intent(this, LocationService::class.java)
                startService(intent)
                startTimeMillis = SystemClock.elapsedRealtime()
                updateElapsedTime()
                capturingLocation = true
                btnCaptureLocation.isEnabled = false
                btnStopCapture.isEnabled = true
                Toast.makeText(this, "Capturando ubicación en segundo plano...", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun stopLocationService() {
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
        capturingLocation = false
        btnCaptureLocation.isEnabled = true
        btnStopCapture.isEnabled = false
        handler.removeCallbacksAndMessages(null)
        tvElapsedTime.text = "Tiempo transcurrido: "
        Toast.makeText(this, "Captura de ubicación detenida.", Toast.LENGTH_SHORT).show()
    }

    private fun updateElapsedTime() {
        val elapsedTimeMillis = SystemClock.elapsedRealtime() - startTimeMillis
        val elapsedTimeFormatted = timeFormat.format(Date(elapsedTimeMillis))
        tvElapsedTime.text = "Tiempo transcurrido: $elapsedTimeFormatted"
        handler.postDelayed(
            {
                updateElapsedTime()
            },
            1000
        )
    }

    // Resto del código de la actividad principal
}
