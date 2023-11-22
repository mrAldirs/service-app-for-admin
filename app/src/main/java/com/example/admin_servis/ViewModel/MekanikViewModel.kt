package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.admin_servis.Model.MekanikModel
import com.example.admin_servis.Repository.MekanikRepository

class MekanikViewModel  : AndroidViewModel {

    private val mekanikRepository : MekanikRepository

    constructor(application: Application) : super(application) {
        mekanikRepository = MekanikRepository(application)
    }

    fun loadData(mode: String, nm: String) : LiveData<List<MekanikModel>> {
        return mekanikRepository.loadData(mode, nm)
    }

    fun detail(kd:String) : LiveData<MekanikModel> {
        return mekanikRepository.detail(kd)
    }
}