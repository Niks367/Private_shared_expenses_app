package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun BillingAccountScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Back",
                    color = PrimaryTeal,
                    fontSize = 16.sp
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryTeal,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Billing Account",
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Card Number field
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = {
                    Text(
                        "Card Number",
                        color = GrayText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = LightTeal,
                    focusedLabelColor = PrimaryTeal,
                    unfocusedLabelColor = GrayText
                ),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text("1234 5678 9012 3456")
                }
            )

            // Card Holder field
            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = {
                    Text(
                        "Card Holder Name",
                        color = GrayText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = LightTeal,
                    focusedLabelColor = PrimaryTeal,
                    unfocusedLabelColor = GrayText
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Expiry Date and CVV in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Expiry Date
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = {
                        Text(
                            "Expiry Date",
                            color = GrayText
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryTeal,
                        unfocusedBorderColor = LightTeal,
                        focusedLabelColor = PrimaryTeal,
                        unfocusedLabelColor = GrayText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text("MM/YY")
                    }
                )

                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = {
                        Text(
                            "CVV",
                            color = GrayText
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryTeal,
                        unfocusedBorderColor = LightTeal,
                        focusedLabelColor = PrimaryTeal,
                        unfocusedLabelColor = GrayText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text("123")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            Button(
                onClick = {
                    onSaveClick(cardNumber, cardHolder, expiryDate, cvv)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Payment Method",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillingAccountScreenPreview() {
    MaterialTheme {
        BillingAccountScreen(
            onBackClick = {},
            onSaveClick = { _, _, _, _ -> }
        )
    }
}

