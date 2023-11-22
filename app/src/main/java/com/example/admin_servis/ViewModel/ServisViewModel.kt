package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.admin_servis.Model.ServisLineModel
import com.example.admin_servis.Model.ServisModel
import com.example.admin_servis.Repository.ServisRepository

class ServisViewModel : AndroidViewModel {

    private val servisRepository: ServisRepository

    constructor(application: Application) : super(application) {
        servisRepository = ServisRepository(application)
    }

    fun getLineChart(): LiveData<List<ServisLineModel>> {
        return servisRepository.lineChart()
    }

    fun batalkan(kd: String): LiveData<Boolean> {
        return servisRepository.batalkan(kd)
    }

    private val _servisData = MutableLiveData<List<ServisModel>>()
    val servisData: LiveData<List<ServisModel>>
        get() = _servisData

    // Add a new LiveData for controlling the visibility of the emptyTextView
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    fun loadData(mode: String, nama: String) {
        servisRepository.loadData(mode, nama) { isError, dataList ->
            if (!isError) {
                _servisData.value = dataList
                _isEmpty.value = dataList.isNullOrEmpty() // Set isEmpty based on the data
            } else {
                _isEmpty.value = true // Error occurred, data is considered empty
            }
        }
    }
}