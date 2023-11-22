package com.example.admin_servis.View.Pengiriman

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.PengirimanViewModel
import com.example.admin_servis.databinding.PengirimanDetailActivityBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import mumayank.com.airlocationlibrary.AirLocation

class PengirimanDetailActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var b: PengirimanDetailActivityBinding
    private lateinit var pVM: PengirimanViewModel

    val MAPBOX_TOKEN = "pk.eyJ1IjoibmFzeWFyeCIsImEiOiJjbDQ4MmtnZWIwbzU0M2lwaTc2anVwM2xmIn0.Mz7mkrr00kTYrroq-qRg0g"
    var URL = ""
    var imgUrl = ""
    var tglSelesai = ""

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnGo ->{
                var paket : Bundle? = intent.extras
                var alm = paket?.getString("alm").toString()
                URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + "$alm.json?proximity=$lng,$lat&access_token=$MAPBOX_TOKEN&limit=1"
                getDestinationLocation(URL)
            }
        }
    }

    var lat : Double = 0.0; var lng : Double = 0.0;
    var airLoc : AirLocation? = null
    var gMap : GoogleMap? = null
    lateinit var mapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = PengirimanDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        pVM = ViewModelProvider(this).get(PengirimanViewModel::class.java)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        b.btnGo.setOnClickListener(this)

        b.btnKirim.setOnClickListener {
            if (!tglSelesai.equals("null")) {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.warning)
                    .setTitle("Peringatan!")
                    .setMessage("Anda telah mengirim bukti pengiriman barang!")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                        null
                    })
                    .show()
            } else {
                var paket : Bundle? = intent.extras
                var frag = FragmentPengirimanBukti()

                val bundle = Bundle()
                bundle.putString("kode", paket?.getString("kode").toString())
                frag.arguments = bundle

                frag.show(supportFragmentManager, "FragmentPengirimanBukti")
            }
        }

        b.btnFoto.setOnClickListener {
            if (imgUrl.isEmpty()) {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.warning)
                    .setTitle("Peringatan!")
                    .setMessage("Anda belum mengirim bukti pengiriman barang, tolong kirim terlebih dahulu!")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                        null
                    })
                    .show()
            } else {
                val intent = Intent(this, ImageDetailActivity::class.java)
                intent.putExtra("img", imgUrl)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        var paket : Bundle? = intent.extras
        detail(paket?.getString("kode").toString())
    }

    fun getDestinationLocation(url : String){
        val request = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener {
                val features = it.getJSONArray("features").getJSONObject(0)
                val place_name = features.getString("place_name")
                val center = features.getJSONArray("center")
                val lat = center.get(0).toString()
                val lng = center.get(1).toString()
                getDestinationRoutes(lng,lat,place_name)
            }, Response.ErrorListener {
                Toast.makeText(this,"can't get destination location", Toast.LENGTH_SHORT).show()
            })
        val q = Volley.newRequestQueue(this)
        q.add(request)
    }

    fun getDestinationRoutes(destLat : String, destLng : String, place_name : String){
        URL = "https://api.mapbox.com/directions/v5/mapbox/driving/" + "$lng,$lat;$destLng,$destLat?access_token=$MAPBOX_TOKEN&geometries=geojson"
        val request = JsonObjectRequest(
            Request.Method.GET,URL,null,
            Response.Listener {
                val routes = it.getJSONArray("routes").getJSONObject(0)
                val legs = routes.getJSONArray("legs").getJSONObject(0)
                val distance = legs.getInt("distance")/1000.0
                val duration = legs.getInt("duration")/60
                b.txMyLocation.setText("Lokasi saya :\nLat : $lat   Lng : $lng\n" +
                        "Destinasi : $place_name\nLat : $destLat   Lng : $destLng\n" +
                        "Jarak : $distance km   Durasi : $duration menit")
                val geometry = routes.getJSONObject("geometry")
                val coordinates = geometry.getJSONArray("coordinates")
                var arraySteps = ArrayList<LatLng>()
                for (i in 0..coordinates.length()-1){
                    val lngLat = coordinates.getJSONArray(i)
                    val fLng = lngLat.getDouble(0)
                    val fLat = lngLat.getDouble(1)
                    arraySteps.add(LatLng(fLat,fLng))
                }
                drawRoutes(arraySteps,place_name)
            },
            Response.ErrorListener {
                Toast.makeText(this, "something is wrong! ${it.message.toString()}",
                    Toast.LENGTH_SHORT).show()
            })
        val q = Volley.newRequestQueue(this)
        q.add(request)
    }

    fun drawRoutes(array : ArrayList<LatLng>,place_name: String){
        gMap?.clear()
        val polyline = PolylineOptions().color(Color.BLUE).width(10.0f)
            .clickable(true).addAll(array)
        gMap?.addPolyline(polyline)
        val ll = LatLng(lat,lng)
        val drawable = resources.getDrawable(R.drawable.location_marker) // Mendapatkan Drawable
        val markerIcon = drawableToBitmapDescriptor(drawable) // Konversi Drawable menjadi Bitmap
        gMap?.addMarker(MarkerOptions().position(ll).title("Hei! I'm here").icon(markerIcon))
        gMap?.addMarker(MarkerOptions().position(array.get(array.size-1)).title(place_name))
        gMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 12f))
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        if (gMap != null){
            airLoc = AirLocation(this,true,true,
                object : AirLocation.Callbacks{
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                        Toast.makeText(this@PengirimanDetailActivity, "Failed to get current location",
                            Toast.LENGTH_SHORT).show()
                        b.txMyLocation.setText("Failed to get current location")
                    }

                    override fun onSuccess(location: Location) {
                        var paket : Bundle? = intent.extras
                        var alm = paket?.getString("alm").toString()
                        val drawable = resources.getDrawable(R.drawable.location_marker) // Mendapatkan Drawable
                        val markerIcon = drawableToBitmapDescriptor(drawable) // Konversi Drawable menjadi Bitmap
                        lat = location.latitude; lng = location.longitude
                        val ll = LatLng(location.latitude, location.longitude)
                        gMap?.addMarker(MarkerOptions().position(ll).title("Hei! I'm here").icon(markerIcon))
                        gMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 12f))
                        getDestinationLocation("https://api.mapbox.com/geocoding/v5/mapbox.places/\" + \"$alm.json?proximity=$lng,$lat&access_token=$MAPBOX_TOKEN&limit=1")
                        b.txMyLocation.setText("Lokasi saya : LAT=${location.latitude}," +
                                "LNG=${location.longitude}")
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLoc?.onRequestPermissionsResult(requestCode,
            permissions,grantResults)
        super.onRequestPermissionsResult(requestCode,
            permissions, grantResults)
    }

    private fun drawableToBitmapDescriptor(drawable: Drawable): BitmapDescriptor {
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun detail(kd: String) {
        pVM.detail(kd).observe(this, Observer { detail ->
            b.detailNama.setText(detail.nama)

            val status_bayar = detail.no_hp
            if (status_bayar.equals("Selesai")) {
                b.detailBarang.setText(detail.nama_barang+" (Lunas)")
            } else {
                b.detailBarang.setText(detail.nama_barang+" (Belum Bayar)")
            }
            b.detailAlamat.setText(detail.alamat)
            b.detailTglKirim.setText(detail.tglkirim_jadwal)

            val tglS = detail.tglsampai_jadwal
            if (tglS.equals("null")) {
                b.detailTglSampai.setText("Proses pengiriman oleh Admin!")
            } else {
                b.detailTglSampai.setText(detail.tglsampai_jadwal)
                imgUrl = detail.bukti_kirim
            }

            tglSelesai = detail.tglsampai_jadwal
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}