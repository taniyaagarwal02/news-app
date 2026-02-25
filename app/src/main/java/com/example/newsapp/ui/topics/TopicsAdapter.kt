package com.example.newsapp.ui.topics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.google.android.material.card.MaterialCardView

data class TopicItem(
    val name: String,
    var selected: Boolean = false
)

class TopicsAdapter(
    private val topics: List<TopicItem>,
    private val onSelectionChanged: (List<TopicItem>) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView as MaterialCardView
        val name: TextView = itemView.findViewById(R.id.topicName)
        val check: ImageView = itemView.findViewById(R.id.topicCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = topics[position]
        holder.name.text = item.name

        updateVisualState(holder, item.selected)

        holder.itemView.setOnClickListener {
            item.selected = !item.selected
            updateVisualState(holder, item.selected)
            onSelectionChanged(topics)
        }
    }

    private fun updateVisualState(holder: TopicViewHolder, isSelected: Boolean) {
        val context = holder.itemView.context
        val card = holder.card

        val strokeColor = if (isSelected) {
            context.getColor(R.color.accent_blue)
        } else {
            context.getColor(R.color.dot_inactive)
        }
        card.strokeColor = strokeColor

        holder.check.visibility = if (isSelected) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = topics.size
}

