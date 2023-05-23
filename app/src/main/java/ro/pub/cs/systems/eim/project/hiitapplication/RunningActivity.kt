package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.WorkoutSession
import ro.pub.cs.systems.eim.project.hiitapplication.repository.WorkoutHistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class RunningActivity : AppCompatActivity(), SensorEventListener, LocationListener {
    private val REQUEST_PERMISSION_ACTIVITY_RECOGNITION = 1
    private val REQUEST_PERMISSION_LOCATION = 2

    private var isRunning: Boolean = false

    private var distanceTV: TextView? = null
    private var timeTV: TextView? = null
    private var speedTV: TextView? = null
    private var stepsTV: TextView? = null
    private var startBtn: FrameLayout? = null
    private var startBtnText: TextView? = null

    /* The sensor manager is used to access the device's sensors */
    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null

    /* The location manager is used to access the device's location */
    private var locationManager: LocationManager? = null

    private var distance: Float = 0f
    private var steps: Int = 0
    private var speed: Float = 0f
    private var lastTime: Long = 0
    private var lastLocation: Location? = null

    private lateinit var workoutHistoryRepository: WorkoutHistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!

        workoutHistoryRepository = WorkoutHistoryRepository(user.uid)

        Log.d("RunningActivity", "onCreate() callback method was invoked")

        distanceTV = findViewById(R.id.actRunning_distance_tv)
        timeTV = findViewById(R.id.actRunning_time_tv)
        speedTV = findViewById(R.id.actRunning_speed_tv)
        stepsTV = findViewById(R.id.actRunning_steps_tv)
        startBtn = findViewById(R.id.actRunning_start_fl)
        startBtnText = findViewById(R.id.actRunning_start_button_text_tv)

        resetUI()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        /* Register the sensor listener and the location listener in order to receive updates */
        startBtn?.setOnClickListener {
            startRunningSession()
        }
    }

    private fun resetUI() {
        distanceTV?.text = "Distance: 0 m"
        timeTV?.text = "Time: 00:00:00"
        speedTV?.text = "Current speed: 0 m/s"
        stepsTV?.text = "Steps: 0"

        distance = 0f
        steps = 0
        speed = 0f
        lastTime = 0
        lastLocation = null
    }

    private fun startRunningSession() {
        startCountingSteps()
        startLocationUpdates()

        ifIsRunningStartTimer()
    }

    private fun ifIsRunningStartTimer() {
        if (isRunning) {
            startTimer()

            startBtnText?.text = "   Stop\nRunning"
            startBtn?.setOnClickListener {
                stopRunningSession()
            }
        }
    }

    private fun startTimer() {
        lastTime = getCurrentTime()

        Log.e("RunningActivity", "startTimer() callback method was invoked")
        Log.e("RunningActivity", "startTimer() callback method was invoked, isRunning = $isRunning")

        Thread {
            while (isRunning) {
                val currentTime = getCurrentTime()
                val time = currentTime - lastTime
                runOnUiThread {
                    timeTV?.text = "Time: ${formatTime(time)}"
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun formatTime(time: Long): String {
        val seconds = (time / 1000) % 60
        val minutes = (time / (1000 * 60)) % 60
        val hours = (time / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    private fun stopRunningSession() {
        isRunning = false

        sensorManager?.unregisterListener(this)
        locationManager?.removeUpdates(this)

        // Save the workout session in the database
        val workoutSession = WorkoutSession(
            "Running Session",
            "${distanceTV?.text.toString()}\n${timeTV?.text.toString()}",
            getCurrentDate()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            workoutHistoryRepository.addWorkoutSession(workoutSession)
        }

        startBtnText?.text = "   Start\nRunning"
        startBtn?.setOnClickListener {
            startRunningSession()
        }

        resetUI()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun startCountingSteps() {
        if (stepCounterSensor == null) {
            alertDialog("Sensor not found", "The step counter sensor could not be found, so the step count will not be available")
        } else {
            if (hasPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)) {
                alertDialog("Sensor found", "The step counter sensor was found, so the step count will be available")
                sensorManager?.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
                isRunning = true
            } else {
                alertDialog("Need permission", "The step counter sensor was found, but the app needs the permission to access it")
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    REQUEST_PERMISSION_ACTIVITY_RECOGNITION
                )
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Check if the location service is enabled
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDialog("Location service is disabled", "Please enable the location service")
        } else {
            if (hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                alertDialog("Location service is enabled", "The location service is enabled, so the distance and speed will be available")
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                isRunning = true
            } else {
                alertDialog("Need permission", "The location service is enabled, but the app needs the permission to access it")
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("RunningActivity", "onDestroy() method was invoked")

        stopRunningSession()
    }

    private fun hasPermission(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun alertDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(title)
            .setMessage(message)

        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_ACTIVITY_RECOGNITION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Activity Recognition permission granted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Activity Recognition permission denied!", Toast.LENGTH_LONG).show()
                }
            }
            REQUEST_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Location permission denied!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /* This method is called when the sensor values have changed */
    override fun onSensorChanged(p0: SensorEvent?) {
        Log.e("Sensor", "Sensor changed")

        if (p0 != null) {
            if (p0.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if (steps == 0) {
                    steps = p0.values[0].toInt()
                } else {
                    stepsTV?.text = "Steps: ${(p0.values[0].toInt() - steps)}"
                }
            }
        }
    }

    /* This method is called when the accuracy of the registered sensor has changed */
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Not in use
    }

    /* This method is called when the location is changed */
    override fun onLocationChanged(p0: Location) {
        Log.e("Location", "Location changed")

        if (p0.hasSpeed()) {
            speedTV?.text = "Current speed: ${p0.speed} m/s"
        } else {
            speedTV?.text = "Current speed: 0 m/s"
        }

        if (lastLocation == null) {
            lastLocation = p0
            Log.d("Location", "start location: $lastLocation")
        } else {
            Log.d("Current location", "current location: $p0")
            Log.d("Distance", "distance: ${p0.distanceTo(lastLocation!!)}")
            distance += p0.distanceTo(lastLocation!!)
            lastLocation = p0
            val distanceTwoDecimals = (distance * 100.0).roundToInt() / 100.0

            if (distanceTwoDecimals > 1000) {
                distanceTV?.text = "Distance: ${((distanceTwoDecimals / 1000) * 100.0).roundToInt() / 100.0} km"
            } else {
                distanceTV?.text = "Distance: $distanceTwoDecimals m"
            }

        }
    }
}