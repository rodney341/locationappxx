package com.example.locationappxx
import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationService : Service() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            saveLocationToDatabase(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // Implementa lógica de manejo de cambios de estado si es necesario
        }

        override fun onProviderEnabled(provider: String) {
            // Implementa lógica de manejo de habilitación de proveedor si es necesario
        }

        override fun onProviderDisabled(provider: String) {
            // Implementa lógica de manejo de deshabilitación de proveedor si es necesario
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000, // Intervalo de actualización en milisegundos
                0f, // Distancia mínima en metros
                locationListener
            )
        }
    }

    private fun saveLocationToDatabase(location: Location) {
        // Implementa aquí la lógica para almacenar la ubicación en la base de datos SQLite
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }
}
