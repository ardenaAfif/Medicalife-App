package com.kuliah.medicalife.ui.shopping

import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kuliah.medicalife.R
import com.kuliah.medicalife.adapter.AddressAdapter
import com.kuliah.medicalife.adapter.BillingProductsAdapter
import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.data.Cart
import com.kuliah.medicalife.data.order.Order
import com.kuliah.medicalife.data.order.OrderStatus
import com.kuliah.medicalife.databinding.FragmentBillingBinding
import com.kuliah.medicalife.utils.HorizontalItemDecoration
import com.kuliah.medicalife.utils.PriceFormatter
import com.kuliah.medicalife.utils.Resource
import com.kuliah.medicalife.utils.hideBottomNavigationView
import com.kuliah.medicalife.viewmodel.BillingViewModel
import com.kuliah.medicalife.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment: Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<Cart>()
    private var totalPrice = 0

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        hideBottomNavigationView()
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        setupAddressRv()
        setupBillingProductsRv()

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "Pesanan mu berhasil diproses", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        billingProductsAdapter.differ.submitList(products)

        binding.tvTotalPrice.text = PriceFormatter.formatPrice(totalPrice)

        addressAdapter.onClick = {
            selectedAddress = it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Silahkan pilih alamat dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Konfirmasi Pesanan")
            setMessage("Apakan Anda yakin untuk membeli barang di keranjang?")
            setNegativeButton("Tidak") {dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yakin") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.tvToolbarName.text = "Pembayaran"
        }
    }
}