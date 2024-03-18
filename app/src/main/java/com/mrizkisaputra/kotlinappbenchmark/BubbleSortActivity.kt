package com.mrizkisaputra.kotlinappbenchmark

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrizkisaputra.kotlinappbenchmark.databinding.ActivityBubbleSortBinding
import org.json.JSONArray
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class BubbleSortActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBubbleSortBinding
    // membuat background thread untuk memproses sorting, supaya tidak terjadi application not responding
    private val executor: ExecutorService = Executors.newFixedThreadPool(2)
    private val handler: Handler = Handler(Looper.getMainLooper())
    private fun readJsonFromAsset(): JSONArray {
        // membaca file json
        val inputStream: InputStream = assets.open("dummy_data_bubblesort.json")
        val json: String = inputStream.bufferedReader().use { it.readText() }
        return JSONArray(json)
    }

    private fun bubbleShort(data: JSONArray): JSONArray {
        Log.i("SEBELUM DI SORTING", "$data")
        val n = data.length()
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                val jsonObject1 = data.getJSONObject(j)
                val jsonObject2 = data.getJSONObject(j + 1)
                if (jsonObject1.getInt("nomorAntrian") > jsonObject2.getInt("nomorAntrian")) {
                    // Menukar elemen secara asc
                    data.put(j, jsonObject2)
                    data.put(j + 1, jsonObject1)
                }
            }
        }
        return data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBubbleSortBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val executionTime: MutableList<String> = mutableListOf()
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1)
        binding.listview.adapter = arrayAdapter

        val data = readJsonFromAsset()
        binding.startBenchmark.setOnClickListener {
            executor.execute {
                val numberOfIterationTest = binding.numberOfIterationTest.text.toString()
                if (numberOfIterationTest.isNotEmpty()) {
                    var totalTimeExecution: Long = 0
                    for (i in 1..numberOfIterationTest.toInt()) {
                        if (i == 1) {
                            handler.post {
                                binding.runningOfCountIteration.text = "0 of ${numberOfIterationTest.toInt()} iterasi pengujian"
                                binding.status.text = getString(R.string.loading, 0)
                            }
                        }
                        val measureTimeMillis = measureTimeMillis { // lakukan pengurutan data
                            val sorted = bubbleShort(data)
                            Log.i("HASIL SORTING", "${sorted}")
                        }
                        totalTimeExecution += measureTimeMillis
                        handler.post { // menampilkan ukuran waktu runtime
                            arrayAdapter.clear()
                            arrayAdapter.add("Waktu eksekusi iterasi pengujian $i = $measureTimeMillis ms")
                        }
                        val progressPercent = i * 100 / numberOfIterationTest.toInt() // kode untuk menghitung persentase loading
                        handler.post {
                            binding.runningOfCountIteration.text = "$i of ${numberOfIterationTest.toInt()} iterasi pengujian" // tampilkan jumlah iterasi
                            if (progressPercent == 100) { // tampilkan progress loading saat benchmark berlangsung
                                binding.status.text = getString(R.string.task_completed)
                            } else {
                                binding.status.text = getString(R.string.loading, progressPercent)
                            }
                        }
                    }
                    handler.post {
                        binding.totalExecutionTime.visibility = View.VISIBLE
                        binding.totalExecutionTime.text = "Total waktu eksekusi : $totalTimeExecution ms"
                    }
                } else {
                    handler.post {
                        binding.numberOfIterationTest.error = "field tidak boleh kosong"
                    }
                }
            }
        }
    }
}