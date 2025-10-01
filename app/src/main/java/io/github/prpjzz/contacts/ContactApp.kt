package io.github.prpjzz.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import java.io.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isSearchExpanded: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onSearchClose: () -> Unit
) {
    TopAppBar(
        title = {
            if (isSearchExpanded) {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = { Text("Tìm kiếm liên hệ...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("Danh Bạ Điện Thoại")
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isSearchExpanded) {
                    onSearchClose()
                } else {
                    /* Xử lý nút quay lại */
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = if (isSearchExpanded) "Đóng tìm kiếm" else "Quay lại"
                )
            }
        },
        actions = {
            if (!isSearchExpanded) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                }
            }
        },
        modifier = Modifier.background(Color.Blue)
    )
}

data class Contact(val id: Int, val name: String, val phoneNumber: String)

val sampleContacts = mutableStateListOf(
    Contact(1, "Nguyen Van A", "0123456789"),
    Contact(2, "Le Thi B", "0987654321"),
    Contact(3, "Tran Van C", "0121987654")
)

@Composable
fun AddContactDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddContact: (String, String) -> Unit
) {
    if (showDialog) {
        var name by remember { mutableStateOf(TextFieldValue()) }
        var phoneNumber by remember { mutableStateOf(TextFieldValue()) }

        AlertDialog(
            onDismissRequest = {
                onDismiss()
                name = TextFieldValue("")
                phoneNumber = TextFieldValue("")
            },
            title = { Text("Thêm Liên Hệ Mới") },
            text = {
                Column {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Tên liên hệ") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.text.isNotBlank() && phoneNumber.text.isNotBlank()) {
                            onAddContact(name.text, phoneNumber.text)
                            onDismiss()
                            name = TextFieldValue("")
                            phoneNumber = TextFieldValue("")
                        }
                    }
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    name = TextFieldValue("")
                    phoneNumber = TextFieldValue("")
                }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun ContactListScreen() {
    val context = LocalContext.current
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Lọc danh sách liên hệ theo từ khóa tìm kiếm
    val filteredContacts = remember(searchText) {
        if (searchText.isBlank()) {
            sampleContacts
        } else {
            sampleContacts.filter { contact ->
                contact.name.contains(searchText, ignoreCase = true) ||
                        contact.phoneNumber.contains(searchText)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Thanh ứng dụng
            TopBar(
                isSearchExpanded = isSearchExpanded,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchClick = { isSearchExpanded = true },
                onSearchClose = {
                    isSearchExpanded = false
                    searchText = ""
                }
            )

            // Contact list section
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredContacts.size) { index ->
                    val contact = filteredContacts[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedContact = contact },
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = contact.name, fontSize = 18.sp)
                        Text(text = contact.phoneNumber, fontSize = 16.sp)
                    }
                }

                // Hiển thị thông báo khi không tìm thấy kết quả
                if (filteredContacts.isEmpty() && searchText.isNotBlank()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không tìm thấy liên hệ nào",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Thêm liên hệ"
            )
        }
    }

    // Add Contact Dialog
    AddContactDialog(
        showDialog = showAddDialog,
        onDismiss = { showAddDialog = false },
        onAddContact = { name, phoneNumber ->
            val newContact = Contact(
                id = (sampleContacts.maxOfOrNull { it.id } ?: 0) + 1,
                name = name,
                phoneNumber = phoneNumber
            )
            sampleContacts.add(newContact)
        }
    )

    // Contact Detail Dialog
    if (selectedContact != null) {
        AlertDialog(
            onDismissRequest = { selectedContact = null },
            confirmButton = {
                TextButton(onClick = { selectedContact = null }) {
                    Text("Đóng")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    makeCall(context, selectedContact?.phoneNumber.toString())
                    selectedContact = null
                }) {
                    Text("Gọi")
                }
            },
            title = { Text("Chi tiết Liên Hệ") },
            text = {
                Column {
                    Text("Tên: ${selectedContact?.name}")
                    Text("Số điện thoại: ${selectedContact?.phoneNumber}")
                }
            }
        )
    }
}

fun makeCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}