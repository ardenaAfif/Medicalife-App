package com.kuliah.medicalife.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Cart
import com.kuliah.medicalife.data.Product
import com.kuliah.medicalife.databinding.CartProductItemBinding
import com.kuliah.medicalife.databinding.ProductRvItemBinding
import com.kuliah.medicalife.ui.home.HomeFragmentDirections
import com.kuliah.medicalife.utils.PriceFormatter

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {

    inner class CartProductsViewHolder(val binding: CartProductItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: Cart) {
            binding.apply {
                if (cartProduct.product.images.isNotEmpty()) {
                    Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                } else {
                    imageCartProduct.setImageResource(R.drawable.no_picture_taking)
                }
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()
                tvProductCartPrice.text = PriceFormatter.formatPrice(cartProduct.product.price)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductAdapter.CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CartProductAdapter.CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
//            val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(product)
//            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.imagePlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }

        holder.binding.imageMin.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onProductClick: ((Cart) -> Unit)? = null
    var onPlusClick: ((Cart) -> Unit)? = null
    var onMinusClick: ((Cart) -> Unit)? = null

}