package com.jetli.vina.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetli.vina.R
import com.jetli.vina.utils.TouchableOpacity
import com.jetli.vina.utils.onClickNextButton
import com.jetli.vina.viewmodel.PaymentViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPinScreen(
    navController : NavController,
    paymentViewModel: PaymentViewModel = viewModel()
){
    var arrNum by remember {  mutableStateOf(MutableList(4) {""})  }
    // get the viewModel state
    val qrCodeDetails by paymentViewModel.qrCodeDetails.collectAsStateWithLifecycle()
    val showModal by paymentViewModel.showModal.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current
    var passcodeReady by remember { mutableStateOf(false) }
    val focusRequester = remember { List(4) { FocusRequester() } }
    var showmodal by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isDarkMode = isSystemInDarkTheme()
    // Check if the pin entry is complete (i.e., if 4 digits have been entered)
    val isPinComplete = arrNum.size == 4 && arrNum.all { it.isNotEmpty() }
    LaunchedEffect(Unit) {
        focusRequester[0].requestFocus() // Automatically focus on the first field when screen is visited
    }



    // get permission

    // Display the modal if showModal is true
    if (showmodal) {
        CircularProgressModal(onDismiss = { showmodal = false })
    }

     fun updateNum(value: String, arrNum: MutableList<String>, updateArrNum: (List<String>) -> Unit) {
//         keyboardController?.hide()
        val index = arrNum.indexOfFirst { it.isEmpty() }
        if (index != -1) {
            val newArrValue = arrNum.toMutableList()
            if (value.isNotEmpty() && index < focusRequester.size - 1) {

                focusRequester[index + 1].requestFocus()
            }
            newArrValue[index] = value
            updateArrNum(newArrValue) // Reassign the list to trigger recomposition
        }
    }

    fun deleteLastNum( arrNum: MutableList<String>, updateArrNum: (List<String>) -> Unit){
//        keyboardController?.hide()
        val index = arrNum.indexOfLast { it.isNotEmpty() }
        if(index != -1){
            val newArrVal = arrNum.toMutableList()
            newArrVal[index] = ""
            if ( index >= 0) {
                keyboardController?.hide()

                focusRequester[if(index == 0) { 0 } else {
                    index + 1 - 1
                }].requestFocus()
                keyboardController?.hide()
            }
            updateArrNum(newArrVal)
        }
    }

   Column(
       modifier = Modifier
           .fillMaxSize()
           .blur(
               if (showmodal) {
                   8.dp
//                   edgeTreatment = BlurredEdgeTreatment.Unbounded
               } else {
                   0.dp
               }
           )
           .padding(16.dp)
           .onGloballyPositioned { // Ensure the layout is complete before hiding the keyboard
               keyboardController?.hide()
           },
       horizontalAlignment = Alignment.CenterHorizontally,
//       verticalArrangement = Arrangement.Top
   ) {
        Text("Enter Your Pin", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

       Text("Enter your 4 digit pin", fontSize = 15.sp, fontWeight = FontWeight.Normal,
           modifier = Modifier.padding(bottom = 10.dp)
       )

       Text("${qrCodeDetails.Amount} ${qrCodeDetails.Merchant_Name}", fontSize = 15.sp, fontWeight = FontWeight.Normal,
           modifier = Modifier.padding(bottom = 10.dp)
       )
        Spacer(modifier = Modifier.height(15.dp))
       Box(modifier = Modifier
           .padding(horizontal = 16.dp, vertical = 9.dp)
           .clip(RoundedCornerShape(8.dp))
           .onGloballyPositioned { keyboardController?.hide() }
       ){
       Row (
           horizontalArrangement = Arrangement.Center,
           modifier = Modifier
               .fillMaxWidth()
               .background(color = if (isDarkMode) Color(0xFF1A263E) else Color(0XFFFFFFFF))
               .onGloballyPositioned { keyboardController?.hide() }
           ){
            arrNum.forEachIndexed { index, num ->

                OutlinedTextField(
                    value = num,
                    onValueChange = {
//                            value -> if(value.length <= 1){
//                        val newArrNum = arrNum.toMutableList()
//                        newArrNum[index] = value
//                        arrNum = newArrNum
//// Log the change in value Color(0xFF0E1A32)
//                        // Move focus to the next field if typing a value
//                        if (value.isNotEmpty() && index < focusRequester.size - 1) {
//                            Log.d("MyApp", "updated focus at index  changed to $value")
//                            focusRequester[index + 1].requestFocus()
//                        }
//                        // Move focus to the previous field if deleting
//                        if (value.isEmpty() && index > 0) {
//                            Log.d("MyApp", "deleted focus at index  changed to $value")
//                            focusRequester[index - 1].requestFocus()
//                        }
//                    }
                    }, // No direct user input allowed
                    readOnly = true, // This prevents the system keyboard from showing up
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF96D0E),
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333E52) else Color(0xFFF7F7F7),
                        focusedContainerColor = if (isDarkMode) Color(0xFF0E1A32) else Color(0xFFFFFFFF),
                        cursorColor = Color(0xFFF96D0E),
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    ,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(), // Apply password visual transformation
                    modifier = Modifier
                        .padding(6.dp)
                        .width(50.dp)
                        .height(60.dp)
                        .border(1.dp, Color.Transparent)
                        .focusRequester(focusRequester[index])
                        .focusable(true)
                )

            }
       }
       }
       Spacer(modifier = Modifier.height(16.dp))

       Row(
//           horizontalArrangement = Arrangement.SpaceBetween,
           modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 2.dp)
       ) {
           val nums = listOf(1,2,3,4,5,6,7,8,9)

           LazyVerticalGrid(
               columns = GridCells.Fixed(3),
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp),
               horizontalArrangement = Arrangement.spacedBy(16.dp)

           ) {
                items(nums) { num ->
                    TouchableOpacity(onClick = {  updateNum(num.toString(), arrNum){ updatedList ->
                        arrNum = updatedList.toMutableList()
                    } }) {

                    Box(modifier = Modifier
                        .size(80.dp) // set the btn size 1a263e
                        .clip(CircleShape)
                        .background(color = if (isDarkMode) Color(0xFF1A263E) else Color(0XFFFFFFFF)),
                            contentAlignment = Alignment.Center

                        ) {

                        Text(text = num.toString(), fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(vertical = 7.dp)
                        )
                    }

                }
                }
           }




       }

       // next 3
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(start = 18.dp)
       ) {
           TouchableOpacity(onClick = { deleteLastNum(arrNum){ updateNum ->
               arrNum = updateNum.toMutableList()
           } }) {
               Box(modifier = Modifier
                   .size(80.dp) // set the btn size
                   .clip(CircleShape)
                   .background(color = if (isDarkMode) Color(0xFF1A263E) else Color(0XFFFFFFFF)),
                   contentAlignment = Alignment.Center

               ) {

                   Icon(painter = painterResource(id = R.drawable.delete_left_icon_white), contentDescription = "Delete button icon",
                       modifier = Modifier.size(30.dp))                   }
           }

           Spacer(modifier = Modifier.width(24.dp))

           TouchableOpacity(onClick = { updateNum("0", arrNum){ updatedList ->
               arrNum = updatedList.toMutableList()
           } }) {
               Box(modifier = Modifier
                   .size(80.dp) // set the btn size
                   .clip(CircleShape)
                   .background(color = if (isDarkMode) Color(0xFF1A263E) else Color(0XFFFFFFFF)),
                   contentAlignment = Alignment.Center

               ) {
                   Text(text = "0", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, )
               }
           }

           Spacer(modifier = Modifier.width(24.dp))
           TouchableOpacity(onClick = {
               // Retrieve the context


               // Define the phone number and message you want to
               val phoneNumber = "+2349030602139"
               val message = "This is a test message."
                onClickNextButton(context, phoneNumber, message)

//               showmodal = true
//               navController.navigate("processing")
           }) {
               Box(modifier = Modifier
                   .size(80.dp) // set the btn size
                   .clip(CircleShape)
                   .background(color = Color(0xFFF96D0E).copy(alpha = if (isPinComplete) 1f else 0.1f))
                   .clickable {  },
                   contentAlignment = Alignment.Center

               ) {
                   Icon(painter = painterResource(id = R.drawable.arrow_right_icon),
                       contentDescription = "next button icon",
                       tint = Color.White,
                       modifier = Modifier.size(30.dp))
               }
           }

       }

       TouchableOpacity(onClick = { /*TODO*/ }) {
       Box(
           modifier = Modifier.fillMaxSize(),
           contentAlignment = Alignment.BottomCenter

       ){
       Text("Forgot Pin?", fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFFF96D0E),
           modifier = Modifier.padding(6.dp))
       }
   }
   }
}




@Composable
fun CircularProgressModal(onDismiss: () -> Unit) {
    // A full-screen Box to contain the blur and centered circular progress indicator
    Box(
        modifier = Modifier
            .fillMaxSize() // Fill the entire screen
            .background(Color.Black.copy(alpha = 0.5f)) // Background overlay with semi-transparency
            .zIndex(1f), // Ensure it's rendered above other content
        contentAlignment = Alignment.Center // Center the circular progress indicator
    ) {
        // Background circle
        Box(
            modifier = Modifier
                .size(80.dp) // Size of the background circle
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    shape = CircleShape
                ) // Background circle with transparency
                .align(Alignment.Center),

        ){
            Canvas(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)) {
              drawCircle(
                  color = Color.Gray,
                  radius = 80f,
                  style = Stroke(19f),
                  center = Offset(x= 70f, y=70f)
              )
            }
            // circular indicator
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center), // Size of the progress indicator
                color = Color(0xFFF96D0E), // Progress indicator color
                strokeWidth = 5.dp // Thickness of the progress indicator
            )
        }

    }
}


// Function to handle click events and retrieve context

