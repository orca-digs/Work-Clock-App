import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Geofence
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var clockInOutStatus: TextView
    private var isClockedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geofencingClient = LocationServices.getGeofencingClient(this)
        clockInOutStatus = findViewById(R.id.clockInOutStatus)

        val clockInButton: Button = findViewById(R.id.clockInButton)
        val clockOutButton: Button = findViewById(R.id.clockOutButton)

        clockInButton.setOnClickListener { clockIn() }
        clockOutButton.setOnClickListener { clockOut() }

        // Request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    private fun clockIn() {
        isClockedIn = true
        clockInOutStatus.text = "Clocked In"
        Toast.makeText(this, "You've clocked in!", Toast.LENGTH_SHORT).show()
        setupGeofencing()  // Setting up geofencing on clock in
    }

    private fun clockOut() {
        isClockedIn = false
        clockInOutStatus.text = "Clocked Out"
        Toast.makeText(this, "You've clocked out!", Toast.LENGTH_SHORT).show()
        // Logic for clocking out can be added here
    }

    private fun setupGeofencing() {
        val geofence = Geofence.Builder()
            .setRequestId("uniqueID")
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setCircularRegion(37.4219983, -122.084, 100f)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, getPendingIntent()).addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Geofencing set up successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to set up geofencing.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getPendingIntent(): PendingIntent {
        // Create pending intent for geofence transitions
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}