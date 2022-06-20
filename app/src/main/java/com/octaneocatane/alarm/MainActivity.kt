package com.octaneocatane.alarm

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.octaneocatane.alarm.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    var sdf = SimpleDateFormat("HH:mm", Locale.getDefault())


    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChanel()
    }

    fun onClickSetAlarm(view: View){
        val picker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select alarm time")
            .build()

        picker.addOnPositiveButtonClickListener {
            calendar = Calendar.getInstance()
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)


            //binding.textTime.text = "${picker.hour} + ${picker.minute}"
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val alarmClockInfo = AlarmClockInfo(calendar.timeInMillis, getAlarmInfoPendingIntent())

            alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
            Toast.makeText(this, "Будильник установлен на ${sdf.format(calendar.time)}", Toast.LENGTH_SHORT).show()
        }
        picker.show(supportFragmentManager, "tag_picker")

        /*
        val intent = Intent(this, AlarmReciever::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
        */
    }

    /*
    fun onClickCancelAlarm(view: View) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReciever::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show()
    }*/

    /*private fun showTimePicker() {
        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = picker.hour
        calendar[Calendar.MINUTE] = picker.minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }
     */

    //Интент который перенаправляет пользователя на наш интент который мы установили
    private fun getAlarmInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(this, MainActivity::class.java)
        //Зачем нужны эти 2 флага? Мы не знаем в момента запусска Активити будет ли активно
        // наше приложение Если оно не будет активно, то у Интента должен быть обязательно флаг -
        //FLAG_ACTIVITY_NEW_TASK для того чтобы указать Андроиду на то, что нужно создать на пустом
        // месте новый таск в котором наше активити будет запущено. Если этот флаг не указать то
        //Активити не сможет запуститься если приложение в данный момент не запущено
        //А если приложение запущено, то мы хотим сделать так чтобы почистить стэк и текущий
        // экземпляр MainACtivity оказался бы на вершине
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        //Теперь созданный Интент нужно обернуть в PendingIntent
        return  PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //Обрабатываем дейтствите того что будет во время того как будилньик срабатывает
    private fun getAlarmActionPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //PendingIntent.FLAG_UPDATE_CURRENT - если такой уже есть, то заменить его новым
    }
    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //O - oreo
            val name: CharSequence = "mychanelReminder" //name of the chanel
            val description = "Channel for AlarmManager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel  = NotificationChannel("octane", name, importance)
            channel.description = description //description for this channel
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}