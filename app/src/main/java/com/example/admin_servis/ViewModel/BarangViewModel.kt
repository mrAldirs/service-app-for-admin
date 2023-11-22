package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin_servis.Model.BarangModel
import com.example.admin_servis.Repository.BarangRepository

class BarangViewModel : AndroidViewModel {

    private val barangRepository: BarangRepository

    constructor(application: Application): super(application) {
        barangRepository = BarangRepository(application)
    }

    fun loadData(kd:String, nama:String): LiveData<List<BarangModel>> {
        return barangRepository.loadData(kd, nama)
    }

    fun detail(kd: String): LiveData<BarangModel> {
        return barangRepository.detail(kd)
    }

    fun delete(kd: String): LiveData<Boolean> {
        return barangRepository.delete(kd)
    }

    fun insert(barang: BarangModel): LiveData<Boolean> {
        return barangRepository.insert(barang)
    }

    fun edit(kd: String, barang: BarangModel): LiveData<Boolean> {
        return barangRepository.edit(kd, barang)
    }
}