package com.example.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wallet.ui.theme.WalletTheme


val LOG_IN_TXT = "Login as "
val CREATE_TXT = "Create Account"


@Composable
fun LogInScreen(
    onGOTOMain: ()-> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        OutlinedButton(
            modifier = Modifier.padding(top = 50.dp),
            onClick =  { /*TODO*/ },
            //border = BorderStroke(1.dp, Color.LightGray),
        ){
            Text(
                text = CREATE_TXT
            )
        }
        OutlinedButton(
            modifier = Modifier.padding(top = 30.dp),
            onClick = {
            },
            //border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(
                text =LOG_IN_TXT,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }


}