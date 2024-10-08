package com.jetli.vina.screens


import androidx.compose.foundation.background
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import com.jetli.vina.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jetli.vina.utils.transactionLists

@Composable
fun Home( navController: NavController){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp))
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                , horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Hello,", fontSize = 15.sp, fontWeight = FontWeight.Bold, )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Adebisi", fontSize = 15.sp, fontWeight = FontWeight.Bold, )
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = Color(0xFFF96D0E))
                        .padding(1.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("092139101", fontSize = 9.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(painter = painterResource( id = R.drawable.copy_icon ), contentDescription = "Copy icon", tint = Color(0xFFFFFFFF), modifier = Modifier.size(15.dp) )
                    }
                }
            }

            Spacer(modifier = Modifier.height(9.dp))
            Text("Tier 3 Verification in progress", fontSize = 12.sp, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic)
            Spacer(modifier = Modifier.height(9.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(9.dp))
                    .background(color = Color(0xFFF96D0E))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var showBalance by remember { mutableStateOf(false) }

                fun toggleShowBalance(){
                    showBalance = !showBalance
                }

                if(showBalance){
                    Row(
                        modifier = Modifier.clickable { toggleShowBalance() }
                    ){
                        Text("₦562,890.00")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painter = painterResource(id = R.drawable.eye_icon),
                            contentDescription = "view balance icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }else{

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { toggleShowBalance() }
                    ) {
                        repeat(3){
                            Box (modifier = Modifier
                                .size(10.dp)
                                .background(color = Color.Black, shape = CircleShape)
                                .padding(4.dp))
                            Spacer(modifier = Modifier.width(4.dp))

                        }
                        Icon(painter = painterResource(id = R.drawable.eye_off_icon), contentDescription = "View Account Balance", modifier = Modifier
                            .size(20.dp))

                    }

                }
                Row {
                    Icon(painter = painterResource(id = R.drawable.qrcode_icon), contentDescription = "barcode",
                        modifier= Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transaction History", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Icon(painter = painterResource(id = R.drawable.filter_alt_icon), contentDescription = "filter icon", modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            val isDarkMode = isSystemInDarkTheme()
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                transactionLists.forEachIndexed { index, transaction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = if (isDarkMode) Color(0xFF000E28) else Color(0xFFFFFFFF))
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx() // Convert dp to pixels
                                val y =
                                    size.height - strokeWidth / 2 // Position the line at the bottom
                                drawLine(
                                    color = Color.Gray.copy(alpha = 0.2f),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .size(30.dp)
                                .background(color = Color(0xFFF96D0E)),
                                contentAlignment = Alignment.Center
                            ){
                                when(transaction.type){
                                    "Withdrawal" -> WithdrawalIcon(modifier = Modifier.size(18.dp), )
                                    "Transfer" -> TransferIcon(modifier = Modifier.size(18.dp))
                                    "Deposit" -> DepositIcon(modifier = Modifier.size(18.dp))
                                    "Betting" -> BetCasinoIcon(modifier = Modifier.size(18.dp))
                                    "Investment" -> InvestmentIcon(modifier = Modifier.size(18.dp))
                                    "Mobile Data" -> MobileDataIcon(modifier = Modifier.size(18.dp))

                                }
                            }
                            Column {
                                Text(
                                    text = transaction.type,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = transaction.date,
                                    fontSize = 8.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = transaction.amount,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text =  when(transaction.status) {
                                    "Successful" -> "Transaction Successful"
                                    "Pending" -> "Transaction Pending"
                                    else -> "Transaction Failed"
                                },
                                fontSize = 8.sp,
                                textAlign = TextAlign.Left,
                                color = when(transaction.status) {
                                    "Successful" -> Color.Green
                                    "Pending" -> Color.Yellow
                                    else -> Color.Red
                                },

                                )
                        }
                    }
                }
            }
        }


    }
}




@Composable
fun WithdrawalIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.withdraw_dollar_sign_svgrepo_com),
        contentDescription = "Withdrawal Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}

@Composable
fun TransferIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.bank_transfer_icon),
        contentDescription = "Transfer Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}

@Composable
fun DepositIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.bank_svgrepo_com__1_),
        contentDescription = "Deposit Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}

@Composable
fun BetCasinoIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.bet_casino_chip_svgrepo_com__2_),
        contentDescription = "Betting Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}

@Composable
fun InvestmentIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.bank_finance_cash_dollar_purchase_money_transfer_buy),
        contentDescription = "Investment Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}

@Composable
fun MobileDataIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.mobile_svgrepo_com),
        contentDescription = "Mobile Data Icon",
        tint = Color(0xFFFFFFFF) ,
        modifier = modifier
    )
}


