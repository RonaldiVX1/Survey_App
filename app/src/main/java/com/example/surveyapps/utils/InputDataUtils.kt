package com.example.surveyapps.utils

import android.widget.EditText


fun checkData(
    editText1: EditText?,
    editText2: EditText?,
    editText3: EditText?,
    editText4: EditText?,
    editText5: EditText?,
    editText6: EditText?,
    editText7: EditText?,
    editText8: EditText?,
): Boolean {
    if (editText1?.text!!.isEmpty() || editText2?.text!!.isEmpty() || editText3?.text!!.isEmpty() || editText4?.text!!.isEmpty() || editText5?.text!!.isEmpty() || editText6?.text!!.isEmpty() || editText7?.text!!.isEmpty() || editText8?.text!!.isEmpty()){
        return false
    } else  {
        return true
    }
}