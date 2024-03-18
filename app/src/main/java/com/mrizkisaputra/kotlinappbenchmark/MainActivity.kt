package com.mrizkisaputra.kotlinappbenchmark

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrizkisaputra.kotlinappbenchmark.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            benchmarkBubbleSort.setOnClickListener(::onClick)
            benchmarkSelectionSort.setOnClickListener(::onClick)
            benchmarkMergeSort.setOnClickListener(::onClick)
            benchmarkQuickSort.setOnClickListener(::onClick)
            benchmarkRecursiveFibonacci.setOnClickListener(::onClick)
        }
    }

    private fun onClick(v: View) {
        when (v.id) {
            R.id.benchmarkBubbleSort -> {
                Intent(this, BubbleSortActivity::class.java).also { startActivity(it) }
            }
            R.id.benchmarkSelectionSort -> {  }
            R.id.benchmarkMergeSort -> {  }
            R.id.benchmarkQuickSort -> {  }
            R.id.benchmarkRecursiveFibonacci -> {  }
        }
    }


}