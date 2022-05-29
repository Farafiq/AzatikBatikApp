package com.azatik.batikpahlawan.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.azatik.batikpahlawan.R
import com.azatik.batikpahlawan.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private var getImg: File? = null
    private var storiesBitmap: Bitmap? = null
    private var modelMain: MutableList<ModelMain> = ArrayList()
    lateinit var mainAdapter: MainAdapter
    private lateinit var binding: ActivityMainBinding


    companion object {
        const val CAMERA_X = 200

        const val DATA_USER = "USER"

        private val PERMISSIONS_MANIFEST = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionReq()

        binding.btnCamera.setOnClickListener { startCameraX() }

        //transparent background searchview
        val searchPlateId = searchData.getContext()
            .resources.getIdentifier("android:id/search_plate", null, null)

        val searchPlate = searchData.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)
        searchData.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainAdapter.filter.filter(newText)
                return true
            }
        })

        rvListPahlawan.setLayoutManager(LinearLayoutManager(this))
        rvListPahlawan.setHasFixedSize(true)

        fabBackTop.setOnClickListener { view: View? ->
            rvListPahlawan.smoothScrollToPosition(
                0
            )
        }

        //get data json
        getListPahlawan()
    }

    private fun permissionReq() {
        if (!granted()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_MANIFEST, REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        permissionCode: Int,
        permissions: Array<String>,
        permissionResults: IntArray,
    ) {
        super.onRequestPermissionsResult(permissionCode, permissions, permissionResults)
        if (permissionCode == REQUEST_CODE) {
            if (!granted()) {
                Toast.makeText(this, getString(R.string.invalid_permission), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun granted() = PERMISSIONS_MANIFEST.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun startCameraX() {
        launchCameraX.launch(Intent(this, cameraActivity::class.java))
    }

    private val launchCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getImg = myFile
            storiesBitmap =
                Helper.rotateBitmap(
                    BitmapFactory.decodeFile(getImg?.path), isBackCamera
                )
        }
        //binding.viewImage.setImageBitmap(storiesBitmap)
    }

    private fun getListPahlawan() {
        try {
            val stream = assets.open("pahlawan_nasional.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strContent = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strContent)
                val jsonArray = jsonObject.getJSONArray("daftar_pahlawan")
                for (i in 0 until jsonArray.length()) {
                    val jsonObjectData = jsonArray.getJSONObject(i)
                    val dataApi = ModelMain()
                    dataApi.nama = jsonObjectData.getString("nama")
                    dataApi.namaLengkap = jsonObjectData.getString("nama2")
                    dataApi.kategori = jsonObjectData.getString("kategori")
                    dataApi.image = jsonObjectData.getString("img")
                    dataApi.asal = jsonObjectData.getString("asal")
                    dataApi.usia = jsonObjectData.getString("usia")
                    dataApi.lahir = jsonObjectData.getString("lahir")
                    dataApi.gugur = jsonObjectData.getString("gugur")
                    dataApi.lokasimakam = jsonObjectData.getString("lokasimakam")
                    dataApi.history = jsonObjectData.getString("history")
                    modelMain.add(dataApi)
                }
                mainAdapter = MainAdapter(this, modelMain)
                rvListPahlawan.adapter = mainAdapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException) {
            Toast.makeText(
                this@MainActivity,
                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}