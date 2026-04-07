import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.android.gms.location.Geofence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
        private const val CHANNEL_ID = "clock_notifications"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofenceTransition = intent.getIntExtra("geofence_transition", -1)

        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> handleClockIn(context)
            Geofence.GEOFENCE_TRANSITION_EXIT -> handleClockOut(context)
            else -> {
                Toast.makeText(context, "Unknown Geofence Transition", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Invalid geofence transition: $geofenceTransition")
            }
        }
    }

    private fun handleClockIn(context: Context) {
        try {
            val timestamp = System.currentTimeMillis()
            val formattedTime = formatTimestamp(timestamp)

            // Get database instance
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "clock-events-db"
            ).build()

            // Save clock-in event to database
            val clockRecord = ClockRecord(
                clockInTime = formattedTime,
                clockOutTime = "",
                date = getDate(),
                duration = 0L
            )
            db.clockDao().insertClock(clockRecord)

            // Display notification
            displayNotification(context, "Clock In", "You have been clocked in at $formattedTime")
            Toast.makeText(context, "Clock-in saved!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Clock-in recorded at: $formattedTime")

        } catch (e: Exception) {
            Log.e(TAG, "Error handling clock-in: ${e.message}")
            Toast.makeText(context, "Error clocking in: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleClockOut(context: Context) {
        try {
            val timestamp = System.currentTimeMillis()
            val formattedTime = formatTimestamp(timestamp)

            // Get database instance
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "clock-events-db"
            ).build()

            // Save clock-out event to database
            val clockRecord = ClockRecord(
                clockInTime = "",
                clockOutTime = formattedTime,
                date = getDate(),
                duration = 0L
            )
            db.clockDao().insertClock(clockRecord)

            // Display notification
            displayNotification(context, "Clock Out", "You have been clocked out at $formattedTime")
            Toast.makeText(context, "Clock-out saved!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Clock-out recorded at: $formattedTime")

        } catch (e: Exception) {
            Log.e(TAG, "Error handling clock-out: ${e.message}")
            Toast.makeText(context, "Error clocking out: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun displayNotification(context: Context, title: String, message: String) {
        try {
            val notificationId = System.currentTimeMillis().toInt()
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())

            Log.d(TAG, "Notification displayed: $title - $message")
        } catch (e: Exception) {
            Log.e(TAG, "Error displaying notification: ${e.message}")
        }
    }
}