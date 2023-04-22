package com.example.deviceinfoapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.displayInfo).text = getSystemDetails()

    }

    @SuppressLint("HardwareIds")
    private fun getSystemDetails() : String {
        return "Manufacture: ${Build.MANUFACTURER} \n" +
                "Model: ${Build.MODEL} \n" +
                "Brand: ${Build.BRAND} \n" +
                getRAM() +
                getBatteryInfo() +
                "Version Code: ${Build.VERSION.RELEASE}\n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                getCamerasMegaPixel() +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
//                getIMEI() +
                cpuInfo() +
                gpuInfo() +
                getCameraAperture() +
                sensorReading() +
                "\nDeviceID: ${Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)} \n" +
                "ID: ${Build.ID} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n"
    }

    private fun getIMEI() : String {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "IMEI number : ${telephonyManager.imei} \n"
        } else {
            return ""
        }
    }

    private fun getCameraAperture() : String{
        return "Camera Aperture: \n"
    }

    private fun cpuInfo() : String {
        return "CPU Info: \n"
    }

    private fun gpuInfo() : String {
        return "GPU Info: \n"
    }

    private fun sensorReading() : String {
        return "Sensor Reading: \n"
    }

    @Throws(CameraAccessException::class)
    private fun getCamerasMegaPixel(): String {
        var output: String
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = manager.cameraIdList
        var characteristics = manager.getCameraCharacteristics(cameraIds[0])
        output = "Back camera MP: " + calculateMegaPixel(
            characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)!!
                .width.toFloat(),
            characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)!!.height.toFloat()
        ) + "\n"
        characteristics = manager.getCameraCharacteristics(cameraIds[1])
        output += "Front camera MP: " + calculateMegaPixel(
            characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)!!
                .width.toFloat(),
            characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)!!
                .height.toFloat()
        ) + "\n"
        return output
    }

    private fun calculateMegaPixel(width: Float, height: Float): Int {
        return (width * height / 1024000).roundToInt()
    }

    private fun getRAM() : String{
        val actManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)

        val availMemory = round( memInfo.availMem.toDouble()/(1024*1024*1024))
        val totalMemory= round( memInfo.totalMem.toDouble()/(1024*1024*1024))

        return "Available RAM: $availMemory\nTotal RAM: $totalMemory \n"
    }

    private fun getBatteryInfo() : String {
        val batLevel = this.getSystemService(BATTERY_SERVICE) as BatteryManager
        return "Battery: ${batLevel.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)} \n"
    }

}