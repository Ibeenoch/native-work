package com.jetli.vina

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jetli.vina.navigations.AppNavigation
import com.jetli.vina.ui.theme.AppTheme

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.provider.Telephony


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request necessary permissions on app startup
        requestSmsPermissions()

        // Log whether this app is the default SMS app
        logDefaultSmsAppStatus()

        setContent {
            // provide the viewmodel gloablly to the app

            MainScreen() // Call the extracted UI composable
        }
    }

    // Request SMS permissions for sending and reading
    private fun requestSmsPermissions() {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        if (!permissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }
    }

    // Log whether the app is the default SMS app
    private fun logDefaultSmsAppStatus() {
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
        if (defaultSmsApp == packageName) {
            Log.d("SMS", "This app is the default SMS app.")
        } else {
            Log.d("SMS", "This app is NOT the default SMS app. Default is: $defaultSmsApp")
        }
    }


    // Function to request becoming the default SMS app
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestDefaultSmsApp() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        if (!roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
            startActivityForResult(intent, 123)
        }
    }

    // Handle activity result to check if the app is the default SMS app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Log.d("SMS", "App is now the default SMS app")
        } else {
            Log.e("SMS", "Failed to become the default SMS app")
        }
    }

}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Apply background color globally
        ) {
            AppNavigation()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
