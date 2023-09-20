package com.kuliah.medicalife.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kuliah.medicalife.R
import com.kuliah.medicalife.databinding.FragmentLoginBinding
import com.kuliah.medicalife.ui.dialog.setupBottomSheetDialog
import com.kuliah.medicalife.ui.home.HomeActivity
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password)
                } else {
                    Toast.makeText(requireContext(), "Harap isi email dan paswword dahulu", Toast.LENGTH_SHORT).show()
                }
            }

            btnGoogle.setOnClickListener {
                Toast.makeText(requireContext(), "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
            }

            tvForgotPassword.setOnClickListener {
                setupBottomSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
            }

            binding.dontHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Link reset password telah dikirim ke email Anda",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.btnLogin.revertAnimation()
                        Intent(requireActivity(), HomeActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Email atau Password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.btnLogin.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        playAnimation()
    }

    private fun playAnimation() {
        val judul = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.loginDesc, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.textInputLayoutEmail, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.textInputLayoutPassword, View.ALPHA, 1f).setDuration(500)
        val forgotPassword =
            ObjectAnimator.ofFloat(binding.tvForgotPassword, View.ALPHA, 1f).setDuration(500)
        val btnMulai = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val masukDengan = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val btnGoogle = ObjectAnimator.ofFloat(binding.btnGoogle, View.ALPHA, 1f).setDuration(500)
        val dontHaveAccount =
            ObjectAnimator.ofFloat(binding.linearBelumPunyaAkun, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                judul,
                desc,
                email,
                password,
                forgotPassword,
                btnMulai,
                masukDengan,
                btnGoogle,
                dontHaveAccount
            )
            start()
        }
    }

}