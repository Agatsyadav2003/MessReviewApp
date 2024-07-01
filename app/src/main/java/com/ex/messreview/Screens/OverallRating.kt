package com.ex.messreview.Screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ex.messreview.R
import com.ex.messreview.SharedViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun OverallRatingScreen(sharedViewModel: SharedViewModel) {
    val username by sharedViewModel.username.observeAsState("User")
    val messType by sharedViewModel.messType.observeAsState(null)
    val database = FirebaseDatabase.getInstance()

    val averageBreakfastValueState =
        rememberFetchAverageValue(database, "breakfast", messType ?: "")
    val averageLunchValueState = rememberFetchAverageValue(database, "lunch", messType ?: "")
    val averageHighTeaValueState =
        rememberFetchAverageValue(database, "highTea", messType ?: "")
    val averageDinnerValueState = rememberFetchAverageValue(database, "dinner", messType ?: "")
    var breakfast = remember { mutableStateOf(0) }
    var lunch = remember { mutableStateOf(0) }
    var highTea = remember { mutableStateOf(0) }
    var dinner = remember { mutableStateOf(0) }
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now().format(dateFormat)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                    .compositeOver(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
    ) {
        Spacer(modifier = Modifier.height(15.dp))
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.5f),
//            text = "Review",
//            fontWeight = FontWeight.SemiBold,
//            style = MaterialTheme.typography.displaySmall,
//            textAlign = TextAlign.Center,
//            color = MaterialTheme.colorScheme.onBackground,
//        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f)
        ) {
            ReviewCard(text = "Breakfast", modifier = Modifier.weight(0.75f),
                selectedStars = breakfast,
                avg = averageBreakfastValueState)
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "Lunch", modifier = Modifier.weight(0.75f),
                selectedStars = lunch,
                avg = averageLunchValueState)
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "High Tea", modifier = Modifier.weight(0.75f),
                selectedStars = highTea,
                avg = averageHighTeaValueState)
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "Dinner", modifier = Modifier.weight(0.75f),
                selectedStars = dinner,
                avg = averageDinnerValueState)
            Spacer(modifier = Modifier.height(5.dp))
            val context = LocalContext.current
            Button(
                onClick = { val contactsRef = database.reference.child("Reviews").child("$messType")
                    val dateref = contactsRef.child(currentDate)
                    val randomName =
                        UUID.randomUUID().toString() // Generate a random UUID string
                    val contactRef =
                        dateref.child(username) // Use the random name for the child node

                    val contact =
                        Contact1(breakfast.value, lunch.value, highTea.value, dinner.value)
                    contactRef.setValue(contact)
                    Toast.makeText(context, "Save Review", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .width(145.dp)
                    .height(49.dp)
                    .align(Alignment.CenterHorizontally)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Text(
                    "Submit",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
data class Contact1(val Breakfast: Int, val Lunch: Int,val highTea:Int, val dinner: Int)
@Composable
fun ReviewCard(text: String, modifier: Modifier, selectedStars : MutableState<Int>, avg: MutableState<Int>) {


    Card(
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .clickable(onClick = {selectedStars.value=avg.value }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(36.dp)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)

    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(10.dp)
            ) {
                repeat(5) {
                    StarButton(
                        isSelected = it < selectedStars.value,
                        modifier = Modifier
                            .weight(0.5f)

                            .align(Alignment.CenterVertically)

                    ) {
                        selectedStars.value = if (it == selectedStars.value) 0 else it + 1
                    }
                }
            }
        }
    }
}

@Composable
fun StarButton(isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val starIcon = if (isSelected) {
        painterResource(id = R.drawable.ic_star_filled)
    } else {
        painterResource(id = R.drawable.ic_star_outline)
    }

    Box(
        modifier = modifier

            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = starIcon,
            contentDescription = if (isSelected) "Filled Star" else "Outlined Star",
            tint = if (isSelected) Color.Yellow else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun rememberFetchAverageValue(database: FirebaseDatabase, mealType: String,messType:String): MutableState<Int> {
    Log.d("Firebase", "messType received: $messType")
    val averageValue = remember { mutableStateOf(0) }
    val totalValues = remember { mutableStateOf(0) }
    val totalSum = remember { mutableStateOf(0) }
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now().format(dateFormat)

    val databaseRef = database.reference.child("Reviews").child(messType).child(currentDate)

    DisposableEffect(database) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sum = 0
                var count = 0
                snapshot.children.forEach { nameSnapshot ->
                    val mealSnapshot = nameSnapshot.child(mealType)
                    if (mealSnapshot.exists()) {
                        val mealValue = mealSnapshot.value as? Long
                        mealValue?.let {
                            sum += it.toInt()
                            count++
                        }
                    }
                }
                totalSum.value = sum
                totalValues.value = count
                if (count > 0) {
                    averageValue.value = sum.toInt() / count.toInt()
                } else {
                    averageValue.value = 0
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching $mealType values: $error")
            }
        }

        databaseRef.addValueEventListener(valueEventListener)

        onDispose {
            databaseRef.removeEventListener(valueEventListener)
        }

    }

    return averageValue
}




