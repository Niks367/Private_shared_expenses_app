package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.DebtInfo
import com.example.myapplication.model.GroupBalanceSummary
import com.example.myapplication.model.Settlement
import com.example.myapplication.ui.theme.PrimaryTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupBalanceScreen(
    groupBalanceSummary: GroupBalanceSummary,
    currentUserId: Long,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Group Balance",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PrimaryTeal,
                titleContentColor = Color.White
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Group Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        groupBalanceSummary.groupName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total Expenses",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        formatCurrency(groupBalanceSummary.totalExpenses),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryTeal
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Settlements Section
            if (groupBalanceSummary.settlements.isNotEmpty()) {
                Text(
                    "Suggested Settlements",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Highlight current user's settlements
                val userSettlements = groupBalanceSummary.settlements.filter {
                    it.fromProfileId == currentUserId || it.toProfileId == currentUserId
                }
                
                val otherSettlements = groupBalanceSummary.settlements.filter {
                    it.fromProfileId != currentUserId && it.toProfileId != currentUserId
                }
                
                // Show user's settlements first
                if (userSettlements.isNotEmpty()) {
                    Text(
                        "Your Settlements",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4A9B8E),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    userSettlements.forEach { settlement ->
                        SettlementCard(
                            settlement = settlement,
                            isCurrentUserInvolved = true,
                            currentUserId = currentUserId
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    if (otherSettlements.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Other Settlements",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                
                // Show other settlements
                otherSettlements.forEach { settlement ->
                    SettlementCard(
                        settlement = settlement,
                        isCurrentUserInvolved = false,
                        currentUserId = currentUserId
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "✓",
                            fontSize = 48.sp,
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "All Settled!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        Text(
                            "Everyone has paid their fair share",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Member Balances Section
            Text(
                "Member Balances",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            groupBalanceSummary.memberDebts.forEach { debtInfo ->
                MemberBalanceCard(
                    debtInfo = debtInfo,
                    isCurrentUser = debtInfo.profileId == currentUserId
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SettlementCard(
    settlement: Settlement,
    isCurrentUserInvolved: Boolean,
    currentUserId: Long
) {
    val isUserPaying = settlement.fromProfileId == currentUserId
    val backgroundColor = if (isCurrentUserInvolved) {
        if (isUserPaying) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
    } else {
        Color.White
    }
    
    val borderColor = if (isCurrentUserInvolved) {
        if (isUserPaying) Color(0xFFE57373) else Color(0xFF81C784)
    } else {
        Color.Transparent
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isCurrentUserInvolved) 4.dp else 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrentUserInvolved) 2.dp else 0.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // From Person
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    settlement.fromProfileName,
                    fontSize = 16.sp,
                    fontWeight = if (settlement.fromProfileId == currentUserId) 
                        FontWeight.Bold else FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
                Text(
                    "pays",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            // Arrow and Amount
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = if (isCurrentUserInvolved) {
                        if (isUserPaying) Color(0xFFF44336) else Color(0xFF4CAF50)
                    } else {
                        PrimaryTeal
                    },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatCurrency(settlement.amount),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentUserInvolved) {
                        if (isUserPaying) Color(0xFFF44336) else Color(0xFF4CAF50)
                    } else {
                        PrimaryTeal
                    }
                )
            }
            
            // To Person
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    settlement.toProfileName,
                    fontSize = 16.sp,
                    fontWeight = if (settlement.toProfileId == currentUserId) 
                        FontWeight.Bold else FontWeight.Medium,
                    color = Color(0xFF2D3748),
                    textAlign = TextAlign.End
                )
                Text(
                    "receives",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MemberBalanceCard(
    debtInfo: DebtInfo,
    isCurrentUser: Boolean
) {
    val backgroundColor = if (isCurrentUser) Color(0xFFF0F6F5) else Color.White
    val statusColor = when {
        debtInfo.balance > 0.01 -> Color(0xFF4CAF50) // Owed money (green)
        debtInfo.balance < -0.01 -> Color(0xFFF44336) // Owes money (red)
        else -> Color.Gray // Settled
    }
    
    val statusText = when {
        debtInfo.balance > 0.01 -> "gets back"
        debtInfo.balance < -0.01 -> "owes"
        else -> "settled"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isCurrentUser) 3.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryTeal.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        debtInfo.profileName.first().uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryTeal
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Name and details
                Column {
                    Text(
                        debtInfo.profileName + if (isCurrentUser) " (You)" else "",
                        fontSize = 16.sp,
                        fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                        color = Color(0xFF2D3748)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Paid: ${formatCurrency(debtInfo.totalPaid)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text("•", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            "Share: ${formatCurrency(debtInfo.totalShare)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            // Balance
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    statusText,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    formatCurrency(kotlin.math.abs(debtInfo.balance)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }
    }
}

