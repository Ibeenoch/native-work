package com.jetli.vina.screens

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jetli.vina.utils.TouchableOpacity
import com.jetli.vina.viewmodel.PaymentViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    paymentViewModel: PaymentViewModel
){
    var textValue by remember { mutableStateOf("") }
    var currentTime = LocalDateTime.now()
    var formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
    var formatedtime = currentTime.format(formatTime)
    var formatter = DecimalFormat("#,###")
    var focusRequester = remember { FocusRequester() }

    val qrCodeDetails by paymentViewModel.qrCodeDetails.collectAsStateWithLifecycle()
    // Max and Min length constraints
    val maxLength = 12
    val minLength = 3




    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // Automatically adds padding when keyboard is visible
            .onGloballyPositioned {
                focusRequester.requestFocus()
            }
    ){
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier
//                .weight(0.1f) // This column will take up the available space
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = "₦$textValue",
                onValueChange = { newValue ->
                val cleanVal = newValue.replace(",", "").replace("₦", "")

                    if (cleanVal.length <= maxLength ) {
                        val formattedValue = cleanVal.toLongOrNull()?.let {
                            formatter.format(it)
                        } ?: ""
                        textValue = formattedValue
                    }
            },
                label = { Text("Enter An Amount", fontSize = 8.sp, color = Color(0xFFF96D0E)) },
//                 { Text(text = "₦") },
                modifier = Modifier
                    .width(180.dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFF96D0E),
                    unfocusedIndicatorColor = Color(0xFFF96D0E),
                    focusedLabelColor = Color(0xFFF96D0E),
                    unfocusedLabelColor = Color(0xFFF96D0E),
                    unfocusedTextColor = Color(0xFFF96D0E),
                    focusedTextColor = Color(0xFFF96D0E),
                    cursorColor =  Color(0xFFF96D0E),
                    unfocusedPlaceholderColor = Color(0xFFF96D0E),
                    focusedPlaceholderColor = Color(0xFFF96D0E),
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Minimum of ₦500", fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.Gray)
            Text("Time $formatedtime", fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.Gray)

        }
        Spacer(modifier = Modifier.height(18.dp))
        Column(

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .customDottedBorder(
                        1.dp,
                        Color(0xFFF96D0E),
                        10f
                    )  // Pass the desired thickness, color, and spacing for the dotted lines
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Merchant Name:", fontSize = 12.sp, color = Color.Gray)
                }

                Column {
                    Text(qrCodeDetails.Merchant_Name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Account Number:", fontSize = 12.sp, color = Color.Gray)

                }

                Column {
                    Text(qrCodeDetails.Account_Number, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .customDottedBorder(
                        1.dp,
                        Color(0xFFF96D0E),
                        10f
                    )  // Pass the desired thickness, color, and spacing for the dotted lines
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Store Name:", fontSize = 12.sp, color = Color.Gray)
                }

                Column {
                    Text(qrCodeDetails.Store_Name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),

            ) {
                TouchableOpacity(onClick = { navController.navigate("confirmpin") }) {

            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(color = Color(0xFFF96D0E)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
              Text("Proceed", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }

            }

                TouchableOpacity(onClick = { navController.popBackStack()}) {

            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(color = Color(0xFFF96D0E)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text("Cancel", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }

            }
        }

        }

    }
}







// Extension function to create a custom modifier for dotted borders
fun Modifier.customDottedBorder(borderWidth: androidx.compose.ui.unit.Dp, color: Color, dotSpacing: Float): Modifier = this
    .then(
        Modifier.drawWithContent {
            // Draw content inside the row first
            drawContent()

            // Calculate the width
            val width = size.width

            // Draw top dotted border
            drawDottedLine(
                color = color,
                borderWidth = borderWidth.toPx(),
                length = width,
                yOffset = 0f,  // Top of the Row
                dotSpacing = dotSpacing
            )

            // Draw bottom dotted border
            drawDottedLine(
                color = color,
                borderWidth = borderWidth.toPx(),
                length = width,
                yOffset = size.height - borderWidth.toPx(),  // Bottom of the Row
                dotSpacing = dotSpacing
            )
        }
    )

// Function to draw a dotted line
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDottedLine(
    color: Color,
    borderWidth: Float,
    length: Float,
    yOffset: Float,
    dotSpacing: Float
) {
    var xOffset = 0f
    while (xOffset < length) {
        // Draw a circle (dot)
        drawCircle(
            color = color,
            radius = borderWidth / 2,
            center = Offset(x = xOffset, y = yOffset + borderWidth / 2)
        )
        // Move to the next dot position
        xOffset += borderWidth + dotSpacing
    }
}