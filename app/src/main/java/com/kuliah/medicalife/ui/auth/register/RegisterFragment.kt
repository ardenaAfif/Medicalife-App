package com.kuliah.medicalife.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.User
import com.kuliah.medicalife.databinding.FragmentRegisterBinding
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
    ): View {
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
                if (user != null && password.isNotEmpty()) {
                    viewModel.createAccountWithEmailAndPassword(user, password)
                } else {
                    Toast.makeText(requireContext(), "Silahkan isi terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
            btnGoogle.setOnClickListener {
                Toast.makeText(requireContext(), "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
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
        playAnimation()
    }

    private fun playAnimation() {
        val judul = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.loginDesc, View.ALPHA, 1f).setDuration(300)
        val email =
            ObjectAnimator.ofFloat(binding.textInputLayoutEmail, View.ALPHA, 1f).setDuration(300)
        val password =
            ObjectAnimator.ofFloat(binding.textInputLayoutPassword, View.ALPHA, 1f).setDuration(300)
        val name =
            ObjectAnimator.ofFloat(binding.textInputLayoutName, View.ALPHA, 1f).setDuration(300)
        val btnMulai = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(300)
        val masukDengan = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val btnGoogle = ObjectAnimator.ofFloat(binding.btnGoogle, View.ALPHA, 1f).setDuration(300)
        val haveAccount =
            ObjectAnimator.ofFloat(binding.linearHaveAccount, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                judul,
                desc,
                name,
                email,
                password,
                btnMulai,
                masukDengan,
                btnGoogle,
                haveAccount
            )
            start()
        }
    }


    companion object {
        const val TAG = "RegisterFragment"
    }

}