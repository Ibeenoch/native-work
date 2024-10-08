package com.jetli.vina.utils


data class transaction (
    val amount: String,
    val recipient: String,
    val date: String,
    val status: String,
    val type: String,
)


val transactionLists = listOf(
    transaction(
        amount= "₦3,500",
        recipient= "Adewale Olayemi",
        date= "25 Mar 2024, 07:34pm",
        status= "Successful",
        type= "Transfer",
    ),
    transaction(
        amount= "₦14,200",
        recipient= "Michael Okpara",
        date= "26 Mar 2024, 08:34am",
        status= "Successful",
        type= "Withdrawal",
    ),
    transaction(
        amount= "₦23,200",
        recipient= "Chima Obina",
        date= "27 Mar 2024, 10:54am",
        status= "Unsuccessful",
        type= "Investment"
    ),
    transaction(
        amount= "₦1000",
        recipient= "Chima Obina",
        date= "27 Mar 2024, 10:54am",
        status= "Successful",
        type= "Mobile Data"
    ),
    transaction(
        amount= "₦23,200",
        recipient= "Esther Adanna",
        date= "29 Mar 2024, 04:55pm",
        status= "Successful",
        type= "Deposit",
    ),
    transaction(
        amount= "₦11,890",
        recipient= "Adewale Tunji",
        date= "30 Mar 2024, 11:22am",
        status= "Pending",
        type= "Withdrawal",
    ),
    transaction(
        amount= "₦1,500",
        recipient= "Seun Kunle",
        date= "1 April 2024, 05:34pm",
        status= "Pending",
        type= "Betting",
    ),
    transaction(
        amount= "₦3,500",
        recipient= "Adewale Olayemi",
        date= "25 Mar 2024, 07:34pm",
        status= "Successful",
        type= "Transfer",
    ),
    transaction(
        amount= "₦14,200",
        recipient= "Michael Okpara",
        date= "26 Mar 2024, 08:34am",
        status= "Successful",
        type= "Withdrawal",
    ),
    transaction(
        amount= "₦23,200",
        recipient= "Chima Obina",
        date= "27 Mar 2024, 10:54am",
        status= "Unsuccessful",
        type= "Investment"
    ),
    transaction(
        amount= "₦1000",
        recipient= "Chima Obina",
        date= "27 Mar 2024, 10:54am",
        status= "Successful",
        type= "Mobile Data"
    ),
    transaction(
        amount= "₦23,200",
        recipient= "Esther Adanna",
        date= "29 Mar 2024, 04:55pm",
        status= "Successful",
        type= "Deposit",
    ),
    transaction(
        amount= "₦11,890",
        recipient= "Adewale Tunji",
        date= "30 Mar 2024, 11:22am",
        status= "Pending",
        type= "Withdrawal",
    ),
    transaction(
        amount= "₦1,500",
        recipient= "Seun Kunle",
        date= "1 April 2024, 05:34pm",
        status= "Pending",
        type= "Betting",
    )
)