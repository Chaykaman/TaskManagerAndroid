package com.example.taskmanager.data.logger
import android.util.Log

object TaskLogger {

    private const val TAG = "TaskManager"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }
}