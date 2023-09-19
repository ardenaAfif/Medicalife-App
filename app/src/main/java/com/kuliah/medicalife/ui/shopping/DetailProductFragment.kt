package com.kuliah.medicalife.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Cart
import com.kuliah.medicalife.databinding.FragmentDetailProductBinding
import com.kuliah.medicalife.utils.PriceFormatter
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.utils.hideBottomNavigationView
import com.kuliah.medicalife.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private val args by navArgs<DetailProductFragmentArgs>()
    private lateinit var binding: FragmentDetailProductBinding
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        // Inflate the layout for this fragment
        binding = FragmentDetailProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.apply {
            if (product.images.isNotEmpty()) {
                Glide.with(this@DetailProductFragment)
                    .load(product.images[0])
                    .into(productImage)
            } else {
                productImage.setImageResource(R.drawable.no_picture_taking)
            }

            tvNamaObat.text = product.name
            tvPrice.text = PriceFormatter.formatPrice(product.price)
            tvRules.text = product.rules
            tvDescription.text = product.description

            btnAddCart.setOnClickListener {
                viewModel.addUpdateProductInCart(Cart(product, 1))
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        binding.btnAddCart.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnAddCart.revertAnimation()
                        Toast.makeText(requireContext(), "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.btnAddCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        backButton()

    }

    private fun backButton() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}