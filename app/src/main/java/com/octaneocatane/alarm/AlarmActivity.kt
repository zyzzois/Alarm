package com.octaneocatane.alarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {
    lateinit var ringtone: Ringtone
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_alarm)

        var notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notificationUri)
        if(ringtone == null) { //если рингтон для будильника отсутсвует
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, notificationUri)
        }
        if(ringtone != null){
            ringtone.play()
        }
    }

    override fun onDestroy() {
        if(ringtone != null && ringtone.isPlaying) {
            ringtone.stop()
        }
    }
}