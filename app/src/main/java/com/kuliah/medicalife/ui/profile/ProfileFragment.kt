package com.kuliah.medicalife.ui.profile

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kuliah.medicalife.R
import com.kuliah.medicalife.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionOrder()
        getVersion()
    }


    private fun actionOrder() {
        binding.apply {
            historyOrder.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
            }
        }
    }

    //    override fun onResume() {
//        super.onResume()
//        showBottomNavigationView()
//    }
    private fun getVersion() {
        val manager = requireActivity().packageManager
        val info = manager.getPackageInfo(requireActivity().packageName, PackageManager.GET_ACTIVITIES)
        binding.appVersion.text = "Versi ${info.versionName.toString()}"
    }
}