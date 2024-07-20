package com.ex.messreview.Screens


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ex.messreview.R
import com.ex.messreview.SharedViewModel
import com.ex.messreview.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

val currentDayIndex = LocalDate.now().dayOfWeek.value % 7

val currentMealTime = when (LocalTime.now()) {
    in LocalTime.MIDNIGHT..LocalTime.of(8, 59) -> "Breakfast"
    in LocalTime.of(9, 0)..LocalTime.of(13, 59) -> "Lunch"
    in LocalTime.of(14, 0)..LocalTime.of(18, 59) -> "High Tea"
    else -> "Dinner"
}

var selectedDay by mutableIntStateOf(currentDayIndex)
val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
val mealTimes = listOf("Breakfast", "Lunch", "High Tea", "Dinner")
var selectedMealTime by mutableStateOf(currentMealTime)

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 100
                0.9f at 400
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(color = Color.Gray, shape = CircleShape)
                .alpha(alpha)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                .alpha(alpha)
        )
    }
}

@Composable
fun FoodItemList(day: String, mealTime: String, navController: NavHostController,menuData: Map<String, Map<String, List<String>>>,sharedViewModel: SharedViewModel) {
    var menuItems by remember { mutableStateOf(listOf<String>()) }
    val messType by sharedViewModel.messType.observeAsState(null)


    menuItems = menuData[day]?.get(mealTime) ?: listOf()
    val isLoading = menuData.isEmpty()
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Menu for $day - $mealTime",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }


        if (isLoading){
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = MaterialTheme.shapes.medium.copy(all = CornerSize(36.dp))
                ) {
                    LoadingAnimation()
                }
            }
        }else {
            items(menuItems) { menuItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp)
                    .clickable {
                        navController.navigate("rating_screen/$menuItem/${R.drawable.foodimg}/$messType-$day-$mealTime")
                    },
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium.copy(all = CornerSize(36.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.foodimg),
                        contentDescription = "Meal Image",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .padding(end = 15.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.weight(2f),
                    ) {
                        Text(
                            text = menuItem,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("rating_screen/$menuItem/${R.drawable.foodimg}/$messType-$day-$mealTime")
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Navigate to next screen",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController,sharedViewModel: SharedViewModel, authViewModel: AuthViewModel) {
    val username1 = authViewModel.getCurrentUsername()
    sharedViewModel.login(username1?:"")
    val username by sharedViewModel.username.observeAsState("User")

    val menuData by sharedViewModel.menuData.observeAsState(emptyMap())
    val dayListState = rememberLazyListState()
    val mealListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            dayListState.scrollToItem(selectedDay)
            mealListState.scrollToItem(mealTimes.indexOf(selectedMealTime))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                    .compositeOver(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ),
    ) {
        Text(
            text = "Good Evening $username",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            state = dayListState,
            modifier = Modifier.padding(16.dp)
        ) {
            items(daysOfWeek.size) { index ->
                val day = daysOfWeek[index]
                val isSelected = selectedDay == index
                val buttonColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background.copy(alpha = 0.65f)
                        .compositeOver(MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Button(
                    onClick = { selectedDay = index },
                    modifier = Modifier
                        .padding(3.dp)
                        .background(buttonColor, CircleShape),
                    shape = CircleShape
                ) {
                    Text(
                        text = day,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }
        }

        LazyRow(
            state = mealListState,
            modifier = Modifier.padding(16.dp)
        ) {
            items(mealTimes.size) { index ->
                val mealTime = mealTimes[index]
                val isSelected = selectedMealTime == mealTime
                val buttonColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background.copy(alpha = 0.65f)
                        .compositeOver(MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Button(
                    onClick = { selectedMealTime = mealTime },
                    modifier = Modifier
                        .padding(3.dp)
                        .background(buttonColor, CircleShape),
                    shape = CircleShape
                ) {
                    Text(
                        text = mealTime,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }
        }

        if (selectedDay > -1) {
            FoodItemList(day = daysOfWeek[selectedDay], mealTime = selectedMealTime, navController = navController,menuData = menuData,sharedViewModel)
        }
    }
}


