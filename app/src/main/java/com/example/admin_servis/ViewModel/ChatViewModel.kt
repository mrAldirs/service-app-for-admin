package com.example.admin_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin_servis.Model.ChatModel
import com.example.admin_servis.Repository.ChatRepository

class ChatViewModel : AndroidViewModel {

    private val chatRepository : ChatRepository

    constructor(application: Application) : super(application) {
        chatRepository = ChatRepository(application)
    }

    fun loadData(topik: String, kdParent: String): LiveData<List<ChatModel>> {
        return chatRepository.loadData(topik, kdParent)
    }

    fun loadDataPelanggan(topik: String, nm: String): LiveData<List<ChatModel>> {
        return chatRepository.loadDataPelanggan(topik, nm)
    }

    fun insert(chat: ChatModel, nmFile: String): LiveData<Boolean> {
        return chatRepository.insert(chat, nmFile)
    }

    fun delete(kd: String): LiveData<Boolean> {
        return chatRepository.delete(kd)
    }
}