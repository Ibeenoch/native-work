package com.jetli.vina.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jetli.vina.R
import com.jetli.vina.utils.TouchableOpacity

@Composable
fun ProcessingScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.wait_icon), contentDescription = "ProcessingIcon", modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Payment of ₦23,200 is processing for Sunday Adelola, You will recieve an SMS notification upon completion", fontSize = 12.sp)

        }
    }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(16.dp)

        ) {

            TouchableOpacity(onClick = { navController.navigate("home") },
        ) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(color = Color(0xFFF96D0E)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text("Continue To Dashboard", fontSize = 15.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))

            }
        }
    }
}
}