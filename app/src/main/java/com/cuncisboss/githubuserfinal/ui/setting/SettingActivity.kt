package com.cuncisboss.githubuserfinal.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            alarmOn()
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
            alarmOn()
            val repeatTime = tv_setTime.text.toString()
            val repeatMessage = getString(R.string.alarm_msg)
            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                repeatTime, repeatMessage)
        }

        btn_cancelAlarm.setOnClickListener {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            alarmOff()
            GithubPref.clear(this)
            tv_setTime.text = getString(R.string._00_00)
        }
    }

    private fun alarmOn() {
        switch_alarm.isChecked = true
        btn_setAlarm.setBackgroundResource(R.color.colorDarkGray)
        btn_cancelAlarm.setBackgroundResource(R.color.colorPrimary)
        btn_setAlarm.isEnabled = false
        btn_cancelAlarm.isEnabled = true
    }

    private fun alarmOff() {
        switch_alarm.isChecked = false
        btn_setAlarm.setBackgroundResource(R.color.colorPrimary)
        btn_cancelAlarm.setBackgroundResource(R.color.colorDarkGray)
        btn_setAlarm.isEnabled = true
        btn_cancelAlarm.isEnabled = false
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