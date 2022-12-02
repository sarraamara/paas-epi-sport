package com.namelesscloudco.coachnotify


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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

        button.setOnClickListener {
            try {
                    button?.isEnabled = false
                    button2?.isEnabled = true
                    val idCoach = editText.text.toString()
                    notifyPresence(idCoach)
                    intent.putExtra("idCoach", idCoach)
                    startForegroundService(intent)
                    System.out.println("heey")
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()


            } catch (e:java.lang.Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
            }
        }
        button2.setOnClickListener {
            button?.isEnabled = true
            button2?.isEnabled = false
            val idCoach = editText.text.toString();
            try {
                stopService(intent)
                notifyAbsence(idCoach)
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
            } catch (e:java.lang.Exception) {
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun notifyAbsence(idCoach: String) {
        Log.d("notify", "Notify absence coach : $idCoach")
        val request: retrofit2.Call<String> = RetrofitHelper.getInstance().create(GetAPI::class.java).delete(idCoach)
        request.execute().body()?.let { Log.i("notify", it) };
    }

    private fun notifyPresence(idCoach: String) {
        Log.d("notify", "Notify presence coach : $idCoach")
       val request: retrofit2.Call<String> = RetrofitHelper.getInstance().create(GetAPI::class.java).notify(idCoach)
        request.execute().body()?.let { Log.i("notify", it) };

    }



}