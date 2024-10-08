package com.jetli.vina.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jetli.vina.R

@Composable
fun Payment(navController: NavController){
    val isDarkMode = isSystemInDarkTheme()
    Spacer(modifier = Modifier.height(20.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 12.dp, end = 12.dp)
        .clickable {
            navController.navigate("scan")
        }

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = if (isDarkMode) Color(0xFF1A263E) else Color(0xFFFFFFFF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color(0xFFF96D0E)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.scan_svgrepo_com),
                        contentDescription = "Scan",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(6.dp),
                        tint =  Color.White
                    )

                }
                Text("Scan To Pay", fontSize = 12.sp, fontWeight = FontWeight.SemiBold )
            }

            Box(modifier = Modifier.padding(4.dp),
            ){
                Icon(painter = painterResource(id = R.drawable.arrow_forward_simple_svgrepo_com__1_), contentDescription = "Next Icon",
                    tint = Color(0xFFF96D0E),
                    modifier = Modifier.size(18.dp)
                )
            }

        }
    }
}