package com.example.khata.pojo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.khata.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFrag : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pop_billing, container, false)
}