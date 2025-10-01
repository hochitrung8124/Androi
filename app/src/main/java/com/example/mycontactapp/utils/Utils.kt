package com.example.mycontactapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun makeCall(context: Context, phoneNumber: String) {
    if (phoneNumber.isBlank()) {
        Toast.makeText(context, "Số điện thoại không hợp lệ.", Toast.LENGTH_SHORT).show()
        return
    }
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$phoneNumber")
    try {
        context.startActivity(intent)
    } catch (e: SecurityException) {
        Toast.makeText(context, "Không có quyền thực hiện cuộc gọi. Vui lòng cấp quyền trong cài đặt.", Toast.LENGTH_LONG).show()
        e.printStackTrace()
        // Cân nhắc mở cài đặt ứng dụng để người dùng cấp quyền
        // val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        // settingsIntent.data = Uri.parse("package:" + context.packageName)
        // context.startActivity(settingsIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Không thể thực hiện cuộc gọi.", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

