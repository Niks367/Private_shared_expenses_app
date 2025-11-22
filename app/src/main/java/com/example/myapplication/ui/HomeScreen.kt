package com.example.myapplication.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.TextStyle
import com.example.myapplication.R
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.model.Transaction
import com.example.myapplication.ui.viewmodels.HomeUiState
import com.example.myapplication.ui.viewmodels.HomeViewModel
import com.example.myapplication.ui.viewmodels.HomeViewModelFactory
import com.example.myapplication.ui.viewmodels.UiState
import java.util.Calendar
import kotlin.math.abs

@Composable
fun HomeScreen(
    userName: String,
    userId: String,
    onProfileClick: () -> Unit = {},
    localExpenses: List<com.example.myapplication.entities.Expense> = emptyList(),
    walletBalance: Double = 0.0,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(ServiceLocator.userRepository)
    )
) {
    // Get current time for greeting
    val currentHour = remember { java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUserBalance(userId)
        viewModel.loadUserData(userId)
    }
    
    // For new users (numeric ID), use local expenses instead of API
    val isNewUser = userId.toIntOrNull() != null
    val localTransactions = remember(localExpenses) {
        localExpenses.map { expense ->
            // Convert timestamp to readable date if needed
            val readableDate = expense.date.toLongOrNull()?.let { timestamp ->
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                dateFormat.format(java.util.Date(timestamp))
            } ?: expense.date
            
            com.example.myapplication.model.Transaction(
                iconRes = R.drawable.expense,
                title = expense.description,
                date = readableDate,
                amount = expense.amount,
                isIncome = false
            )
        }
    }
    
    val displayedTransactions = if (isNewUser) localTransactions else (uiState.userBalance?.transactions ?: emptyList())
    
    // Calculate balance from local expenses and wallet for new users
    val totalExpense = localExpenses.sumOf { it.amount }
    val balance = if (isNewUser) {
        com.example.myapplication.model.BalanceDto(
            total = walletBalance - totalExpense,
            income = walletBalance,
            expense = totalExpense
        )
    } else {
        uiState.userBalance?.balance ?: com.example.myapplication.model.BalanceDto(0.0, 0.0, 0.0)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with gradient background and decorative circles
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2F7E79))
            ) {
                // Decorative circles in background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    // Large circle - bottom left
                    Box(
                        modifier = Modifier
                            .size(212.dp)
                            .offset(x = (-55).dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                    )
                    
                    // Medium circle - top center-left
                    Box(
                        modifier = Modifier
                            .size(127.dp)
                            .offset(x = 59.dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF438883).copy(alpha = 0.25f))
                    )
                    
                    // Small circle - top center-right
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
                        .padding(top = 56.dp, bottom = 80.dp, start = 24.dp, end = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Good ${when (currentHour) {
                                    in 0..11 -> "morning"
                                    in 12..16 -> "afternoon"
                                    else -> "evening"
                                }},",
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userName,
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        // Notification icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .clickable { /* Handle notification click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notification),
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
            
            // Balance Card (overlapping header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-60).dp)
                    .padding(horizontal = 20.dp)
            ) {
                // Main balance card with shadow
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
                            
                            // Three dots menu
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
                            // Income
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
                            
                            // Expenses
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
            
            // Transactions History
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
                
                // Transaction items
                displayedTransactions.take(4).forEach { transaction ->
                    TransactionItem(transaction = transaction)
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
            
            // Send Again Section
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
                
                // User avatars row
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
    }
}

@Composable
fun TransactionItem(transaction: com.example.myapplication.model.Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle transaction click */ },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background
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
            
            // Title and date
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
        
        // Amount
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
fun LoadingBalancePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray, RoundedCornerShape(20.dp))
    )
}

@Composable
fun ErrorBalanceView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Unable to load balance", color = Color.Red)
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun BalanceCard(uiState: UiState) {
    if (uiState.isLoading) {
        LoadingBalancePlaceholder()
        return
    }

    uiState.error?.let { errMsg ->
        ErrorBalanceView(message = errMsg) {
            // Retry functionality could be added here
        }
        return
    }

    // Show default balance card for new users or when data is not available
    val balance = uiState.userBalance

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2E7E78))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Balance",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                NotificationIcon()
            }

            Text(
                text = "$${balance?.balance?.total ?: "0.00"}",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BalanceInfoItem(
                    icon = R.drawable.income,
                    label = "Income",
                    amount = "$${balance?.balance?.income ?: "0.00"}"
                )
                BalanceInfoItem(
                    icon = R.drawable.expense,
                    label = "Expenses",
                    amount = "$${balance?.balance?.expense ?: "0.00"}"
                )
            }
        }
    }
}

@Composable
fun BalanceInfoItem(icon: Int, label: String, amount: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color.White
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp
        )
        Text(
            text = amount,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TransactionHistorySection(transactions: List<Transaction>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { /* navigate */ }
            )
        }

        if (transactions.isEmpty()) {
            Text(
                text = "No transactions yet.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )
        } else {
            transactions.forEach { tx ->
                TransactionItem(
                    icon = tx.iconRes,
                    title = tx.title,
                    date = tx.date,
                    amount = "${if (tx.isIncome) "+" else "-"}$${abs(tx.amount)}",
                    isIncome = tx.isIncome
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun TransactionItem(icon: Int, title: String, date: String, amount: String, isIncome: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F6F5))
                    .padding(10.dp)
            )
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) Color(0xFF24A869) else Color(0xFFF95B51)
        )
    }
}

@Composable
fun SendAgainSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Send Again",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UserAvatarItem(R.drawable.user1)
            UserAvatarItem(R.drawable.user2)
            UserAvatarItem(R.drawable.user3)
            UserAvatarItem(R.drawable.user4)
            UserAvatarItem(R.drawable.user5)
        }
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
            text = "$greeting, $userName 👋",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
        NotificationIcon()
            
            // Profile Avatar - clickable to navigate to profile
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
