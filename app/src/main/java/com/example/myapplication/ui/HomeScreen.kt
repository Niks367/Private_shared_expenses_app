package com.example.myapplication.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(ServiceLocator.userRepository)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUserBalance(userId)
        viewModel.loadUserData(userId)
    }
    Scaffold(
        topBar = {
            GreetingBar(userName = userName)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            val user = uiState.userBalance
            BalanceCard(uiState = uiState)
            
            // Show transactions if available, otherwise show empty state
            TransactionHistorySection(transactions = user?.transactions ?: emptyList())
            
            SendAgainSection()
        }
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
fun GreetingBar(userName: String) {

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
        NotificationIcon()
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
private fun NotificationIcon(
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
