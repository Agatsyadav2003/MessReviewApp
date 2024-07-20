package com.ex.messreview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.messreview.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>().apply { value = AuthState.Unauthenticated }
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }
    fun getCurrentUsername(): String? {
        val email = auth.currentUser?.email
        return email?.substringBefore("@")
    }

    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated

        }
    }

    fun login(username : String,password : String, sharedViewModel: SharedViewModel){



        sharedViewModel.login(username)
        if (username.isEmpty() || password.isEmpty()) {_authState.value = AuthState.Error("Email or password can't be empty")
            return}
        _authState.value = AuthState.Loading

        val email = formatUsername(username)
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }


    }



    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}
fun formatUsername(username: String): String {
    return if (username.matches(Regex("21bce\\d{4}"))) {
        "$username@gmail.com"
    } else {
        username
    }
}
