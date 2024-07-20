package com.ex.messreview.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ex.messreview.R
import com.ex.messreview.SharedViewModel
import com.ex.messreview.viewmodel.AuthState
import com.ex.messreview.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(sharedViewModel: SharedViewModel, authViewModel: AuthViewModel, navController: NavHostController) {
    val username by sharedViewModel.username.observeAsState("User")
    val messType by sharedViewModel.messType.observeAsState(null)
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                    .compositeOver(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Occupies 1/3rd of the screen height
        ) {
            Image(
                painter = painterResource(id = R.drawable.pfp), // Replace with your image resource
                contentDescription = "Profile Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(200.dp) // Adjust size as needed
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(1.dp)) // Add vertical spacing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            // Occupies 1/3rd of the screen height
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                ){
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                        )
                }
                Spacer(modifier = Modifier.height(50.dp)) // Expands space between icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                ){
                    Icon(
                        imageVector = Icons.Filled.Home, // Replace with appropriate icon
                        contentDescription = "Mess Icon",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$messType",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.5f),
            contentAlignment = Alignment.Center
            )

        {
            Button(
                onClick = {authViewModel.signout()},
                modifier = Modifier
                    .width(145.dp)
                    .height(49.dp)

                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Text(
                    "Sign-Out",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }
    }
}