package com.example.pecode_test

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pecode_test.databinding.ActivityMainBinding
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        }
//                        else {
//                            showSettingDialog()
//                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

//    private fun showSettingDialog() {
//        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
//            .setTitle("Notification Permission")
//            .setMessage("Notification permission is required, Please allow notification permission from setting")
//            .setPositiveButton("Ok") { _, _ ->
//                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.parse("package:$packageName")
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    var hasNotificationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createNotificationButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 33) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                hasNotificationPermissionGranted = true
            }
        }

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "This is a channel for notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}