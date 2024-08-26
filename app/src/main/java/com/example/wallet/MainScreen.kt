package com.example.wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp

import com.example.wallet.db.ActivityDb

import com.example.wallet.db.CardsDb
import com.example.wallet.db.UsersDb
import kotlinx.coroutines.time.delay
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.time.Duration


const val MS = "MainScreen"
val toEncrypt = mutableListOf<ByteArray>()
val encrypted = mutableListOf<ByteArray>()


@Composable
fun MainScreen(
    gotoLogin: () -> Unit,
    userid: String,
    usersDb: UsersDb,
    activityDb: ActivityDb,
    cardsDB: CardsDb,
    context: Context
) {
    val TAG = "MAIN SCREEN"
    Log.v(TAG, "UserId at Main $userid")

    var status by remember { mutableStateOf("") }
    fun setStatus(s: String) {
        status = s
    }


    fun encryptor(data: ByteArray) {//TODO rework idea to make reusable for other objects
        toEncrypt.clear()
        Log.v("ENCRYPTOR", "$data")
        toEncrypt.add(data)
        Log.v("ENCRYPTOR2", "$toEncrypt")
        setStatus("toEncrypt")

    }
    AuthenticateMain(successCallback = { setStatus("ok") }, exitCallback = { setStatus("no") })
    when (status) {
        "ok" -> {
            Authenticated(
                gotoLogin = gotoLogin,
                userid = userid,
                usersDb = usersDb,
                activityDb = activityDb,
                cardsDB = cardsDB,
                context = context,
                setStatus = { s -> status = s }
            )
        }

        "addCard" -> {
            AddCreditCard(
                encryptor = { data: ByteArray -> encryptor(data) },
                context = context,
                currentUser = usersDb.getUsers(userid.toInt())[0]
            )
        }

        "toLogin" -> {
            gotoLogin()
        }
        "toEncrypt" -> {
            Log.v(TAG, "LLEG")
            BioEncryptData(doneCallback = { d: ByteArray? ->
                if (d != null) {
                    encrypted.add(d)
                    setStatus("toSaveEncrypted")
                }
            }, exitCallback = { gotoLogin() }, toEncrypt.last(), userid)
        }
        "toSaveEncrypted" -> {
            Log.v("To Encrypt",encrypted.last().toString(Charset.defaultCharset()))
            Log.v("SAVE ENCRYPTED", "$encrypted")
            //save encrypted
            encrypted.clear()
            setStatus("ok")
        }
        "toShowQR"->{

        }
        else -> {
            Text(text = "Authenticate!", Modifier.padding(100.dp))
        }
    }
}

val ADD_CARDT_TXT = "Add Card"

@Composable
fun Authenticated(
    gotoLogin: () -> Unit,
    userid: String,
    usersDb: UsersDb,
    activityDb: ActivityDb,
    cardsDB: CardsDb,
    context: Context,
    setStatus: (String) -> Unit
) {

    val currentUser = usersDb.getUsers(ID = userid.toInt())[0]
    activityDb.save(
        ActivityData(
            userid = currentUser.id,
            status = 1,
            time = getTimeString()
        )
    ) //SaveS connection activity TODO addDisconnection

    val cards = cardsDB.getCards(currentUser.id)


    Column {
        Button(onClick = {
            gotoLogin()
            activityDb.save(
                ActivityData(
                    userid = currentUser.id,
                    status = 0,
                    time = getTimeString()
                )
            )
        }) { Text(text = "LogOut") }
        Text("Hola, ${currentUser.firstName} ${currentUser.lastName}!!")
        Text("Saldo : ${currentUser.saldo}") //Hardcoded at UserData Class, only to show saved data TODO add saveSaldo(userId) to db handler
        Button(onClick = {
            //TODO http request and handle response!

        }, modifier = Modifier.padding(20.dp)) { Text(text = "Generate QR") }
        Button(
            onClick = { setStatus("addCard") },
            modifier = Modifier.padding(20.dp)
        ) { Text(text = "Add Card") }
    }
}

@Composable
fun CardsPreview(cards: MutableList<CardData>, currentUser: UserData) {

    for (c in cards) {
        CardWidget(card = c, currentUser = currentUser)
    }
}

val brandNum = mapOf("3" to "amex", "4" to "visa", "5" to "mastercard")

@Composable
fun CardWidget(card: CardData, currentUser: UserData) {
    Card(modifier = Modifier.padding(20.dp),
        onClick = {//TODO GOTO PAY

        }) {
        var brand = "Unknow"
        if (brandNum.containsKey(card.brand)) {
            brand = brandNum[card.brand].toString()
        }
        Text(
            text = "Last 4: $card.public. Brand:$brand Name:${currentUser.firstName} ${currentUser.lastName}",
            Modifier.padding(10.dp)
        )
    }
}

const val FN = "First Name"
const val LN = "Last Name"
const val NUM = "Card Number"
const val SECURE = "Security Numbers"
const val EXP = "Expiration Date"

@Composable
fun AddCreditCard(
    encryptor: (ByteArray) -> Unit,
    context: Context,
    currentUser: UserData
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var secure by remember { mutableStateOf("") }
    var expire by remember { mutableStateOf("") }
    val modifier = Modifier.padding(10.dp)

    Column {
        OutlinedTextField(
            value = firstname,
            onValueChange = { firstname = it },
            label = {
                Text(
                    FN
                )
            }, modifier = modifier
        )
        OutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = {
                Text(
                    LN
                )
            }, modifier = modifier
        )
        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = {
                Text(
                    NUM
                )
            }, modifier = modifier
        )
        OutlinedTextField(
            value = secure,
            onValueChange = { secure = it },
            label = {
                Text(
                    SECURE
                )
            }, modifier = modifier
        )
        OutlinedTextField(
            value = expire,
            onValueChange = { expire = it },
            label = {
                Text(
                    EXP
                )
            }, modifier = modifier
        )
        OutlinedButton(
            modifier = modifier,
            onClick = {

                if (firstname != currentUser.firstName || lastname != currentUser.lastName) {
                    Toast.makeText(
                        context,
                        "Names must be equal to user names!!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.v(MS, "BAD NAME INPUTS AT CREATE CARD")
                } else if (number[0] == '3') { //amex
                    if (number.length != 15 || secure.length != 4) {
                        Toast.makeText(context, "WRONG NUMBER OR SECURE NUMBER", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (number.length != 16 || secure.length != 3) {
                    Toast.makeText(context, "WRONG NUMBER OR SECURE NUMBER", Toast.LENGTH_SHORT)
                        .show()
                } else if (expire.length != 4) {
                    Toast.makeText(context, "WRONG EXPIRE DATE NEEDS 4 DIGITS", Toast.LENGTH_SHORT)
                        .show()

                } else if (firstname == "" || lastname == "" || number == "" || secure == "" || expire == "") {
                    Toast.makeText(context, "Complete all fields!", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    val data = number + secure + expire

//                        currentUser.id.toString(),
//                        number.slice(0..4),
//                        number[0].toString()
                //TODO slice encrypted superchunk to populate evenly sensible data columns, add the public data to that row. Then sum chunks , decrypt and slice data

                    val bdata=data.toByteArray()

                    Log.v(MS, "DATA: ${data} ")
                    encryptor(bdata)
                }

            }
        ) {
            Text(
                text = SUBMIT_TEXT,
                style = MaterialTheme.typography.labelMedium
            )
        }
        //description text
        Text(
            text = """
                First Name:$firstname
                Last Name: $lastname
                Number: $number
                Secure: $secure
                Expires: ${expire}
                Brand: ${if (number.isNotEmpty()) (brandNum.get(number[0].toString()) ?: "unknown") else ""}
            """.trimIndent(), modifier = modifier
        )
    }
}