package com.zaid.midweektaskcalllog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit  var name : String
    private val requestReadLog =2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        PermissionManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
//                Add the permission fro the sending and receiving sms  (PART 2)
                this, arrayOf(Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS), requestReadLog)
        }else{
            loadData()
        }
    }





    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestReadLog) loadData()
    }

    private fun loadData() {
        val list = getCallsDetails(this)
        val adapter = ListAdapter(this, list)
        list_view.adapter = adapter
    }

    private fun getCallsDetails(context: Context): ArrayList<CallDetials> {
        val callDetials = ArrayList<CallDetials>()
        val contactUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contactUri, null, null, null, null)
            val nameUri = cursor?.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
            val duration = cursor?.getColumnIndex(CallLog.Calls.DURATION)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val phoneNumber = number?.let { cursor.getString(it) }
                        val callerNameUri = nameUri?.let { cursor.getString(it) }
                        val callDuration = duration?.let { cursor.getString(it) }
                        callDetials.add(
                            CallDetials(
                                getCallerName(callerNameUri).toString(),
                                phoneNumber.toString(), callDuration.toString()
                            )
                        )
                    } while (cursor.moveToNext())
                }
                if (cursor != null) {
                    cursor.close()
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "USER DENIED PERMISSION", Toast.LENGTH_SHORT).show()
        }
        return callDetials
    }




//    @SuppressLint("Range")
    @SuppressLint("Range")
    private fun getCallerName(callerNameUri: String?): Any {
            return if (callerNameUri != null){
                val cursor = contentResolver.query(Uri.parse(callerNameUri),null,null,null,null)
                name = "Unknown"

//                Unknwon by default passed  not working down part

                if ((cursor?.count ?: 0) > 0){
                    while (cursor != null && cursor.moveToNext()){

                      name = cursor.getString( cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                    }

                }
                cursor?.close()
                return name
            }else{
                "Not an contact"
            }
    }
}
