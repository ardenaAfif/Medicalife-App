package com.kuliah.medicalife.ui.address

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.databinding.FragmentAddressBinding
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : AppCompatActivity() {

    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentAddressBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            buttonSave.setOnClickListener {
                val fullName = edFullName.text.toString()
                val asrama = edAsrama.text.toString()
                val room = edRoom.text.toString()
                val phone = edPhone.text.toString()
                val address = Address(fullName, asrama, room, phone)

                viewModel.addAddress(address)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(this@AddressFragment, "Berhasil menambahkan alamat", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@AddressFragment, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(this@AddressFragment, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}