package com.example.asgn1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var etBillAmount: EditText
    lateinit var rgTipPercentages: RadioGroup
    lateinit var rb10: RadioButton
    lateinit var rb15: RadioButton
    lateinit var rb20: RadioButton
    lateinit var btnCalculateTip: Button
    lateinit var tvCalculatedTip: TextView
    lateinit var totalAmount: TextView
    val PREFS_NAME = "tipCalculatorPrefs"
    val PREF_BILL_AMOUNT = "billAmount"
    val PREF_TIP_PERCENTAGE = "tipPercentage"
    val PREF_TIP_AMOUNT = "tipAmount"
    val PREF_TOTAL_AMOUNT = "totalAmount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBillAmount = findViewById(R.id.etBillAmount)
        rgTipPercentages = findViewById(R.id.rgTipPercentages)
        rb10 = findViewById(R.id.rb10)
        rb15 = findViewById(R.id.rb15)
        rb20 = findViewById(R.id.rb20)
        btnCalculateTip = findViewById(R.id.btnCalculateTip)
        tvCalculatedTip = findViewById(R.id.tvCalculatedTip)
        totalAmount = findViewById(R.id.tvCalculatedTotal)

        loadPreferences()

        btnCalculateTip.setOnClickListener {
            calculateTip()
        }
    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
    }
    override fun onPause() {
        super.onPause()
        var selectedTipPercentage = when (rgTipPercentages.checkedRadioButtonId) {
            R.id.rb10 -> 0.10
            R.id.rb15 -> 0.15
            R.id.rb20 -> 0.20
            else -> 0.0
        }
        savePreferences(etBillAmount.text.toString(), selectedTipPercentage,
                        tvCalculatedTip.text.toString(),totalAmount.text.toString())

    }

    private fun calculateTip() {
        var billAmountString = etBillAmount.text.toString()
        if (billAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter a bill amount", Toast.LENGTH_SHORT).show()
            return
        }

        var billAmount = billAmountString.toDouble()
        var selectedTipPercentage = when (rgTipPercentages.checkedRadioButtonId) {
            R.id.rb10 -> 0.10
            R.id.rb15 -> 0.15
            R.id.rb20 -> 0.20
            else -> 0.0
        }

        if (selectedTipPercentage == 0.0) {
            Toast.makeText(this, "Please select a tip percentage", Toast.LENGTH_SHORT).show()
            return
        }
        var tipAmount = billAmount * selectedTipPercentage
        var calculatedAmount =  tipAmount + billAmount
        tvCalculatedTip.text ="%.2f".format(tipAmount).toString()
        totalAmount.text = "%.2f".format(calculatedAmount).toString()
        savePreferences(billAmountString, selectedTipPercentage,tvCalculatedTip.text.toString(),totalAmount.text.toString())
    }

    private fun savePreferences(billAmount: String, tipPercentage: Double,tipAmount:String,totalAmount:String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_BILL_AMOUNT, billAmount)
        editor.putInt(PREF_TIP_PERCENTAGE, (tipPercentage * 100).toInt())
        editor.putString(PREF_TIP_AMOUNT, tipAmount)
        editor.putString(PREF_TOTAL_AMOUNT,totalAmount)
        editor.apply()
    }

    private fun loadPreferences() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val billAmount = sharedPreferences.getString(PREF_BILL_AMOUNT, "0.00")
        val tipPercentage = sharedPreferences.getInt(PREF_TIP_PERCENTAGE, 0)
        val tipAmount = sharedPreferences.getString(PREF_TIP_AMOUNT,"0.00")
        val calculatedAmount = sharedPreferences.getString(PREF_TOTAL_AMOUNT,"0.00")

        etBillAmount.setText(billAmount)
        tvCalculatedTip.setText(tipAmount)
        when (tipPercentage) {
            10 -> rgTipPercentages.check(R.id.rb10)
            15 -> rgTipPercentages.check(R.id.rb15)
            20 -> rgTipPercentages.check(R.id.rb20)
        }
        totalAmount.setText(calculatedAmount)
    }
}