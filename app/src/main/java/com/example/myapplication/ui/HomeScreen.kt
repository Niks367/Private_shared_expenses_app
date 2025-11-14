package com.example.myapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.myapplication.model.User
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun HomeScreen(user: User?, onProfileClick: () -> Unit) {
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val initials = user.username
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
        .take(2)

    // --- NEW: Root Box to allow floating the profile button ---
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = Color(0xFFFFFFFF),
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = Color(0xFFFFFFFF),
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val request1 = ImageRequest.Builder(LocalContext.current)
                            .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d1f81298-0c34-4a91-b4b1-209964ed53b3")
                            .crossfade(true)
                            .build()
                        CoilImage(
                            imageModel = { request1 },
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier
                                .padding(bottom = 70.dp)
                                .height(287.dp)
                                .fillMaxWidth()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp) // Centered with margins
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(
                                color = Color(0xFF2E7E78),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(20.dp) // Inner padding for content
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Total Balance",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                )
                                val request2 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5ba99855-dbf3-4a62-9166-8dfd98cb3dfa")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request2 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height(18.dp)
                                )
                            }
                            // --- REMOVED: Profile button was here ---
                        }
                        Text(
                            "$ 2,548.00",
                            color = Color(0xFFFFFFFF),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { println("Pressed!") },
                                border = BorderStroke(0.dp, Color.Transparent),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(),
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(40.dp))
                                    .background(
                                        color = Color(0x26FFFFFF),
                                        shape = RoundedCornerShape(40.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(3.dp)
                                ) {
                                    val request4 = ImageRequest.Builder(LocalContext.current)
                                        .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/9f1d947c-1baf-4543-ba97-91a181e90de2")
                                        .crossfade(true)
                                        .build()
                                    CoilImage(
                                        imageModel = { request4 },
                                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(40.dp))
                                            .width(18.dp)
                                            .height(18.dp)
                                    )
                                }
                            }
                            OutlinedButton(
                                onClick = { println("Pressed!") },
                                border = BorderStroke(0.dp, Color.Transparent),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(),
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(40.dp))
                                    .background(
                                        color = Color(0x26FFFFFF),
                                        shape = RoundedCornerShape(40.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(3.dp)
                                ) {
                                    val request5 = ImageRequest.Builder(LocalContext.current)
                                        .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/f0413aec-eed5-4222-9485-5d09b507c287")
                                        .crossfade(true)
                                        .build()
                                    CoilImage(
                                        imageModel = { request5 },
                                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(40.dp))
                                            .width(18.dp)
                                            .height(18.dp)
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 29.dp) // Make this card slightly narrower
                            .padding(bottom = 9.dp) // Position it from the bottom
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(
                                color = Color(0xFF1A5C57),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp) // Inner padding
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Income",
                                color = Color(0xFFD0EBE9),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "Expenses",
                                color = Color(0xFFD0E5E3),
                                fontSize = 18.sp,
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(top = 4.dp).fillMaxWidth()
                        ) {
                            Text(
                                "$ 1,840.00",
                                color = Color(0xFFFFFFFF),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "$ 284.00",
                                color = Color(0xFFFFFFFF),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 19.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Transactions history",
                        color = Color(0xFF222222),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "See all",
                        color = Color(0xFF666666),
                        fontSize = 14.sp,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(
                            onClick = { println("Pressed!") },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(end = 9.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Color(0xFFF0F6F5),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 8.dp)
                            ) {
                                val request6 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/985dbd33-90ec-4c94-ab3d-3bf9de287b8b")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request6 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .width(34.dp)
                                        .height(30.dp)
                                )
                            }
                        }
                        Column(
                        ) {
                            Text(
                                "Upwork",
                                color = Color(0xFF000000),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                            )
                            Text(
                                "Today",
                                color = Color(0xFF666666),
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .padding(end = 20.dp)
                            )
                        }
                    }
                    Text(
                        "+ $ 850.00",
                        color = Color(0xFF24A869),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(
                            onClick = { println("Pressed!") },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(end = 9.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Color(0xFFF0F6F5),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                            ) {
                                val request7 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/14bc6b50-b361-4070-8a88-c963acbd9a04")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request7 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .width(30.dp)
                                        .height(30.dp)
                                )
                            }
                        }
                        Column(
                        ) {
                            Text(
                                "Transfer",
                                color = Color(0xFF000000),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                            )
                            Text(
                                "Yesterday",
                                color = Color(0xFF666666),
                                fontSize = 13.sp,
                            )
                        }
                    }
                    Text(
                        "- $ 85.00",
                        color = Color(0xFFF95B51),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(
                            onClick = { println("Pressed!") },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(end = 9.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Color(0xFFF0F6F5),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                            ) {
                                val request8 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/45f73769-d424-4384-b037-024705a4f811")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request8 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .width(26.dp)
                                        .height(31.dp)
                                )
                            }
                        }
                        Column(
                        ) {
                            Text(
                                "Paypal",
                                color = Color(0xFF000000),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(bottom = 6.dp, end = 32.dp)
                            )
                            Text(
                                "Jan 30, 2022",
                                color = Color(0xFF666666),
                                fontSize = 13.sp,
                            )
                        }
                    }
                    Text(
                        "+ $ 1,406.00",
                        color = Color(0xFF24A869),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 30.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(
                            onClick = { println("Pressed!") },
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            modifier = Modifier
                                .padding(end = 9.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Color(0xFFF0F6F5),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 7.dp)
                            ) {
                                val request9 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/67addca7-1d4d-4a01-95d5-7e21252d89e0")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request9 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .width(35.dp)
                                        .height(35.dp)
                                )
                            }
                        }
                        Column(
                        ) {
                            Text(
                                "Youtube",
                                color = Color(0xFF000000),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(bottom = 6.dp, end = 16.dp)
                            )
                            Text(
                                "Jan 16, 2022",
                                color = Color(0xFF666666),
                                fontSize = 13.sp,
                            )
                        }
                    }
                    Text(
                        "- $ 11.99",
                        color = Color(0xFFF95B51),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 15.dp, start = 22.dp, end = 22.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Send Again",
                        color = Color(0xFF222222),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "See all",
                        color = Color(0xFF666666),
                        fontSize = 14.sp,
                    )
                }
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val request10 = ImageRequest.Builder(LocalContext.current)
                                .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5e179dfd-69fc-40ad-a0ba-033ba519cfde")
                                .crossfade(true)
                                .build()
                            CoilImage(
                                imageModel = { request10 },
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(start = 22.dp)
                                    .width(62.dp)
                                    .height(62.dp)
                            )

                            CoilImage(
                                imageModel = { request10 },
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(start = 99.dp)
                                    .width(62.dp)
                                    .height(62.dp)
                            )
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                val request11 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a729e65c-eef3-46ad-9310-544cb8dd59dd")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request11 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .padding(end = 96.dp)
                                        .width(62.dp)
                                        .height(62.dp)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .padding(bottom = 9.dp)
                                    .fillMaxWidth()
                            ) {
                                val request12 = ImageRequest.Builder(LocalContext.current)
                                    .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/c29c8028-a78e-4a2a-9d53-1effd0240191")
                                    .crossfade(true)
                                    .build()
                                CoilImage(
                                    imageModel = { request12 },
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .padding(end = 18.dp)
                                        .width(62.dp)
                                        .height(62.dp)
                                )
                            }

                            CoilImage(
                                imageModel = { request10 },
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .height(80.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    val request13 = ImageRequest.Builder(LocalContext.current)
                        .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/e8768201-c066-450f-b52f-a3a18cf3fd68")
                        .crossfade(true)
                        .build()
                    CoilImage(
                        imageModel = { request13 },
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .offset(x = 169.dp, y = 30.dp)
                            .align(Alignment.TopStart)
                            .padding(start = 169.dp, bottom = 30.dp)
                            .height(78.dp)
                            .fillMaxWidth()
                    )
                    CoilImage(
                        imageModel = { request13 },
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .offset(x = 177.dp, y = 0.dp)
                            .align(Alignment.TopStart)
                            .padding(start = 177.dp)
                            .height(62.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // --- NEW: Floating profile button in the top right corner ---
        OutlinedButton(
            onClick = onProfileClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .size(48.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = initials,
                    color = Color(0xFF1A5C57),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
