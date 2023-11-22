package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin_servis.Model.UserModel
import com.example.admin_servis.Repository.UserRepository

class UserViewModel : AndroidViewModel {

    private val userRepository : UserRepository

    constructor(application: Application) : super(application) {
        userRepository = UserRepository(application)
    }

    fun loadData(nama: String) : LiveData<List<UserModel>> {
        return userRepository.loadData(nama)
    }

    fun detail(kd:String) : LiveData<UserRepository.DetailData> {
        return userRepository.detail(kd)
    }
}