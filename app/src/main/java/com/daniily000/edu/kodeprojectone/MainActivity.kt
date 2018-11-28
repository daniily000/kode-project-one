package com.daniily000.edu.kodeprojectone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {


    // aliases
    private lateinit var op1: EditText
    private lateinit var op2: EditText
    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var mul: Button
    private lateinit var div: Button
    private lateinit var rst: TextView

    private fun aliases() {
        op1 = operand_1_edit_text
        op2 = operand_2_edit_text
        add = add_button
        sub = subtract_button
        mul = multiply_button
        div = divide_button
        rst = result_text_view
    }


    // Activity lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        aliases()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(FIRST_OPERAND, op1.text.toString())
        outState?.putString(SECOND_OPERAND, op2.text.toString())
        outState?.putString(RESULT_TEXT, rst.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            op1.setText(savedInstanceState.getString(FIRST_OPERAND, ""))
            op2.setText(savedInstanceState.getString(SECOND_OPERAND, ""))
            rst.text = savedInstanceState.getString(RESULT_TEXT, "")
        }
    }


    // XML onClicks

    // FIXME: Weak localization
    fun onCalculate(v: View) {

        if (v is Button) {

            var result = "Unexpected error has been experienced"

            when (checkOperands()) {

                OPERANDS_OK -> {
                    try {
                        result = op1.text.toString() +
                                " " + v.text + " " +
                                op2.text.toString() +
                                " = " +
                                when (v) {
                                    add -> (op1.double() + op2.double()).fmt()
                                    sub -> (op1.double() - op2.double()).fmt()
                                    mul -> (op1.double() * op2.double()).fmt()
                                    div -> if (op2.double() == BigDecimal.ZERO) "Not a number" else (op1.double() / op2.double()).fmt()
                                    else -> ""
                                }

                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }
                }

                OPERANDS_FIRST_FAILED -> {
                    result = "First operand is incorrect. Please check it for misspelling"
                }

                OPERANDS_SECOND_FAILED -> {
                    result = "First operand is incorrect. Please check it for misspelling"
                }

                OPERANDS_BOTH_FAILED -> {
                    result = "Both operands have failed to be parsed! Please check it for misspelling"
                }

                else -> {
                    Log.wtf(TAG, "Else in onCalculate - THIS SHOULD NOT HAPPEN")
                }
            }

            showResultText(result)

        } else {
            Log.e(TAG, "Unexpected call of onCalculate with wrong parameter - aborting")
        }
    }


    // Utils

    /**
     * Checks both operands for correct input.
     *
     * @return OPERANDS_OK if both OK, similar constants otherwise
     */
    private fun checkOperands(o1: EditText = op1, o2: EditText = op2): Int {
        var result = 0
        if (o1.text.toString().toDoubleOrNull() == null) result = result or OPERANDS_FIRST_FAILED
        if (o2.text.toString().toDoubleOrNull() == null) result = result or OPERANDS_SECOND_FAILED

        return result
    }

    /**
     * Sets result to view after an operation
     * Uses XML
     * @param text text to be shown
     */
    private fun showResultText(text: String) {
        rst.text = text
    }

    /**
     * Ease an access to editText.text.toString.toDouble()
     */
    private fun EditText.double() = this.text.toString().toBigDecimal()

    /**
     * Format a double to look better
     */
    private fun BigDecimal.fmt() = this.toPlainString()

    // Misc

    companion object {

        private const val TAG = "MainActivity"
        private const val FIRST_OPERAND = "FirstOperand"
        private const val SECOND_OPERAND = "SecondOperand"
        private const val RESULT_TEXT = "ResultText"

        private const val OPERANDS_OK = 0x00
        private const val OPERANDS_FIRST_FAILED = 0x01
        private const val OPERANDS_SECOND_FAILED = 0x10
        private const val OPERANDS_BOTH_FAILED = 0x11

    }
}
