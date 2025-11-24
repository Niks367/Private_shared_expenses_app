package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.R

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5F4),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        // Decorative circles in background (matching Figma design)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            // Large circle - Ellipse 5 (bottom left, largest)
            Box(
                modifier = Modifier
                    .size(628.dp)
                    .offset(x = (-107).dp, y = (-28).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7E78).copy(alpha = 0.08f))
                    .zIndex(1f)
            )

            // Medium circle - Ellipse 4 (middle size)
            Box(
                modifier = Modifier
                    .size(484.dp)
                    .offset(x = (-35).dp, y = 45.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7E78).copy(alpha = 0.12f))
                    .zIndex(2f)
            )

            // Small circle - Ellipse 3 (top, smallest)
            Box(
                modifier = Modifier
                    .size(360.dp)
                    .offset(x = 27.dp, y = 106.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7E78).copy(alpha = 0.15f))
                    .zIndex(3f)
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Illustration section with decorative coins and donut
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Left coin icon (Dollar symbol)
                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .offset(x = (-80).dp, y = (-40).dp)
                        .align(Alignment.TopStart)
                ) {
                    // Outer ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF5BA89E).copy(alpha = 0.3f))
                    )
                    // Inner blue circle
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color(0xFF4A9FE8))
                    )
                    // Dollar text
                    Text(
                        text = "$",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Right donut icon (Pie chart style)
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .offset(x = 100.dp, y = 20.dp)
                        .align(Alignment.TopEnd)
                ) {
                    // Outer ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF5BA89E).copy(alpha = 0.3f))
                    )
                    // Inner gradient circle (simulating pie chart)
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B9D),
                                        Color(0xFF4A9FE8),
                                        Color(0xFFFF8C42),
                                        Color(0xFFC86DD7)
                                    )
                                )
                            )
                    )
                }

                // Center illustration - 3D character
                Box(
                    modifier = Modifier
                        .size(280.dp, 460.dp)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    // Shadow
                    Box(
                        modifier = Modifier
                            .size(300.dp, 40.dp)
                            .offset(y = 200.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                    
                    // 3D character image
                    Image(
                        painter = painterResource(id = R.drawable.onboarding),
                        contentDescription = "Onboarding character",
                        modifier = Modifier
                            .size(280.dp, 400.dp)
                            .offset(y = (-20).dp)
                    )
                }
            }

            // Title text
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF2E7E78),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Spend Smarter\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF2E7E78),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Save More")
                    }
                },
                textAlign = TextAlign.Center,
                lineHeight = 42.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Get Started Button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7E78)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login text
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already Have Account? ",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = "Log In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E7E78),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

