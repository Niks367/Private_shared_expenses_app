package com.example.myapplication.ui

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.room.withTransaction
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.WalletTransaction
import com.example.myapplication.model.BalanceDto
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.User
import com.example.myapplication.ui.viewmodels.HomeViewModel
import com.example.myapplication.ui.viewmodels.HomeViewModelFactory
import com.example.myapplication.ui.viewmodels.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    userName: String,
    userId: String,
    localExpenses: List<Expense> = emptyList(),
    walletTransactions: List<WalletTransaction> = emptyList(),
    walletBalance: Double = 0.0,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(ServiceLocator.userRepository)
    ),
    onTransactionClick: (WalletTransaction) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUserBalance(userId)
        viewModel.loadUserData(userId)
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showTransferDialog by remember { mutableStateOf(false) }
    val isNewUser = userId.toIntOrNull() != null
    val totalExpense = localExpenses.sumOf { it.amount }
    val balance = if (isNewUser) {
        BalanceDto(
            total = walletBalance - totalExpense,
            income = walletBalance,
            expense = totalExpense
        )
    } else {
        uiState.userBalance?.balance ?: BalanceDto(0.0, 0.0, 0.0)
    }
    val database = AppDatabase.getInstance(LocalContext.current)
    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            AddTransactionFab(
                onClick = {
                    showTransferDialog = true
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    BalanceCard(uiState = uiState, balance = balance, userName = userName)
                }
                Spacer(modifier = Modifier.height(60.dp))

                TransactionHistorySection(
                    walletTransactions = walletTransactions,
                    localExpenses = localExpenses,
                    uiState = uiState,
                    isNewUser = isNewUser,
                    onTransactionClick = onTransactionClick
                )
                Spacer(modifier = Modifier.height(24.dp))
                SendAgainSection()
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    )
    if (showTransferDialog) {
        TransferDialog(
            userId = userId,
            onDismiss = { showTransferDialog = false },
            onTransferSent = { recipient, amount ->
                coroutineScope.launch {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        val transaction = WalletTransaction(
                            0,
                            userId.toLong(),
                            "send",
                            "Send to $recipient",
                            amount,
                            dateFormat.format(java.util.Date())
                        )
                        database.walletTransactionDao().insert(transaction)
                    }

                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Money sent successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                coroutineScope.launch {
                    val dateFormat = java.text.SimpleDateFormat(
                        "yyyy-MM-dd",
                        java.util.Locale.getDefault()
                    )
                    val fetchedProfile = database.profileDao().findByFullName(recipient.trim())
                    if (fetchedProfile == null) {
                        Toast.makeText(
                            context,
                            "⚠️ No user found with that name. Please check the spelling.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }

                    val recipient = User(
                        userId = fetchedProfile.id.toString(),
                        username = "${fetchedProfile.firstName} ${fetchedProfile.lastName}",
                        email = fetchedProfile.email,
                        phone = fetchedProfile.phone
                    )
                    val userGroupIds = database.profileDao().getGroupIdsForProfile(userId.toLong())
                    val recipientGroupIds =
                        database.profileDao().getGroupIdsForProfile(fetchedProfile.id)
                    val senderGroupId = userGroupIds.firstOrNull() ?: run {
                        Toast.makeText(
                            context,
                            "⚠️ You aren’t a member of any group. Join or create one first.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }
                    val receiverGroupId = recipientGroupIds.firstOrNull() ?: run {
                        Toast.makeText(
                            context,
                            "⚠️ The recipient isn’t in any group yet.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }
                    val senderExpense = Expense(
                        groupId = senderGroupId,
                        description = "Sent to ${recipient.username}",
                        amount = amount,
                        paidBy = userId.toLong(),
                        date = dateFormat.format(java.util.Date())
                    )
                    val receiverExpense = WalletTransaction(
                        userId = recipient.userId.toLong(),
                        type = "add",
                        description = "Received money from $userName ",
                        amount = amount,
                        date = dateFormat.format(java.util.Date())
                    )
                    withContext(Dispatchers.IO) {
                        database.withTransaction {
                            database.expenseDao().insert(senderExpense)
                            database.walletTransactionDao().insert(receiverExpense)
                        }
                    }
                }
                showTransferDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferDialog(
    userId: String,
    onDismiss: () -> Unit,
    onTransferSent: (recipient: String, amount: Double) -> Unit
) {
    var recipient by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    val presetContacts = listOf("Alice", "Bob", "Charlie", "David")

    var expanded by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Send Money") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = recipient,
                        onValueChange = { recipient = it },
                        label = { Text("Recipient") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        presetContacts.forEach { contact ->
                            DropdownMenuItem(
                                text = { Text(contact) },
                                onClick = {
                                    recipient = contact
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        if (it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            amountText = it
                        }
                    },
                    label = { Text("Amount") },
                    placeholder = { Text("e.g. 12.34") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (recipient.isBlank() || amount == null || amount <= 0.0) {
                        return@TextButton
                    }
                    onTransferSent(recipient.trim(), amount)
                }
            ) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddTransactionFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(),
        modifier = modifier
            .size(64.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add transaction",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F6F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = transaction.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFF4A9B8E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = transaction.title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.date,
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Text(
            text = if (transaction.isIncome) {
                "+ $ %.2f".format(transaction.amount)
            } else {
                "- $ %.2f".format(transaction.amount)
            },
            fontSize = 18.sp,
            color = if (transaction.isIncome) Color(0xFF25A969) else Color(0xFFF95B51),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BalanceCard(uiState: UiState, balance: BalanceDto, userName: String) {
    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    Column(
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Curved background using Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                val width = size.width
                val height = size.height
                
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, height - 60f)
                    
                    // Curve di bagian bawah
                    quadraticBezierTo(
                        width / 2f, height + 20f,  // Control point (tengah, lebih rendah)
                        width, height - 60f         // End point (kanan)
                    )
                    
                    lineTo(width, 0f)
                    close()
                }
                
                drawPath(
                    path = path,
                    color = Color(0xFF2F7E79),
                    style = Fill
                )
            }
            
            // Decorative circles
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(212.dp)
                        .offset(x = (-55).dp, y = (-15).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                )
                Box(
                    modifier = Modifier
                        .size(127.dp)
                        .offset(x = 59.dp, y = (-15).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF438883).copy(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .size(85.dp)
                        .offset(x = 127.dp, y = (-22).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5BA89E).copy(alpha = 0.2f))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 46.dp, bottom = 80.dp, start = 24.dp, end = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Good ${
                                when (currentHour) {
                                    in 0..11 -> "morning"
                                    in 12..16 -> "afternoon"
                                    else -> "evening"
                                }
                            },",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$userName! 👋",
                            fontSize = 28.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Text(
                            text = "🔔",
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp)
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2F7E79)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Balance",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.stats),
                                contentDescription = "More options",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { /* Handle menu click */ }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$ %.2f".format(balance.total),
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.income),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Income",
                                        fontSize = 16.sp,
                                        color = Color(0xFFD0E5E4),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$ %.2f".format(balance.income),
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "Expenses",
                                        fontSize = 16.sp,
                                        color = Color(0xFFD0E5E4),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$ %.2f".format(balance.expense),
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.expense),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionHistorySection(
    walletTransactions: List<WalletTransaction>,
    localExpenses: List<Expense>,
    uiState: UiState,
    isNewUser: Boolean,
    onTransactionClick: (WalletTransaction) -> Unit = {}
) {
    val localTransactions = remember(localExpenses, walletTransactions) {
        val expenseList = localExpenses.map { expense ->
            val readableDate = expense.date.toLongOrNull()?.let { timestamp ->
                val dateFormat =
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                dateFormat.format(java.util.Date(timestamp))
            } ?: expense.date

            Transaction(
                iconRes = R.drawable.expense,
                title = expense.description,
                date = readableDate,
                amount = expense.amount,
                isIncome = false,
                expense = expense
            )
        }

        val walletList = walletTransactions.map { wallet ->
            val readableDate = wallet.date.toLongOrNull()?.let { timestamp ->
                val dateFormat =
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                dateFormat.format(java.util.Date(timestamp))
            } ?: wallet.date

            val isIncome = wallet.type in listOf("add", "income")

            Transaction(
                iconRes = if (isIncome) R.drawable.income else if (wallet.type == "send") R.drawable.transfer else R.drawable.expense,
                title = wallet.description,
                date = readableDate,
                amount = wallet.amount,
                isIncome = isIncome,
                walletTransaction = wallet
            )
        }
        (expenseList + walletList).sortedByDescending { it.date }
    }
    val displayedTransactions =
        if (isNewUser) localTransactions else (uiState.userBalance?.transactions ?: emptyList())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-40).dp)
            .padding(horizontal = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions History",
                fontSize = 18.sp,
                color = Color(0xFF222222),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        displayedTransactions.take(4).forEach { transaction ->
            TransactionItem(
                transaction = transaction,
                onClick = {
                    // Handle wallet transaction click
                    transaction.walletTransaction?.let { onTransactionClick(it) }
                    
                    // Handle expense click - convert to WalletTransaction for navigation
                    transaction.expense?.let { expense ->
                        val tempWalletTransaction = WalletTransaction(
                            id = expense.id,
                            userId = expense.paidBy,
                            type = "expense",
                            description = expense.description,
                            amount = expense.amount,
                            date = expense.date
                        )
                        onTransactionClick(tempWalletTransaction)
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (displayedTransactions.isEmpty()) {
            Text(
                text = "No transactions yet.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SendAgainSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-20).dp)
            .padding(horizontal = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Send Again",
                fontSize = 18.sp,
                color = Color(0xFF222222),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            listOf(
                R.drawable.user1,
                R.drawable.user2,
                R.drawable.user3,
                R.drawable.user4,
                R.drawable.user5
            ).forEach { userImage ->
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F6F5))
                        .clickable { /* Handle user click */ }
                ) {
                    Image(
                        painter = painterResource(id = userImage),
                        contentDescription = "User",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun GreetingBar(userName: String, onProfileClick: () -> Unit = {}) {

    val hour = remember {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    val greeting = when (hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Good night"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$greeting, $userName!👋",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationIcon()
            val initials = userName.take(2).uppercase()
            ProfileAvatar(
                initials = initials,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
fun ProfileAvatar(initials: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun NotificationIcon(
    notificationCount: Int = 0,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Notifications",
            tint = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        if (notificationCount > 0) {
            Box(
                modifier = Modifier
                    .offset(x = 8.dp, y = (-4).dp)
                    .size(16.dp)
                    .background(Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notificationCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun UserAvatarItem(icon: Int) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = "User",
        modifier = Modifier
            .clip(CircleShape)
            .size(62.dp)
    )
}
