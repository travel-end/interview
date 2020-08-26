package com.journey.interview.customizeview.datetimer.demo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.journey.interview.R
import com.journey.interview.customizeview.datetimer.core.DateTimePicker
import com.journey.interview.customizeview.datetimer.dialog.CardDatePickerDialog
import com.journey.interview.utils.StringUtils
import kotlinx.android.synthetic.main.activity_date_timer_picker.*

class DateTimerPickerActivity : AppCompatActivity() {
    private var maxDate: Long = 0
    private var minDate: Long = 0
    private var defaultDate: Long = 0

    private lateinit var context: Context

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_timer_picker)
        context = this
        tvMaxDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET MAX DATE")
                .setOnChoose(listener = object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                        maxDate = millisecond
                        tvMaxDate.text = StringUtils.conversionTime(millisecond, "yyyy-MM-dd HH:mm")
                    }
                }).build().show()

        }

        btnClearMax.setOnClickListener {
            maxDate = 0L
            tvMaxDate.text = ""
        }

        tvMinDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET MIN DATE")
                .setOnChoose(listener = object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                        minDate = millisecond
                        tvMinDate.text = StringUtils.conversionTime(millisecond, "yyyy-MM-dd HH:mm")
                    }
                }).build().show()
        }

        btnClearMin.setOnClickListener {
            minDate = 0L
            tvMinDate.text = ""
        }

        tvDefaultDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET DEFAULT DATE")
                .setOnChoose(listener = object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                        defaultDate = millisecond
                        tvDefaultDate.text = StringUtils.conversionTime(millisecond, "yyyy-MM-dd HH:mm")
                    }
                }).build().show()
        }

        btnClearDefault.setOnClickListener {
            defaultDate = 0L
            tvDefaultDate.text = ""
        }


        btnCardDialogShow.setOnClickListener {

            var displayList = mutableListOf<Int>()
            if (checkYear.isChecked)
                displayList.add(DateTimePicker.YEAR)
            if (checkMonth.isChecked)
                displayList.add(DateTimePicker.MONTH)
            if (checkDay.isChecked)
                displayList.add(DateTimePicker.DAY)
            if (checkHour.isChecked)
                displayList.add(DateTimePicker.HOUR)
            if (checkMin.isChecked)
                displayList.add(DateTimePicker.MIN)

            var model = CardDatePickerDialog.CARD
            if (radioModelCard.isChecked)
                model = CardDatePickerDialog.CARD
            if (radioModelCube.isChecked)
                model = CardDatePickerDialog.CUBE
            if (radioModelStack.isChecked)
                model = CardDatePickerDialog.STACK
            if (radioModelCustom.isChecked)
                model = R.drawable.shape_bg_dialog_custom

            CardDatePickerDialog.builder(context)
                .setTitle("CARD DATE PICKER DIALOG")
                .setDisplayType(displayList)
                .setBackGroundModel(model)
                .showBackNow(checkBackNow.isChecked)
                .setDefaultTime(defaultDate)
                .setMaxTime(maxDate)
                .setMinTime(minDate)
                .setThemeColor(if (model == R.drawable.shape_bg_dialog_custom) Color.parseColor("#FF8000") else 0)
                .showDateLabel(checkUnitLabel.isChecked)
                .setLabelText("年","月","日","时","分")
                .showFocusDateInfo(checkDateInfo.isChecked)
                .setOnChoose("选择",object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                        tvChooseDate.text = "◉  ${StringUtils.conversionTime(millisecond, "yyyy-MM-dd HH:mm")}    ${StringUtils.getWeek(millisecond)}"
                    }
                })
                .setOnCancel("关闭",object :CardDatePickerDialog.OnCancelListener{
                    override fun onCancel() {

                    }
                }).build().show()
        }

        //dateTimePicker使用示例
//        dateTimePicker.setLabelText(" Y"," M"," D"," Hr"," Min")
        dateTimePicker.setOnDateTimeChangedListener(object : DateTimePicker.OnDateTimeChangedListener{
            override fun onDateTimeChanged(view: DateTimePicker?, millisecond: Long) {
                tvDateTimePickerValue.text = "${StringUtils.conversionTime(millisecond, "yyyy年MM月dd日 HH时mm分")}  ${StringUtils.getWeek(millisecond)}"
            }
        })

    }
}
