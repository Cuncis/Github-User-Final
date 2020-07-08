package com.cuncisboss.githubuserfinal.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.data.local.GithubPref
import com.cuncisboss.githubuserfinal.data.service.AlarmReceiver
import com.cuncisboss.githubuserfinal.util.TimePickerFragment
import com.cuncisboss.githubuserfinal.util.Utils.hideView
import com.cuncisboss.githubuserfinal.util.Utils.showView
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
        const val TAG = "_logMainAct"
    }

    lateinit var alarmReceiver: AlarmReceiver
    var isSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (GithubPref.getStrTime(this) != "") {
            tv_setTime.text = GithubPref.getStrTime(this)
        }

        alarmReceiver = AlarmReceiver()

        initListener()
    }

    private fun initListener() {
        if (alarmReceiver.isAlarmSet(this, AlarmReceiver.TYPE_REPEATING)) {
            switch_alarm.isChecked = true
        }
        if (switch_alarm.isChecked) {
            linear_alarm.showView()
        } else {
            linear_alarm.hideView()
        }
        switch_alarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                linear_alarm.showView()
            } else {
                linear_alarm.hideView()
            }
        }

        tv_setTime.setOnClickListener {
            val timePickerFragmentRepeat = TimePickerFragment()
            timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
        }

        btn_setAlarm.setOnClickListener {
            val repeatTime = tv_setTime.text.toString()
            val repeatMessage = getString(R.string.alarm_msg)
            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                repeatTime, repeatMessage)
        }

        btn_cancelAlarm.setOnClickListener {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            switch_alarm.isChecked = false
            GithubPref.clear(this)
            tv_setTime.text = getString(R.string._00_00)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        if (GithubPref.getStrTime(this) == "") {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, GithubPref.getTime(this).hour.toInt())
            calendar.set(Calendar.MINUTE, GithubPref.getTime(this).minute.toInt())
        }

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_REPEAT_TAG -> {
                val time = dateFormat.format(calendar.time)
                tv_setTime.text = time
                GithubPref.setTime(this, time)
            }
        }
    }
}