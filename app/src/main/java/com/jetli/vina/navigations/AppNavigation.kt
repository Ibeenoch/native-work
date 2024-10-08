package com.jetli.vina.navigations

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box

//import androidx.navigation.NavHost

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jetli.vina.R
import com.jetli.vina.screens.ConfirmPinScreen
import com.jetli.vina.screens.Home
import com.jetli.vina.screens.Payment
import com.jetli.vina.screens.ProcessingScreen
import com.jetli.vina.screens.ScanScreen
import com.jetli.vina.screens.TransactionDetailsScreen
import com.jetli.vina.viewmodel.PaymentViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            when(currentRoute){
                "home" -> {
                    TopBar(
                        modifier = Modifier,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.waspspeed),
                                contentDescription = "Waspeed logo",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .width(120.dp)
                                    .size(80.dp)
                            )

                        },
                        actionIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "profilepics",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .width(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    )
                }
                "payment" -> {
                    TopBar(
                        modifier = Modifier,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(painter = painterResource(id = R.drawable.arrow_back_icon),
                                    contentDescription = "back icon",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(25.dp))
                            }
                        },
                        title = "Payment"
                    )
                }
                "scan" -> {
                    TopBar(
                        modifier = Modifier,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(painter = painterResource(id = R.drawable.arrow_back_icon),
                                    contentDescription = "back icon",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(70.dp))
                            }
                        },
                        title = "Scan"
                    )
                }
                "transactiondetails" -> {
                    TopBar(
                        modifier = Modifier,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(painter = painterResource(id = R.drawable.arrow_back_icon),
                                    contentDescription = "back icon",
                                    modifier = Modifier
                                        .size(25.dp))
                            }
                        },
                        title = "Transaction Details"
                    )
                }
                else -> {
                    // Default top bar
                    TopBar(
                        modifier = Modifier,
                        scrollBehavior = scrollBehavior,
                        title = ""
                    )
                }
            }

                 },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            when(currentRoute){
                "home" -> {
                    BottomBar(currentRoute, navController)
                }
                "payment" -> {
                    BottomBar(currentRoute, navController)
                }
            else -> {

            }

            }}
    ) { innerPadding ->

        val paymentViewModel: PaymentViewModel = viewModel()
        NavHost(
            navController= navController,
            startDestination="home",
            Modifier.padding(innerPadding),

        ){
            composable("home"){
                Home(navController)
            }
            composable("payment"){ Payment(navController) }
            composable("processing"){ ProcessingScreen(navController) }
            composable("transactiondetails"){ TransactionDetailsScreen(navController, paymentViewModel = paymentViewModel) }
            composable("confirmpin"){ ConfirmPinScreen(navController)}
            composable("scan"){ ScanScreen(navController, paymentViewModel = paymentViewModel) }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar( modifier: Modifier,
            scrollBehavior: TopAppBarScrollBehavior,
            title: String?= null,
            navigationIcon: (@Composable () -> Unit)?  = null,
            actionIcon: (@Composable () -> Unit)? = null
            ) {

    val isDarkMode = isSystemInDarkTheme()
    if (navigationIcon != null) {
        TopAppBar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor =  if (isDarkMode) Color(0xFF000E28) else Color(0xFFF7F7F7)
            ),
            title = {
                if( title != null){
                    Box(modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 30.dp),

                    )

                    }
                }
            },
            navigationIcon = navigationIcon,

            actions = {
                actionIcon?.invoke()

            }
        )
    }

}


@Composable
fun BottomBar(currentRoute : String, navController: NavController){


//    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isDarkMode = isSystemInDarkTheme()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp, start = 9.dp, end = 9.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(50.dp), clip = false)        ,

        ){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 9.dp, end = 9.dp)
            .height(85.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Transparent)
            ,
        containerColor =  if (isDarkMode) Color(0xFF0E1A32) else Color(0xFFF7F7F7)


    ) {
        NavigationBarItem(
            selected = currentRoute == "home" ,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent // Make the indicator transparent to effectively "remove" it
            ),
            onClick = {
                navController.navigate("home")
                      },
            label = {
                val homeTextColor = if (isDarkMode) {
                    if (currentRoute == "home") Color(0xFFF96D0E) else Color(0xFFFFFFFF)
                } else {
                    if (currentRoute == "home") Color(0xFFF96D0E) else Color(0xFF000000)
                }
                Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = homeTextColor ) },
            icon = {
                val homeTintColor = if (isDarkMode) {
                    if (currentRoute == "home") Color(0xFFF96D0E) else Color(0xFFFFFFFF)
                } else {
                    if (currentRoute == "home") Color(0xFFF96D0E) else Color(0xFF000000)
                }

                Icon(painter = painterResource(
                    id = R.drawable.house_orange_icon,
                ),
                    contentDescription = "Home Bottom",
                    modifier = Modifier.size(15.dp),
                        tint = homeTintColor
                )
                }
        )

        NavigationBarItem(
            selected = currentRoute == "payment" ,
            onClick = {
                navController.navigate("payment")
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent // Make the indicator transparent to effectively "remove" it
            ),
            label = {
                val paymentTextColor = if (isDarkMode) {
                    if (currentRoute == "payment") Color(0xFFF96D0E) else Color(0xFFFFFFFF)
                } else {
                    if (currentRoute == "payment") Color(0xFFF96D0E) else Color(0xFF000000)
                }
                Text(
                    "Payment",
                    color = paymentTextColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            icon = {
                val paymentTintColor = if (isDarkMode) {
                    if (currentRoute == "payment") Color(0xFFF96D0E) else Color(0xFFFFFFFF)
                } else {
                    if (currentRoute == "payment") Color(0xFFF96D0E) else Color(0xFF000000)
                }
                Icon(painter = painterResource(
                    id = R.drawable.send_2_icon,
                ),
                    contentDescription = "Payment Bottom Tab",
                    modifier = Modifier.size(20.dp),
                        tint = paymentTintColor
                )
            }
        )
    }
    }
}

