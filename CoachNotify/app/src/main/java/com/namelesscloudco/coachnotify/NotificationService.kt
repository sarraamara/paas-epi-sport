package com.namelesscloudco.coachnotify

import android.R
import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.namelesscloudco.coachnotify.model.UserCoachHeartRate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class NotificationService : Service() {
   var timer: Timer? = null
   var timerTask: TimerTask? = null
   var TAG = "Timers"
   var Your_X_SECS: Long = 1
   var ID_COACH = ""
   private var notificationManager: NotificationManager? = null
   val default_notification_channel_id = "default"
   override fun onBind(arg0: Intent?): IBinder? {
      return null
   }

   override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
      Log.e(TAG, "onStartCommand")
      System.out.println("onstartcommand")
      super.onStartCommand(intent, flags, startId)
      ID_COACH = intent?.extras?.get("idCoach") as String
      notificationManager =
         getSystemService(
            NOTIFICATION_SERVICE
         ) as NotificationManager

      createNotificationChannel()
      startTimer()

      val notificationIntent = Intent(this, MainActivity::class.java)
      val pendingIntent = PendingIntent.getActivity(
         this,
         0, notificationIntent, FLAG_MUTABLE
      )
      val notification: Notification = NotificationCompat.Builder(this, "1")
         .setContentTitle("Urgence")
         .setContentText("Vous receverez les alertes des athlétes.")
         .setSmallIcon(R.drawable.ic_dialog_info)
         .setContentIntent(pendingIntent)
         .build()
      startForeground(1, notification)

      return START_STICKY
   }

   override fun onCreate() {
      Log.e(TAG, "onCreate")
   }

   override fun onDestroy() {
      Log.e(TAG, "onDestroy")
      stopTimerTask()
      notificationManager?.cancelAll()
      super.onDestroy()
   }

   //we are going to use a handler to be able to run in our TimerTask
   val handler: Handler = Handler()
   fun startTimer() {
      System.out.println("Starting Timer..")

      timer = Timer()
      initializeTimerTask()
      timer!!.schedule(timerTask, 5000, Your_X_SECS * 1000)
   }

   fun stopTimerTask() {
      if (timer != null) {
         timer!!.cancel()
         timer = null
      }
   }

   fun initializeTimerTask() {
      timerTask = object : TimerTask() {
         override fun run() {
            handler.post(Runnable { fetchEmergencyNotification() })
         }
      }
   }
   private fun createNotificationChannel() {
      System.out.println("Creating notification channel..")

      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(ID_COACH, "NotificationEmergency", importance)

      channel.enableLights(true)
      channel.lightColor = Color.RED
      channel.enableVibration(true)
      channel.vibrationPattern =
         longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
      notificationManager?.createNotificationChannel(channel)
   }

   private fun sendNotification(notifContent: UserCoachHeartRate) {
      System.out.println("Sending notification..")
      //notificationManager?.cancel(notifContent.userCoach.userProfile.userId);
      val mBuilder = NotificationCompat.Builder(applicationContext, default_notification_channel_id)
      mBuilder.setContentTitle("Urgence")
      mBuilder.setContentText("Le rythme cardiaque de "+notifContent.userCoach.userProfile.lastname+
              " "+notifContent.userCoach.userProfile.firstname+" est de "+notifContent.heartRate+ ".")
      mBuilder.setTicker("Notification d'urgence")
      mBuilder.setAutoCancel(true)
      mBuilder.setSmallIcon(R.drawable.ic_dialog_info)

      mBuilder.setChannelId(notifContent.userCoach.userProfile.userId.toString())

      notificationManager!!.notify(System.currentTimeMillis().toInt(), mBuilder.build())
   }

   private fun fetchEmergencyNotification() {
      val request: Call<UserCoachHeartRate> = RetrofitHelper.getInstance().create(GetAPI::class.java).fetchNotif(Integer.parseInt(ID_COACH))
      Log.i("request", request.request().url().toString()+ ", REQUEST TYPE : " + request.request().method())

      request.enqueue(object : Callback<UserCoachHeartRate?> {
         override fun onResponse(call: Call<UserCoachHeartRate?>, response: Response<UserCoachHeartRate?>) {
            Log.i("NOTIF", response.body().toString())
            response.body()?.let { sendNotification(it) }
         }

         override fun onFailure(call: Call<UserCoachHeartRate?>, t: Throwable) {
            Log.i("NOTIF", "Pas d'urgence")
         }
      })
   }

}