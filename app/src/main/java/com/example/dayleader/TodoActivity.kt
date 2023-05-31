package com.example.dayleader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dayleader.databinding.ActivityTodoBinding

class TodoActivity:AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}