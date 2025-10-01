package com.example.mycontactapp.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.mycontactapp.data.sampleContacts // Import sampleContacts
import com.example.mycontactapp.ui.components.AppTopBar
import com.example.mycontactapp.utils.makeCall
import android.content.pm.PackageManager // Explicit import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    navController: NavHostController,
    contactId: Int?
) {
    val context = LocalContext.current
    val contact = sampleContacts.find { it.id == contactId }

    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                contact?.phoneNumber?.let { makeCall(context, it) }
            } else {
                Toast.makeText(context, "Quyền gọi điện bị từ chối.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = contact?.name ?: "Chi Tiết Liên Hệ", // Hiển thị tên nếu có
                canNavigateBack = true,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa nội dung
            verticalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa các phần tử
        ) {
            if (contact != null) {
                Text("ID: ${contact.id}", style = MaterialTheme.typography.labelMedium)
                Text(contact.name, style = MaterialTheme.typography.headlineMedium)
                Text(contact.phoneNumber, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        when (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        )) {
                            PackageManager.PERMISSION_GRANTED -> {
                                makeCall(context, contact.phoneNumber)
                            }
                            else -> {
                                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f) // Nút không quá rộng
                ) {
                    Text("Gọi ${contact.name}")
                }
            } else {
                Text("Không tìm thấy thông tin liên hệ.")
            }
        }
    }
}

