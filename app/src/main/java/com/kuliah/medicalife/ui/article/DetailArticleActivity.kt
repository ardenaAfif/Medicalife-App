package com.kuliah.medicalife.ui.article

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.kuliah.medicalife.R
import com.kuliah.medicalife.data.Article
import com.kuliah.medicalife.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur tombol "up" di ActionBar

        customToolbar()
        detailArticle()
    }

    private fun detailArticle() {
        val dataArticle = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        if (dataArticle != null) {
            binding.apply {
                tvNameArtikel.text = dataArticle.judul
                tvDescArtikel.text = dataArticle.desc
                imgArtikel.setImageResource(dataArticle.image)
            }
        }
    }

    private fun customToolbar() {
        binding.apply {
            toolbar.tvToolbarName.text = "Artikel"
            toolbar.navBack.visibility = View.INVISIBLE
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "EXTRA_ARTICLE"
    }
}