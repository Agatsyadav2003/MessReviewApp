package com.ex.messreview.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ex.messreview.R
import com.ex.messreview.data.menuData
import java.time.LocalDate
import java.time.LocalTime

val currentDayIndex = LocalDate.now().dayOfWeek.value % 7

val currentMealTime = when (LocalTime.now()) {
    in LocalTime.MIDNIGHT..LocalTime.of(8, 59) -> "Breakfast"
    in LocalTime.of(9, 0)..LocalTime.of(13, 59) -> "Lunch"
    in LocalTime.of(14, 0)..LocalTime.of(18, 59) -> "High Tea"
    else -> "Dinner"
}

var selectedDay by mutableStateOf(currentDayIndex)
val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
val mealTimes = listOf("Breakfast", "Lunch", "High Tea", "Dinner")
var selectedMealTime by mutableStateOf(currentMealTime)

@Composable
fun FoodItemList(day: String, mealTime: String, navController: NavHostController) {
    val menuItems = menuData[day]?.get(mealTime) ?: listOf()

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
        items(menuItems) { menuItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        navController.navigate("rating_screen/$menuItem/${R.drawable.foodimg}")
                    },
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
                            .size(70.dp)
                            .clip(CircleShape)
                            .padding(end = 15.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.weight(2f),
                    ) {
                        Text(
                            text = menuItem,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("rating_screen/$menuItem/${R.drawable.foodimg}")
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

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "Good Evening User",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            daysOfWeek.forEachIndexed { index, day ->
                val isSelected = selectedDay == index
                val buttonColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                val buttonShadow by animateDpAsState(targetValue = if (isSelected) 8.dp else 0.dp)
                val textColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)

                Button(
                    onClick = { selectedDay = index },
                    modifier = Modifier
                        .padding(3.dp)
                        .shadow(buttonShadow, CircleShape)
                        .background(buttonColor, CircleShape),
                    shape = CircleShape
                ) {
                    Text(text = day, color = textColor)
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            mealTimes.forEach { mealTime ->
                val isSelected = selectedMealTime == mealTime
                val buttonColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                val buttonShadow by animateDpAsState(targetValue = if (isSelected) 8.dp else 0.dp)
                val textColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)

                Button(
                    onClick = { selectedMealTime = mealTime },
                    modifier = Modifier
                        .padding(3.dp)
                        .shadow(buttonShadow, CircleShape)
                        .background(buttonColor, CircleShape),
                    shape = CircleShape
                ) {
                    Text(text = mealTime, color = textColor)
                }
            }
        }
        if (selectedDay > -1) {
            FoodItemList(day = daysOfWeek[selectedDay], mealTime = selectedMealTime, navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}
