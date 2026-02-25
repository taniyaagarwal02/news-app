package com.example.newsapp.ui.home


import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.utils.SavedArticlesManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class NewsAdapter(
    private val articles: List<Article>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.newsTitle)
        val source: TextView = itemView.findViewById(R.id.newsSource)
        val image: ImageView = itemView.findViewById(R.id.newsImage)
        val loader: ProgressBar = itemView.findViewById(R.id.imageLoader)
        val bookmarkIcon: ImageView = itemView.findViewById(R.id.bookmarkIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val article = articles[position]

        holder.title.text = article.title
        holder.source.text = article.source?.name ?: "Unknown"

        // Update bookmark icon state
        val isSaved = SavedArticlesManager.isArticleSaved(holder.itemView.context, article.url)
        holder.bookmarkIcon.setImageResource(
            if (isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
        )
        holder.bookmarkIcon.setColorFilter(
            holder.itemView.context.getColor(R.color.accent_blue)
        )

        // Bookmark click handler
        holder.bookmarkIcon.setOnClickListener {
            val wasSaved = SavedArticlesManager.isArticleSaved(holder.itemView.context, article.url)
            if (wasSaved) {
                SavedArticlesManager.removeArticle(holder.itemView.context, article)
                holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_outline)
                Toast.makeText(holder.itemView.context, "Removed from saved", Toast.LENGTH_SHORT).show()
            } else {
                SavedArticlesManager.saveArticle(holder.itemView.context, article)
                holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_filled)
                Toast.makeText(holder.itemView.context, "Saved article", Toast.LENGTH_SHORT).show()
            }
        }

        holder.loader.visibility = View.VISIBLE
        Glide.with(holder.itemView.context)
            .load(article.image)
            .placeholder(R.color.purple_50)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.image_error)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.loader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.loader.visibility = View.GONE
                    return false
                }
            })
            .into(holder.image)



        holder.itemView.setOnClickListener {
            val url = article.url
            if (!url.isNullOrBlank()) {
                val urlToOpen = if (url.startsWith("http://") || url.startsWith("https://")) {
                    url.trim()
                } else {
                    "https://${url.trim()}"
                }
                val intent = Intent(holder.itemView.context, com.example.newsapp.ui.article.ArticleActivity::class.java)
                intent.putExtra(com.example.newsapp.ui.article.ArticleActivity.EXTRA_URL, urlToOpen)
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "Article URL not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    }

    override fun getItemCount(): Int = articles.size



}
