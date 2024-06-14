package com.ex.messreview.Screens

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

@Composable
fun OverallRatingScreen() {
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
            ReviewCard(text = "Breakfast", modifier = Modifier.weight(0.75f))
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "Lunch", modifier = Modifier.weight(0.75f))
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "High Tea", modifier = Modifier.weight(0.75f))
            Spacer(modifier = Modifier.height(5.dp))
            ReviewCard(text = "Dinner", modifier = Modifier.weight(0.75f))
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = { /*TODO*/ },
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

@Composable
fun ReviewCard(text: String, modifier: Modifier) {
    var selectedStars by remember { mutableStateOf(0) }

    Card(
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .clickable(onClick = { selectedStars = 0 }),
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
                        isSelected = it < selectedStars,
                        modifier = Modifier
                            .weight(0.5f)

                            .align(Alignment.CenterVertically)

                    ) {
                        selectedStars = if (it == selectedStars) 0 else it + 1
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

@Preview(showBackground = true)
@Composable
fun ORatingPreview() {
    OverallRatingScreen()
}
