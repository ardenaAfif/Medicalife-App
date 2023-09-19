package com.kuliah.medicalife.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                buttonAddress.text = "${address.asrama} ${address.kamar}"
                if (isSelected) {
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.green800))
                    buttonAddress.setTextColor(Color.WHITE)
                } else {
                    val colorText = ContextCompat.getColor(itemView.context, R.color.green800)
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.white))
                    buttonAddress.setTextColor(colorText)
                }
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.fullName == newItem.fullName && oldItem.asrama == newItem.asrama
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    init {
        differ.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    var onClick: ((Address) -> Unit)? = null
}