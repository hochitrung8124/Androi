package com.example.mycontactapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycontactapp.R
import com.example.mycontactapp.data.Contact // Import Contact
import com.example.mycontactapp.data.sampleContacts // Import sampleContacts
import com.example.mycontactapp.navigation.AppDestinations
import com.example.mycontactapp.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(navController: NavHostController) {
    val context = LocalContext.current // Giữ context nếu cần cho Toast hoặc các việc khác sau này
    var nameInput by remember { mutableStateOf(TextFieldValue()) }
    var phoneNumberInput by remember { mutableStateOf(TextFieldValue()) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddContactDialog by remember { mutableStateOf(false) }

    val filteredContacts = if (searchQuery.isBlank()) {
        sampleContacts
    } else {
        sampleContacts.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.phoneNumber.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Danh Bạ Điện Thoại") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddContactDialog = true },
                containerColor = colorResource(id = R.color.green),
                contentColor = Color.White,
                shape = RoundedCornerShape(100)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm liên hệ")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm kiếm liên hệ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Biểu tượng tìm kiếm"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredContacts.size) { index ->
                    val contact = filteredContacts[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("${AppDestinations.CONTACT_DETAIL_ROUTE}/${contact.id}")
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = contact.name, fontSize = 18.sp)
                        Text(text = contact.phoneNumber, fontSize = 16.sp, color = Color.Gray)
                    }
                    Divider() // Thêm đường kẻ phân cách
                }
            }
        }
    }

    if (showAddContactDialog) {
        AlertDialog(
            onDismissRequest = { showAddContactDialog = false },
            title = { Text("Thêm Liên Hệ Mới") },
            text = {
                Column {
                    TextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Tên liên hệ") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                    TextField(
                        value = phoneNumberInput,
                        onValueChange = { phoneNumberInput = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                        // keyboardType = KeyboardType.Phone // Có thể thêm
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.text.isNotBlank() && phoneNumberInput.text.isNotBlank()) {
                            val newContact = Contact(
                                id = (sampleContacts.maxOfOrNull { it.id } ?: 0) + 1,
                                name = nameInput.text,
                                phoneNumber = phoneNumberInput.text
                            )
                            sampleContacts.add(newContact)
                            nameInput = TextFieldValue("")
                            phoneNumberInput = TextFieldValue("")
                            showAddContactDialog = false
                        }
                    }
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddContactDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

