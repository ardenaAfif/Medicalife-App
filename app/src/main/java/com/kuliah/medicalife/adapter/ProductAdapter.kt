package com.kuliah.medicalife.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Cart
import com.kuliah.medicalife.data.Product
import com.kuliah.medicalife.databinding.ProductRvItemBinding
import com.kuliah.medicalife.ui.home.HomeFragment
import com.kuliah.medicalife.ui.home.HomeFragmentDirections
import com.kuliah.medicalife.utils.PriceFormatter
import com.kuliah.medicalife.viewmodel.DetailsViewModel

class ProductAdapter(private val viewModel: DetailsViewModel) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                if (product.images.isNotEmpty()) {
                    Glide.with(itemView).load(product.images[0]).into(imgProduct)
                } else {
                    imgProduct.setImageResource(R.drawable.no_picture_taking)
                }
                tvName.text = product.name
                tvPrice.text = PriceFormatter.formatPrice(product.price)

                // Tambahkan onClickListener untuk tombol "Tambah ke Keranjang"
                btnAddToCart.setOnClickListener {
                    val cartProduct = Cart(product, 1)
                    viewModel.addUpdateProductInCart(cartProduct)
                    Toast.makeText(itemView.context, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        return ProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(product)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onAddToCartClickListener: ((Product) -> Unit)? = null

}