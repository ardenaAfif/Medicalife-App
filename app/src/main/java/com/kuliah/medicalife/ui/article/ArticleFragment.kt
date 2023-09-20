package com.kuliah.medicalife.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuliah.medicalife.R
import com.kuliah.medicalife.adapter.ArticleAdapter
import com.kuliah.medicalife.data.Article
import com.kuliah.medicalife.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleAdapter
    private lateinit var rvArticel: RecyclerView
    private lateinit var articleArrayList: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArticleRv()

        customToolbar()
    }

    private fun setupArticleRv() {
        rvArticel = binding.rvArticle
        listArticle()
        val layoutManager = LinearLayoutManager(context)
        rvArticel.layoutManager = layoutManager
        rvArticel.setHasFixedSize(true)
        adapter = ArticleAdapter(articleArrayList)
        rvArticel.adapter = adapter
    }

    private fun listArticle(): ArrayList<Article> {
        articleArrayList = ArrayList()

        val dataName = resources.getStringArray(R.array.name_artikel)
        val dataDesc = resources.getStringArray(R.array.desk_artikel)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        val listArticel = ArrayList<Article>()
        for (i in dataName.indices) {
            val artikel = Article(dataName[i], dataDesc[i], dataPhoto.getResourceId(i, -1))
            listArticel.add(artikel)
        }

        articleArrayList.addAll(listArticel)
        return articleArrayList
    }

    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.visibility = View.INVISIBLE
            toolbar.tvToolbarName.text = "Artikel"
        }
    }
}