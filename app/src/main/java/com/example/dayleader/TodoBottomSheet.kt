package com.example.dayleader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dayleader.databinding.BottomSheetDialogTodoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoBottomSheet() : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = BottomSheetDialogTodoBinding.inflate(inflater, container, false)
        return binding.root
    }
}