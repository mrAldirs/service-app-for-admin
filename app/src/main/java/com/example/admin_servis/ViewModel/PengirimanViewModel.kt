package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin_servis.Model.PengirimanModel
import com.example.admin_servis.Repository.PengirimanRepository

class PengirimanViewModel : AndroidViewModel {

    private val pengirimanRepository: PengirimanRepository

    constructor(application: Application) : super(application) {
        pengirimanRepository = PengirimanRepository(application)
    }

    fun loadData(mode: String, nama: String): LiveData<List<PengirimanModel>> {
        return pengirimanRepository.loadData(mode, nama)
    }

    fun detail(kd: String): LiveData<PengirimanModel> {
        return pengirimanRepository.detail(kd)
    }

    fun bukti(mode: String, kd: String, kirim:PengirimanModel): LiveData<Boolean> {
        return pengirimanRepository.bukti(mode, kd, kirim)
    }
}