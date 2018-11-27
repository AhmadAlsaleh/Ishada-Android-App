package com.crazy_iter.ishada.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.crazy_iter.ishada.R
import com.crazy_iter.ishada.StaticMethods
import kotlinx.android.synthetic.main.dialog_choose_year_month.*

class ChooseYearMonthDialog(context: Context,
                            private val years: ArrayList<Int>,
                            private val year: Int, private val month: Int): Dialog(context) {

    var isDone = false
    var selectedYearOut = year
    var selectedMonthOut = month

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_year_month)

        selectedYear.text = year.toString()
        selectedMonth.text = StaticMethods.getMonthFromNumber(month)

        val yearsString = ArrayList<String>()
        for (y in years) {
            yearsString.add(y.toString())
        }

        val monthsString = ArrayList<String>()
        for (m in 1..12) {
            monthsString.add(StaticMethods.getMonthFromNumber(m))
        }

        yearsLV.adapter = ArrayAdapter(context, R.layout.linear_list, R.id.linearListTV, yearsString)
        yearsLV.setOnItemClickListener { parent, view, position, id ->
            selectedYearOut = years[position]
            selectedYear.text = yearsString[position]
        }

        monthsLV.adapter = ArrayAdapter(context, R.layout.linear_list, R.id.linearListTV, monthsString)
        monthsLV.setOnItemClickListener { parent, view, position, id ->
            selectedMonthOut = StaticMethods.getMonthFromName(monthsString[position])
            selectedMonth.text = monthsString[position]
        }

        dateCancelBTN.setOnClickListener {
            isDone = false
            dismiss()
        }

        dateDoneBTN.setOnClickListener {
            isDone = true
            dismiss()
        }

    }
}