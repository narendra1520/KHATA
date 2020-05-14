package com.example.khata.pojo

import android.util.Patterns
import java.util.regex.Pattern
fun validEmail(string: String):String {
    if (string.isEmpty()){
        return "Field Required"
    }else if(!Patterns.EMAIL_ADDRESS.matcher(string).matches()){
        return "Enter valid email address"
    }else{
        return ""
    }
}

fun validName(string: String):Any {
    return if (string.isEmpty()){
        "Field Required"
    }else if(!Pattern.compile("^[a-zA-Z0-9\\s]*$").matcher(string).matches()){
        "Enter valid name (ex: nick)"
    }else{
        true
    }
}

fun validZeroMore(string: String):Any {
    return if(!Pattern.compile("^\\d{0,5}(\\d.\\d)?\\d?$").matcher(string).matches()){
        "Enter valid value (range: 0.0 - 100000.0)"
    }else {
        true
    }
}

fun validOneMore(string: String):Any {
    return if(!Pattern.compile("^\\d{0,5}(0.[1-9]|[1-9].[0-9])?\\d?$").matcher(string).matches()){
        "Enter valid value (range: 0.1 - 100000.0)"
    }else {
        true
    }
}

fun validPhone(string: String):Any {
    return if (string.isEmpty()){
        "Field Required"
    }else if(!Pattern.compile("^[0-9]{10}$").matcher(string).matches()){
        "Enter valid phone number"
    }else{
        true
    }
}

fun validNote(string: String):Any {
    return if (string.isEmpty()){
        true
    }else if(!Pattern.compile("^[a-zA-Z0-9.\\s,\\-]+$").matcher(string).matches()){
        "Enter valid note (allows: , . -)"
    }else if(string.length>40){
        "Note is too long (max: 40 character)"
    }else{
        true
    }
}

fun validUpi(string: String):Any {
    return if(!Pattern.compile("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$").matcher(string).matches()){
        "Enter valid upi id"
    }else {
        true
    }
}