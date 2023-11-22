package com.example.admin_servis.View.Barang

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.admin_servis.Model.BarangModel
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.BarangViewModel
import com.example.admin_servis.databinding.BrgtersediaEditFragmentBinding
import com.example.admin_servis.Helper.MediaHelper
import com.example.admin_servis.Helper.PhotoHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.squareup.picasso.Picasso
import com.yigitserin.currencyedittext.CurrencyEditText
import java.util.Locale

class FragmentTersediaEdit : BottomSheetDialogFragment() {
    private lateinit var b: BrgtersediaEditFragmentBinding
    private lateinit var bVM: BarangViewModel
    lateinit var v: View

    lateinit var photoHelper: PhotoHelper
    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    var fileUri = Uri.parse("")
    var kdK = ""
    var bHarga = ""

    companion object {
        private const val ARG_ID = "id"

        fun newInstance(id: String): FragmentTersediaEdit {
            val fragment = FragmentTersediaEdit()
            val args = Bundle()
            args.putString(ARG_ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BrgtersediaEditFragmentBinding.inflate(layoutInflater)
        v = b.root

        mediaHealper = MediaHelper(v.context)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        bVM = ViewModelProvider(this).get(BarangViewModel::class.java)
        b.txKdBarangEdit.setText(arguments?.getString(ARG_ID))

        val txHarga: CurrencyEditText = v.findViewById(R.id.txHargaBarangEdit)
        txHarga.locale = Locale("id", "ID")
        txHarga.decimalDigits = 2

        b.btnChooseBarangEdit.setOnClickListener {
            var photoMenu = PopupMenu(v.context, it)
            photoMenu.menuInflater.inflate(R.menu.menu_photo, photoMenu.menu)
            photoMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuPhotoRealtime -> {
                        requestPermission()
                    }
                    R.id.menuPhotoManager -> {
                        val intent = Intent()
                        intent.setType("image/*")
                        intent.setAction(Intent.ACTION_GET_CONTENT)
                        startActivityForResult(intent,mediaHealper.RcGallery())
                    }
                }
                false
            }
            photoMenu.show()
        }

        b.btnBatalkan.setOnClickListener {
            dismiss()
        }

        b.btnTutup.setOnClickListener {
            dismiss()
        }

        b.btnSimpanEditBarang.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin mengedit barang pada katalog?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    edit()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        detail()

        return v
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(b.imgBarangEdit, fileUri)
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
            mediaHealper.RcGallery() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        b.imgBarangEdit.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.imgBarangEdit)
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    }
                }
            }
        }
    }

    fun detail() {
        bVM.detail(arguments?.getString(ARG_ID)!!).observe(this, Observer { barang ->
            kdK = barang.kd_katalog
            b.txNamaBarangEdit.setText(barang.nama_barang)
            b.txHargaBarangEdit.setText(barang.harga_katalog)
            b.txSpekBarangEdit.setText(barang.spesifikasi)
            b.txKetBarangEdit.setText(barang.ket_barang)
            Picasso.get().load(barang.img_barang).into(b.imgBarangEdit)
        })
    }

    fun edit() {
        bHarga = b.txHargaBarangEdit.text.toString().replace(",", "")
        val data = BarangModel(
            kd_barang = "",
            kd_katalog = kdK,
            status_katalog = "",
            tgl_upload = "",
            nama_barang = b.txNamaBarangEdit.text.toString(),
            jenis_barang = "",
            harga_katalog = bHarga.toString(),
            spesifikasi = b.txSpekBarangEdit.text.toString(),
            ket_barang = b.txKetBarangEdit.text.toString(),
            img_barang = imStr
        )

        bVM.edit(arguments?.getString(ARG_ID)!!, data).observe(this, Observer { result ->
            requireActivity().recreate()
            dismiss()
            Toast.makeText(this.context,"Berhasil mengubah barang pada katalog!", Toast.LENGTH_LONG).show()
        })
    }
}