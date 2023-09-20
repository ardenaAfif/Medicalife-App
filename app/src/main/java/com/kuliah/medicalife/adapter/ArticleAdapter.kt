package com.kuliah.medicalife.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Article
import com.kuliah.medicalife.ui.article.DetailArticleActivity

class ArticleAdapter (private val listArticle: ArrayList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: Article)
    }
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameArtikel)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDescArtikel)
        val imgArtikel: ImageView = itemView.findViewById(R.id.imgArtikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.artikel_list_row, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, image) = listArticle[position]

        holder.imgArtikel.setImageResource(image)
        holder.tvName.text = name
        holder.tvDesc.text = description

        val currentArticle = listArticle[position]
        holder.itemView.setOnClickListener {
            val intentToDetails = Intent(holder.itemView.context, DetailArticleActivity::class.java)
            intentToDetails.putExtra(DetailArticleActivity.EXTRA_ARTICLE, currentArticle)
            holder.itemView.context.startActivity(intentToDetails)
        }
    }
}