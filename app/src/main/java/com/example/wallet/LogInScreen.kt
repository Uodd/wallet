package com.example.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.ui.theme.WalletTheme


val LOG_IN_TXT = "Login as "
val CREATE_TXT = "Create Account"


@Composable
fun LogInScreen(
    onGOTOMain: ()-> Unit
){
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        when(status){
            "create" -> {
                CreateUserCard()
            }
            "login" -> {
                LoginCard()
            }
            else -> {
            }
        }
        OutlinedButton(
            modifier = Modifier.padding(top = 50.dp),
            onClick =  {
                status= "create"
            },
            //border = BorderStroke(1.dp, Color.LightGray),
        ){
            Text(
                text = CREATE_TXT
            )
        }
        //LogIn button
        OutlinedButton(
            modifier = Modifier.padding(top = 30.dp),
            onClick = {
                status="login"
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
@Composable
fun LoginCard(){
    Text("COMPLETAR")
}

val FNAME_TEXT = "First Name"
val LNAME_TEXT = "Last Name"
val SUBMIT_TEXT = "Submit"
val CREATE_TITLE_TEXT = "Create User"

@Composable
fun CreateUserCard(){
    var firstname by remember {mutableStateOf("")}
    var lastname by remember { mutableStateOf("")}
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp),
        ){
        Column {
            //Title
            Text(CREATE_TITLE_TEXT,
                modifier = Modifier
                    .padding(top=20.dp, bottom = 20.dp, start = 20.dp),
                fontSize = 30.sp
            )
            //First Name
            OutlinedTextField(
                value = firstname,
                onValueChange ={firstname=it},
                label = {Text(
                FNAME_TEXT)} )
            //Last Name
            OutlinedTextField(
                value = lastname,
                onValueChange ={lastname=it},
                label = {Text(
                    LNAME_TEXT)} )
            OutlinedButton(
                modifier = Modifier.padding(top = 30.dp),
                onClick = {  }
            ){
                Text(
                    text =SUBMIT_TEXT,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
