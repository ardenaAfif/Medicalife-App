package com.kuliah.medicalife.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kuliah.medicalife.R
import com.kuliah.medicalife.databinding.FragmentProfileBinding
import com.kuliah.medicalife.ui.auth.AuthActivity
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

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

        customToolbar()
        actionMenu()
        getVersion()

        userProfile()
    }

    private fun userProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        binding.progressBarProfile.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarProfile.visibility = View.GONE
                        binding.profileName.text = it.data?.name
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBarProfile.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.tvToolbarName.text = "Profil"
        }
    }


    private fun actionMenu() {
        binding.apply {
            historyOrder.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
            }
            addAddress.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addressFragment)
            }
            logout.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun getVersion() {
        val manager = requireActivity().packageManager
        val info =
            manager.getPackageInfo(requireActivity().packageName, PackageManager.GET_ACTIVITIES)
        binding.appVersion.text = "Versi ${info.versionName.toString()}"
    }
}