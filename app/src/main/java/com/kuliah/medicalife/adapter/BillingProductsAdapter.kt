package com.kuliah.medicalife.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.data.Cart
import com.kuliah.medicalife.databinding.AddressRvItemBinding
import com.kuliah.medicalife.databinding.BillingProductsRvItemBinding
import com.kuliah.medicalife.utils.PriceFormatter

class BillingProductsAdapter : RecyclerView.Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {

    inner class BillingProductsViewHolder(val binding: BillingProductsRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(billingProduct: Cart) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0])
                    .into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()
                tvProductCartPrice.text = PriceFormatter.formatPrice(billingProduct.product.price)
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillingProductsViewHolder {
        return BillingProductsViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        val billingProduct = differ.currentList[position]
        holder.bind(billingProduct)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Address) -> Unit)? = null
}