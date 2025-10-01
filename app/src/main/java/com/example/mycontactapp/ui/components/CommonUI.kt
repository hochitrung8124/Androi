package com.example.mycontactapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar( // Đổi tên để tránh trùng với TopAppBar của Material
    title: String,
    canNavigateBack: Boolean = false,
    onNavigateUp: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Quay lại"
                    )
                }
            }
        }
        // Bạn có thể thêm actions ở đây nếu cần
    )
}

