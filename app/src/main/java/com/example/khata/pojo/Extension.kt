package com.example.khata.pojo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun toast(context: Context, msg: String?, duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(context, msg.toString(), duration).apply { show() }
}

fun exClearNewActivity(context: Context, java: Class<*>) {
    context.startActivity(
        Intent(context, java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    (context as Activity).finish()
}