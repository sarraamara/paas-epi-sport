package com.namelesscloudco.coachnotify

import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var editText:EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        button.setOnClickListener {
            val name = editText.text
            try {
                notifyPresence(name.toString())
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
            } catch (e:java.lang.Exception) {
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun notifyPresence(name: String) {
        Log.d("notify", "Notify presence coach : $name")
       val request: retrofit2.Call<String> = RetrofitHelper.getInstance().create(GetAPI::class.java).notify(name)
        request.execute()
    }
}