package com.example.basiccalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var lastNumeric = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View) {
        tvInput.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View) {
        tvInput.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onEquals(view: View) {
        if (lastNumeric) {
            var tvValue = tvInput.text
            var result: String? = null
            var prefix: String? = null

            try {
                if (tvValue.startsWith("-")){
                    tvValue = tvValue.substring(1)
                    prefix = "-"
                }
                with(tvValue) {
                    when {
                        contains("-") ->
                            result = getNumbersFromString(tvValue, "-", prefix)
                                .reduce { a, b -> a - b }.toString()
                        contains("+") ->
                            result = getNumbersFromString(tvValue, "+", prefix)
                                .reduce { a, b -> a + b }.toString()
                        contains("/") ->
                            result = getNumbersFromString(tvValue, "/", prefix)
                                .reduce { a, b -> a / b }.toString()
                        contains("*") ->
                            result = getNumbersFromString(tvValue, "*", prefix)
                                .reduce { a, b -> a * b }.toString()
                    }
                }
                tvInput.text = result
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            } finally {
                lastNumeric = true
                lastDot = true
            }
        }
    }

    fun onDot(view: View) {
        if (lastNumeric && !lastDot) {
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOp(view: View) {
        if (lastNumeric && !isOperatorAdded(tvInput.text.toString())) {
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }

    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains(Regex("[+\\-*/]"))
        }
    }

    private fun getNumbersFromString(value: CharSequence, operator: String, prefix: String?): List<Double>{
        return value.split(operator)
                .mapIndexed{i, v -> if (i == 0) {prefix?.plus(v) ?: v} else v} // Add the prefix? to the first element
                .map { v -> v.toDouble() }
    }
}
