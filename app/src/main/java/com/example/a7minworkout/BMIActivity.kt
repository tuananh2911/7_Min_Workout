package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private var binding : ActivityBmiBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding?.toobarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toobarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.btnCalculate?.setOnClickListener {
            if (checkValid()){
                showBmiResult(calculateBmi(binding?.etMetricUnitWeight?.text.toString().toFloat(),binding?.etMetricUnitHeight?.text.toString().toFloat() / 100))
            }else{
                if(validateUsUnits()){
                    val usUnitHeightValueFeet: String =
                        binding?.etUsMetricUnitHeightFeet?.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        binding?.etUsMetricUnitHeightInch?.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = binding?.etUsMetricUnitWeight?.text.toString()
                        .toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    showBmiResult(bmi)
                }else{
                    Toast.makeText(this,"Please enter valid values.",Toast.LENGTH_SHORT).show()
                }

            }
        }
        binding?.rbMetricUnits?.setOnClickListener {
            binding?.rbMetricUnits?.isChecked = true
            binding?.itWeight?.visibility = View.VISIBLE
            binding?.itHeight?.visibility = View.VISIBLE
            binding?.tilUsMetricUnitWeight?.visibility = View.INVISIBLE
            binding?.tilMetricUsUnitHeightFeet?.visibility = View.INVISIBLE
            binding?.tilMetricUsUnitHeightInch?.visibility = View.INVISIBLE
        }
        binding?.rbUsUnits?.setOnClickListener {
            binding?.rbUsUnits?.isChecked = true
            binding?.itWeight?.visibility = View.INVISIBLE
            binding?.itHeight?.visibility = View.INVISIBLE
            binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
            binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
            binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE
        }
    }
    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }
    fun checkValid():Boolean{
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            return false
        }
        if (binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            return false
        }
        return true
    }
    fun calculateBmi(w : Float, h : Float):Float{
        val bmi = w / (h * h)
        return bmi
    }
    fun showBmiResult(bmi :Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding?.tvYourBMI?.visibility = View.VISIBLE
        binding?.tvResultBMIByNumber?.text = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.tvResultBMIByString?.text = bmiLabel
        binding?.tvSuggest?.text = bmiDescription
        binding?.tvResultBMIByNumber?.visibility = View.VISIBLE
        binding?.tvResultBMIByString?.visibility = View.VISIBLE
        binding?.tvSuggest?.visibility = View.VISIBLE
    }

}