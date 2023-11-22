package com.example.admin_servis.View.Pengiriman

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.admin_servis.Model.PengirimanModel
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.PengirimanViewModel
import com.example.admin_servis.databinding.PengirimanBuktiFragmentBinding
import com.example.admin_servis.Helper.PhotoHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class FragmentPengirimanBukti : BottomSheetDialogFragment() {
    private lateinit var b: PengirimanBuktiFragmentBinding
    private lateinit var pVM : PengirimanViewModel
    lateinit var v: View
    lateinit var parent: PengirimanDetailActivity

    lateinit var photoHelper: PhotoHelper
    var imStr = ""
    var fileUri = Uri.parse("")

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = PengirimanBuktiFragmentBinding.inflate(layoutInflater)
        v = b.root
        parent = activity as PengirimanDetailActivity

        pVM = ViewModelProvider(this).get(PengirimanViewModel::class.java)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.btnChoose.setOnClickListener {
            requestPermission()
        }

        b.btnBatalkan.setOnClickListener { dismiss() }
        b.btnTutup.setOnClickListener { dismiss() }

        b.btnKirim.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin mengirim bukti pengiriman barang?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (imStr.isNotEmpty()) {
                        sendBukti("bukti_noncod")
                    } else {
                        Toast.makeText(v.context, "Tolong foto bukti pengiriman terlebih dahulu!", Toast.LENGTH_SHORT).show()
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
                        imStr = photoHelper.getBitMapToString(b.insFoto, fileUri)
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    fun sendBukti(mode: String) {
        val data = PengirimanModel(
            "","","","","","","","","","","","",
            bukti_kirim = imStr
        )

        pVM.bukti(mode, arguments?.get("kode").toString(), data).observe(requireActivity(), Observer { result ->
            dismiss()
            parent.detail(arguments?.get("kode").toString())
            Toast.makeText(v.context, "Berhasil mengirim data pengiriman barang ke lokasi!", Toast.LENGTH_SHORT).show()
        })
    }
}