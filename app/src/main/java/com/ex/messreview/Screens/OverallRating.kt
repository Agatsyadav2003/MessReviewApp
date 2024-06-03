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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            text = "Review",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
        ) {
            ReviewCard(text = "Breakfast", modifier = Modifier.weight(0.75f))
            ReviewCard(text = "Lunch", modifier = Modifier.weight(0.75f))
            ReviewCard(text = "High Tea", modifier = Modifier.weight(0.75f))
            ReviewCard(text = "Dinner", modifier = Modifier.weight(0.75f))
            Spacer(modifier = Modifier.height(20.dp))
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

    Box(
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = { selectedStars = 0 })
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                repeat(5) {
                    StarButton(
                        isSelected = it < selectedStars,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
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
            .padding(0.1.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = starIcon,
            contentDescription = if (isSelected) "Filled Star" else "Outlined Star",
            tint = if (isSelected) Color.Yellow else Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ORatingPreview() {
    OverallRatingScreen()
}
