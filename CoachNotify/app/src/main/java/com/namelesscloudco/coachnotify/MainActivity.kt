package com.namelesscloudco.coachnotify


import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var editText:EditText
    lateinit var button: Button
    lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button2.isEnabled = false
        editText = findViewById(R.id.editText)
        val intent = Intent(this, NotificationService::class.java)
        val name = editText.text

        button.setOnClickListener {
            try {
                    button?.isEnabled = false
                    button2?.isEnabled = true
                    val idCoach = editText.text.toString()
                    notifyPresence(name)
                    intent.putExtra("idCoach", idCoach)
                    startForegroundService(intent)
                    System.out.println("heey")
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()


            } catch (e:java.lang.Exception) {
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                Log.i("error",e.toString())
            }
        }
        button2.setOnClickListener {
            button?.isEnabled = true
            button2?.isEnabled = false
            val idCoach = editText.text.toString();
            try {
                stopService(intent)
                notifyAbsence(name)
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
            } catch (e:java.lang.Exception) {
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                Log.i("error",e.toString())
            }
        }
    }

    private fun notifyAbsence(name: String) {
        val request: Call<String> = RetrofitHelper.getInstance().create(GetAPI::class.java).delete(name)
        Log.i("request", request.request().url().toString() + ", REQUEST TYPE : " + request.request().method())
//        request.execute().body()?.let { Log.i("notify", it) };
        request.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.i("success",response.body().toString())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.i("error",t.toString())
            }
        })
    }

    private fun notifyPresence(name: String) {
       val request: Call<String> = RetrofitHelper.getInstance().create(GetAPI::class.java).notify(name)
        Log.i("request", request.request().url().toString()+ ", REQUEST TYPE : " + request.request().method())
//        request.execute().body()?.let { Log.i("notify", it) };
        request.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.i("success",response.body().toString())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.i("error",t.toString())
            }
        })
    }
}