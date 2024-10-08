package com.jetli.vina.utils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast

fun onClickNextButton(context: Context, phoneNumber: String, message: String) {

    val smsManager = SmsManager.getDefault()

    // Define sent and delivery intent actions
    val SENT = "SMS_SENT"
    val DELIVERED = "SMS_DELIVERED"

    // Create pending intents for sending and delivery statuses
    val sentIntent = PendingIntent.getBroadcast(context, 0, Intent(SENT),
        PendingIntent.FLAG_IMMUTABLE)
    val deliveredIntent = PendingIntent.getBroadcast(context, 0, Intent(DELIVERED),
        PendingIntent.FLAG_IMMUTABLE)

    // Register BroadcastReceiver for sent status
    context.registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (resultCode) {
                // Result when SMS is successfully sent
                android.app.Activity.RESULT_OK -> {
                    Log.d("SMS", "SMS sent successfully")
                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show()

                    // Call function to delete SMS after it's sent
                    deleteSentSMS(context, phoneNumber)
                }
                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                    Log.e("SMS", "Generic failure occurred")
                    Toast.makeText(context, "SMS failed: Generic failure", Toast.LENGTH_SHORT).show()
                }
                SmsManager.RESULT_ERROR_NO_SERVICE -> {
                    Log.e("SMS", "No service available")
                    Toast.makeText(context, "SMS failed: No service", Toast.LENGTH_SHORT).show()
                }
                SmsManager.RESULT_ERROR_NULL_PDU -> {
                    Log.e("SMS", "Null PDU")
                    Toast.makeText(context, "SMS failed: Null PDU", Toast.LENGTH_SHORT).show()
                }
                SmsManager.RESULT_ERROR_RADIO_OFF -> {
                    Log.e("SMS", "Radio is off")
                    Toast.makeText(context, "SMS failed: Radio is off", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }, IntentFilter(SENT))

    // Register BroadcastReceiver for delivery status (optional)
    context.registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (resultCode) {
                android.app.Activity.RESULT_OK -> {
                    Log.d("SMS", "SMS delivered successfully")
                    Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show()
                }
                android.app.Activity.RESULT_CANCELED -> {
                    Log.e("SMS", "SMS delivery failed")
                    Toast.makeText(context, "SMS delivery failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }, IntentFilter(DELIVERED))

    // Check if permission to send SMS is granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
        try {
            // Send SMS using SmsManager
            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent)
            Log.d("SMS", "Sending SMS to $phoneNumber...")
        } catch (e: Exception) {
            Log.e("SMS", "Failed to send SMS: ${e.message}")
        }
    } else {
        // Request permission to send SMS
        ActivityCompat.requestPermissions(
            (context as android.app.Activity),
            arrayOf(Manifest.permission.SEND_SMS),
            1
        )
    }
}

fun deleteSentSMS(context: Context?, phoneNumber: String) {
    if (context == null) return
    val contentResolver: ContentResolver = context.contentResolver
    val uriSms = Uri.parse("content://sms/sent")

    // Query to get SMS based on the phone number
    val cursor = contentResolver.query(uriSms, null, null, null, null)
    cursor?.use {
        while (it.moveToNext()) {
            val address = it.getString(it.getColumnIndexOrThrow("address"))
            if (address == phoneNumber) {
                val id = it.getString(it.getColumnIndexOrThrow("_id"))
                // Attempt to delete the SMS from the SMS database
                val rowsDeleted = contentResolver.delete(Uri.parse("content://sms/$id"), null, null)
                if (rowsDeleted > 0) {
                    Log.d("SMS", "Deleted sent SMS with id $id for $phoneNumber")
                    Toast.makeText(context, "Deleted sent SMS", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("SMS", "Failed to delete SMS with id $id for $phoneNumber")
                }
            }
        }
    }
}
