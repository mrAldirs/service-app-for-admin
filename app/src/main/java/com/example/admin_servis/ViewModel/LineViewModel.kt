package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin_servis.Model.LineModel
import com.example.admin_servis.Repository.LineRepository

class LineViewModel : AndroidViewModel {

    private val lineRepository: LineRepository

    constructor(application: Application) : super(application) {
        lineRepository = LineRepository(application)
    }

    fun getTransaksi(): LiveData<List<LineModel>> {
        return lineRepository.detail()
    }
}