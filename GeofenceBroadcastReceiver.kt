import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofenceTransition = intent.getIntExtra(GeofenceTransitionsJobIntentService.EXTRA_GEOFENCE_TRANSITION, -1)

        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> handleGeofenceEnter(context)
            Geofence.GEOFENCE_TRANSITION_EXIT -> handleGeofenceExit(context)
            else -> Toast.makeText(context, "Unknown Geofence Transition", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGeofenceEnter(context: Context) {
        // Handle clock-in logic here
        Toast.makeText(context, "Clock-in triggered!", Toast.LENGTH_SHORT).show()
    }

    private fun handleGeofenceExit(context: Context) {
        // Handle clock-out logic here
        Toast.makeText(context, "Clock-out triggered!", Toast.LENGTH_SHORT).show()
    }
}