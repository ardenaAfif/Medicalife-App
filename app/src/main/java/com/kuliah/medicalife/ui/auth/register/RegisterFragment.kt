package com.kuliah.medicalife.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.User
import com.kuliah.medicalife.databinding.FragmentRegisterBinding
import com.kuliah.medicalife.ui.auth.login.LoginFragment
import com.kuliah.medicalife.utils.RegisterValidation
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignup.setOnClickListener {
                val user = User(
                    edtName.text.toString().trim(),
                    edtEmail.text.toString().trim()
                )
                val password = edtPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }

            binding.haveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.register.collect{
                when(it) {
                    is Resource.Loading -> {
                        binding.btnSignup.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.btnSignup.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.btnSignup.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.empty is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.empty.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.name is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.name.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.email.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.password.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    companion object {
        const val TAG = "RegisterFragment"
    }

}