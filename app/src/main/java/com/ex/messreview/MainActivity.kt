package com.ex.messreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ex.messreview.Screens.daysOfWeek
import com.ex.messreview.navigation.AppNavigation
import com.ex.messreview.ui.theme.MessReviewTheme
import com.ex.messreview.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel : ViewModel() {
    val username = MutableLiveData<String>()
    val messType = MutableLiveData<String?>()
    val menuData = MutableLiveData<Map<String, Map<String, List<String>>>>()
    fun login(username: String) {
        this.username.value = username
        fetchMessType(username)
    }

    private fun fetchMessType(username: String) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            try {
                val documentSnapshot = db.collection("Users").document(username).get().await()
                val messTypeValue = documentSnapshot.getString("Mess")
                messType.postValue(messTypeValue)
                messTypeValue?.let {
                    fetchMenuData(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                messType.postValue(null)
            }
        }
    }
    private fun fetchMenuData(messType: String) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            val menuDataMap = mutableMapOf<String, MutableMap<String, List<String>>>()

            try {
                for (day in daysOfWeek) {
                    val documentSnapshot = db.collection(messType).document(day).get().await()
                    val dayMenuData = documentSnapshot.data?.mapValues { entry ->
                        entry.value as List<String>
                    } ?: emptyMap()
                    menuDataMap[day] = dayMenuData.toMutableMap()
                }
                menuData.postValue(menuDataMap)
            } catch (e: Exception) {
                e.printStackTrace()
                menuData.postValue(emptyMap())
            }
        }
    }
}
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        setContent {
            MessReviewTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(sharedViewModel,authViewModel)
                }
            }
        }
    }
}



