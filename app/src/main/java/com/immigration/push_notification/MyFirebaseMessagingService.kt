package com.immigration.push_notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.text.Html
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.immigration.R
import com.immigration.utils.Utils
import com.immigration.utils.Utils.getBitmapFromURL
import com.immigration.view.notification.NotificationActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
   private val TAG = MyFirebaseMessagingService::class.java.name
   companion object { var NOTIFICATION_ID = 1 }
   
   override fun onMessageReceived(remoteMessage: RemoteMessage?) {
      Utils.log(TAG, "From: " + remoteMessage!!.from)
      
      if (remoteMessage.data.isNotEmpty()) {
         
         val title = remoteMessage.data["title"]
         val message = remoteMessage.data["message"]
         val click_action_open = remoteMessage.data["click_action"]
         val action_url = remoteMessage.data["action_url"]
         //  String userid = remoteMessage.getData().get("userid");
         val userid = ""
         
         Utils.log(TAG, "data: title: $title ,message:$message , " +
          "click_action_open:$click_action_open, action_url: $action_url ,userid: $userid ")
         

         if (click_action_open == "1") {
            click_action_open1(title!!, message!!, "https://play.google.com/store/apps/details?id=" + action_url!!)
         }
         if (click_action_open == "2") {
            click_action_open1(title!!, message!!, action_url!!)
         }
         if (click_action_open == "3") {
            notificationWithMessgae(title!!, message!!)
         }
         if (click_action_open == "4") {
            val bitmap = getBitmapFromURL(action_url!!)
            notificationWithImage(bitmap, title!!, message!!)
         }
      }
   }


   private fun click_action_open1(title: String, message: String, link: String) {
      val pendingIntent: PendingIntent
      val intent = Intent(Intent.ACTION_VIEW)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      intent.data = Uri.parse(link)

      pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
      val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      val mNotifyBuilder = NotificationCompat.Builder(this, "")
       .setAutoCancel(false)
       .setContentTitle(title)
       .setContentIntent(pendingIntent)
       .setSound(defaultSoundUri)
       .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
       .setSmallIcon(R.drawable.ic_notification)
       .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification))
       .setContentText(message)
      val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

      if (NOTIFICATION_ID > 1073741824) {
         NOTIFICATION_ID = 0
      }
      notificationManager.notify(NOTIFICATION_ID++, mNotifyBuilder.build())
   }

   private fun notificationWithImage(bitmap: Bitmap?, title: String, message: String) {
      val intent = Intent(this, NotificationActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
      val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      val bigPictureStyle = NotificationCompat.BigPictureStyle()
      bigPictureStyle.setBigContentTitle(title)
      bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
      bigPictureStyle.bigPicture(bitmap)
      val mNotifyBuilder = NotificationCompat.Builder(this, "")
       .setAutoCancel(false)
       .setContentTitle(title)
       .setContentIntent(pendingIntent)
       .setSound(defaultSoundUri)
       .setStyle(bigPictureStyle)
       .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
       .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
       .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification))
       .setContentText(message)
      val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

      if (NOTIFICATION_ID > 1073741824) {
         NOTIFICATION_ID = 0
      }
      notificationManager.notify(NOTIFICATION_ID++, mNotifyBuilder.build())
   }

   private fun notificationWithMessgae(title: String, message: String) {
      val intent = Intent(this, NotificationActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      // intent.putExtra("userid",userid);
      val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
      val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      val mNotifyBuilder = NotificationCompat.Builder(this, "")
       .setAutoCancel(false)
       .setContentTitle(title)
       .setContentIntent(pendingIntent)
       .setSound(defaultSoundUri)
       .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
       .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
       .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification))
       .setContentText(message)
      val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

      if (NOTIFICATION_ID > 1073741824) {
         NOTIFICATION_ID = 0
      }
      notificationManager.notify(NOTIFICATION_ID++, mNotifyBuilder.build())
   }

  
}
