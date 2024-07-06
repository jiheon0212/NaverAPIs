package com.example.maptester

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maptester.databinding.FragmentMapBinding
import com.example.maptester.encyclopedia.EncyResponse
import com.example.maptester.encyclopedia.EncyResultService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var fragmentMapBinding: FragmentMapBinding
    private lateinit var naverMap: NaverMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.INTERNET
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMapBinding = FragmentMapBinding.inflate(layoutInflater, container, false)
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initializeMap()
        }
        return fragmentMapBinding.root
    }
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMapBinding.inputLayout.setEndIconOnClickListener {
            val word = fragmentMapBinding.inputText.text.toString()
            if (word != "") {
                encyCatchword(word)
                fragmentMapBinding.inputText.setText("")
            } else {
                Toast.makeText(context, "sending word is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentMapBinding.button.setOnClickListener {
            locationControl()
        }
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        naverMap.locationSource = fusedLocationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    private fun initializeMap() {
        val fm = parentFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map_container) as MapFragment? ?: MapFragment.newInstance().also {
            fm.beginTransaction().add(R.id.naver_map_container, it).commit()
        }
        mapFragment.getMapAsync(this)
        fusedLocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    private fun locationControl() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val lng = location.longitude
            val lat = location.latitude

            Log.d("11", "현재 경위도: x: $lng, y: $lat")
            search()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatLocation(number: Int): Double {
        val result = number.toDouble() / 10000000.0
        return result
    }

    private fun search() {
        val search = NaverService.naverInterface.getPlaceInformation(
            clientId = NaverInformation.CLIENT_ID,
            clientSecret = NaverInformation.CLIENT_SECRET,
            query = "죽전동편의점"
        )
        search.enqueue(object: retrofit2.Callback<PlaceResponse> {
            override fun onResponse(p0: Call<PlaceResponse>, p1: Response<PlaceResponse>) {
                if (p1.isSuccessful) {
                    if (p1.body()?.items != null) {
                        p1.body()?.items?.forEach {
                            val latLng = "x: ${formatLocation(it.mapx)}, y: ${formatLocation(it.mapy)}\n"
                            fragmentMapBinding.textView.append(latLng)
                        }
                    }
                    else {
                        Toast.makeText(context, "body is null", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(p0: Call<PlaceResponse>, p1: Throwable) {
                Toast.makeText(context, "$p1", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun encyCatchword(word: String) {
        val ency = EncyResultService.encyInterface.getResult(
            clientId = NaverInformation.CLIENT_ID,
            clientSecret = NaverInformation.CLIENT_SECRET,
            query = word
        )
        ency.enqueue(object: retrofit2.Callback<EncyResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<EncyResponse>, p1: Response<EncyResponse>) {
                if (p1.isSuccessful) {
                    if (p1.body()?.items != null) {
                        p1.body()?.items?.forEach { itemSame ->
                            // html태그를 전부 제거하고 title, description을 받아온다.
                            val result = itemSame.trimResults
                            // 사용자가 입력한 단어 이외의 title에 넘어온 다른 데이터를 제거하며 일치하는 문구가 없을 경우에는 빈칸으로 변경한다
                            val isMatch = if (result.first.contains(word)) word else ""

                            // 빈칸이 호출되면 패배 이외에는 화면에 toast를 띄우며 진행한다.
                            if (isMatch != "") {
                                fragmentMapBinding.textView.text = "$isMatch:\n${result.second}"
                            } else {
                                Toast.makeText(context, "you lose $word doesn't exists", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<EncyResponse>, p1: Throwable) {
                Toast.makeText(context, "$p1", Toast.LENGTH_SHORT).show()
            }

        })

    }
}