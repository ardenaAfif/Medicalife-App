package com.kuliah.medicalife.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kuliah.medicalife.R
import com.kuliah.medicalife.adapter.ProductAdapter
import com.kuliah.medicalife.databinding.FragmentHomeBinding
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.viewmodel.DetailsViewModel
import com.kuliah.medicalife.viewmodel.MainProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productsAdapter: ProductAdapter
    private val mainProductViewModel by viewModels<MainProductViewModel>()
    private val detailViewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter = ProductAdapter(detailViewModel)

        setupProductsRv()
        actionProfile()

        lifecycleScope.launchWhenStarted {
            mainProductViewModel.mainProduct.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        productsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        // Inisialisasi SearchView
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan query
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan teks yang diubah
                performSearch(newText)
                return true
            }

        })
    }

    private fun actionProfile() {
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun performSearch(query: String?) {
        // Filter produk berdasarkan nama
        val filteredProducts = if (!query.isNullOrBlank()) {
            mainProductViewModel.mainProduct.value.data?.filter { product ->
                product.name.contains(query, ignoreCase = true)
            } ?: emptyList()
        } else {
            // Jika query kosong, tampilkan semua produk
            mainProductViewModel.mainProduct.value.data ?: emptyList()
        }

        if (filteredProducts.isEmpty()) {
            binding.tvNoResults.visibility = View.VISIBLE
            binding.tvNoResults.text = getString(R.string.no_results)
        } else {
            binding.tvNoResults.visibility = View.GONE
        }

        // Perbarui daftar produk yang ditampilkan dalam RecyclerView
        productsAdapter.differ.submitList(filteredProducts)
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setupProductsRv() {
        productsAdapter = ProductAdapter(detailViewModel)
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL,  false)
            adapter = productsAdapter
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }


}