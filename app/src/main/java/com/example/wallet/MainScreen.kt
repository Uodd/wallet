package com.example.wallet
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.wallet.db.ActivityDb

import com.example.wallet.db.CardsDb
import com.example.wallet.db.UsersDb

@Composable
fun MainScreen(
    gotoLogin: ()-> Unit,
    userid : String,
    usersDb:UsersDb,
    activityDb: ActivityDb,
    cardsDB: CardsDb

){
    val TAG="MAIN SCREEN"
    Log.v(TAG,"UserId at Main $userid")

    var status by remember { mutableStateOf("") }
    fun setStatus(s:String){
        status=s
    }

    when(status){
        "ok" -> {
            Authenticated(
                gotoLogin = gotoLogin,
                userid = userid ,
                usersDb = usersDb ,
                activityDb = activityDb,
                cardsDB = cardsDB
            )
        }
        "else" -> {
            Text(text = "Authenticate!",Modifier.padding(100.dp))
        }
        else -> {
        }
    }
    AutenticateMain(successCallback = { setStatus("ok") }, exitCallback = { setStatus("no") })

}

@Composable
fun Authenticated(gotoLogin: ()-> Unit,
                  userid : String,
                  usersDb:UsersDb,
                  activityDb: ActivityDb,
                  cardsDB: CardsDb){
    val currentUser= usersDb.getUsers(ID=userid.toInt())[0]
    //var qr by remember { mutableStateOf("") }
    activityDb.save(ActivityData(userid = currentUser.id, status = 1,time= getTimeString())) //Save connection activity TODO addDisconnection
    val cards = cardsDB.getCards(currentUser.id)
    for(c in cards){
        Log.v("AAAA","$c")
    }
    Column {
        Text("Hola, ${currentUser.firstName} ${currentUser.lastName}!!")
        Text("Saldo : ${currentUser.saldo}") //Hardcoded at UserData Class, only to show saved data TODO add saveSaldo(userId) to db handler
        Button(onClick = { }) {
            Text(text = "Generate QR")
        }
        CardsPreview(cards = cards, currentUser = currentUser )

        Button(onClick = {}) {
            Text(text = "Add Credit Card")
        }
//        when(status){
//            "addcard"->{
//
//            }
//            "qr"->{
//
//            }else->{
//
//            }
//        }
    }
}
@Composable
fun CardsPreview(cards:MutableList<CardData>,currentUser:UserData) {
    for(c in cards){
        CardWidget(card = c, currentUser = currentUser )
    }
}
val brandNum= mapOf("3" to "amex", "4" to "visa", "5" to "mastercard" )

@Composable
fun CardWidget(card:CardData,currentUser:UserData){
    Card(modifier = Modifier.padding(20.dp),
        onClick = {//TODO GOTO PAY

        }){
        var brand ="Unknow"
        if(brandNum.containsKey(card.brand)){
            brand = brandNum[card.brand].toString()
        }
        Text(text ="Last 4: $card.public. Brand:$brand Name:${currentUser.firstName} ${currentUser.lastName}" ,Modifier.padding(10.dp))
    }
}
@Composable
fun AddCreditCard(currentUser: UserData){

}