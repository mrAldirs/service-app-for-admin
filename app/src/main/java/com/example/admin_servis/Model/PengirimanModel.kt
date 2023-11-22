package com.example.admin_servis.Model

data class PengirimanModel(
    val kd_jadwal: String,
    val kd_user: String,
    val nama: String,
    val no_hp: String,
    val alamat: String,
    val kd_transaksi: String,
    val nama_barang: String,
    val img_barang: String,
    val total_bayar: String,
    val tglkirim_jadwal: String,
    val nama_pengirim: String,
    val tglsampai_jadwal: String,
    val bukti_kirim: String
)
