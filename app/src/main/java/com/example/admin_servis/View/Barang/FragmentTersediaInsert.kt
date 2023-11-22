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
import com.example.admin_servis.databinding.BrgtersediaInsertFragmentBinding
import com.example.admin_servis.Helper.MediaHelper
import com.example.admin_servis.Helper.PhotoHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.yigitserin.currencyedittext.CurrencyEditText
import kotlinx.android.synthetic.main.brgtersedia_main_activity.*
import java.util.Locale

class FragmentTersediaInsert : BottomSheetDialogFragment() {
    private lateinit var b: BrgtersediaInsertFragmentBinding
    private lateinit var bVM: BarangViewModel
    lateinit var thisParent : TersediaMainActivity
    lateinit var v : View
    lateinit var photoHelper: PhotoHelper
    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    var fileUri = Uri.parse("")

    var jns = ""
    var bHarga = ""

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BrgtersediaInsertFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as TersediaMainActivity

        mediaHealper = MediaHelper(v.context)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        bVM = ViewModelProvider(this).get(BarangViewModel::class.java)

        val txHarga: CurrencyEditText = v.findViewById(R.id.txHargaBarangInsert)
        txHarga.locale = Locale("id", "ID")
        txHarga.decimalDigits = 2

        b.btnChoosePhotoBarang.setOnClickListener {
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

        b.rgJenisBarangInsert.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbJenisAC -> jns = "AC"
                R.id.rbJenisHp -> jns = "Handphone"
                R.id.rbJenisTelevisi -> jns = "Televisi"
                R.id.rbJenisLaptop -> jns = "Laptop"
            }
        }

        b.btnSimpanInsertBarang.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin menambahkan barang pada katalog?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (b.txNamaBarangInsert.text!!.isNotEmpty() && jns.isNotEmpty() && b.txSpekBarangInsert.text!!.isNotEmpty() &&
                            b.txHargaBarangInsert.text!!.isNotEmpty() && b.txKetBarangInsert.text!!.isNotEmpty() && imStr.isNotEmpty()) {
                        insert()
                    } else {
                        Toast.makeText(thisParent, "Tolong lengkapi data terlebih dahulu!", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

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
                        imStr = photoHelper.getBitMapToString(b.imgPhotoBarang, fileUri)
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
            mediaHealper.RcGallery() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        b.imgPhotoBarang.setBackgroundColor(Color.BLACK)
                        imStr = mediaHealper.getBitmapToString(data!!.data,b.imgPhotoBarang)
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    }
                }
            }
        }
    }

    fun insert() {
        bHarga = b.txHargaBarangInsert.text.toString().replace(",", "")
        if (!b.txNamaBarangInsert.text.toString().equals("") && !b.txSpekBarangInsert.text.toString().equals("") && !b.txHargaBarangInsert.text.toString().equals("") &&
            !b.txKetBarangInsert.text.toString().equals("") && imStr !== "") {
            val data = BarangModel(
                kd_barang = "",
                kd_katalog = "",
                status_katalog = "",
                tgl_upload = "",
                nama_barang = b.txNamaBarangInsert.text.toString(),
                jenis_barang = jns,
                harga_katalog = bHarga,
                spesifikasi = b.txSpekBarangInsert.text.toString(),
                ket_barang = b.txKetBarangInsert.text.toString(),
                img_barang = imStr
            )
            bVM.insert(data).observe(requireActivity(), Observer { result ->
                Toast.makeText(v.context, "Berhasil menambahkan barang baru ke katalog!", Toast.LENGTH_SHORT).show()
                dismiss()
                thisParent.restartActivity()
            })
        } else {
            Toast.makeText(v.context, "Tolong lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
}