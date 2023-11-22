package com.example.admin_servis.Model

data class ServisModel (
    val kd_transaksi: String,
    val tgl_transaksi: String,
    val status_transaksi: String,
    val catatan_transaksi: String,
    val tgl_servis: String,
    val kerusakan: String,
    val kd_barang: String,
    val nama_barang: String,
    val jenis_barang: String,
    val ket_barang: String,
    val nama_pelanggan: String,
    val alamat: String,
    val email: String
)