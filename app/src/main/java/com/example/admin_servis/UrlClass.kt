package com.example.admin_servis

import com.example.admin_servis.GlobalData.BaseUrl

class UrlClass {

    // url local
    var local = "http://192.168.137.1/api_service/"
//    var local = "https://rukinservice.000webhostapp.com/"

    // url chart
    var urlMain = local+"admin/admin_main.php"

    // url crud_user
    var urlAkun = local+"admin/admin_crud_akun.php"

    // url crud_user
    var urlUser = local+"admin/admin_crud_user.php"

    // url validasi
    var validasi = local+"login.php"

    // url crud_transaksi
    var urlTransaksi = local+"admin/admin_crud_transaksi.php"

    // url crud_pembayaran
    var urlPembayaran = local+"admin/admin_crud_pembayaran.php"

    // url crud_katalog
    var urlKatalog = local+"admin/admin_crud_katalog.php"

    // url crud_servis
    var urlServis = local+"admin/admin_crud_servis.php"

    // url crud_notifikasi
    var urlNotifikasi = local+"admin/admin_crud_notifikasi.php"

    // url crud_feedback
    var urlFeedback = local+"admin/admin_crud_feedback.php"

    // url crud_riwayat
    var urlRiwayat = local+"admin/admin_crud_riwayat.php"

    // url crud_chat
    var urlChat = "$local/chat.php"

    // url crud_ongkir
    var urlOngkir = "$local/ongkir.php"
}