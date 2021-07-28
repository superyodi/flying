package com.yodi.flying.features.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yodi.flying.databinding.FragmentPreferenceBinding

class PreferenceFragment : Fragment() {

    private lateinit var binding: FragmentPreferenceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferenceBinding.inflate(inflater, container, false)


        return binding.root
    }
}

