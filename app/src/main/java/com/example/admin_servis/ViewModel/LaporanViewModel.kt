package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.admin_servis.Model.LabaRugi
import com.example.admin_servis.Model.LaporanModel
import com.example.admin_servis.Model.UserTransaksiMain
import com.example.admin_servis.Repository.LaporanRepository

class LaporanViewModel: AndroidViewModel {

    private val laporanRepository: LaporanRepository

    constructor(application: Application) : super(application) {
        laporanRepository = LaporanRepository(application)
    }

    private val _laporanData = MutableLiveData<List<LaporanModel>>()
    val laporanData: LiveData<List<LaporanModel>> = _laporanData

    fun getLaporan() {
        laporanRepository.laporan { laporanList, error ->
            if (error != null) {
                // Error handling
            } else {
                _laporanData.value = laporanList
            }
        }
    }

    fun getPendapatan(): LiveData<Pair<LaporanModel?, String?>> {
        val result = MutableLiveData<Pair<LaporanModel?, String?>>()
        laporanRepository.pendapatan() { pendapatan, error ->
            result.value = Pair(pendapatan, error)
        }
        return result
    }

    fun detailPendapatan(thn: String): LiveData<List<LabaRugi>> {
        return laporanRepository.detailPendapatan(thn)
    }

    fun getTransactionData(): LiveData<List<UserTransaksiMain>> {
        return laporanRepository.transaksiDetail()
    }
}