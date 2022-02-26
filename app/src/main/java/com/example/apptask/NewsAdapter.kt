package com.example.apptask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter(val context : Context,val NewsArray : ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val imageView : ImageView = itemView.findViewById(R.id.imageView)
        val timeTV : TextView = itemView.findViewById(R.id.timeTV)
        val publisherTV : TextView = itemView.findViewById(R.id.publisherTV)
        val titleTV : TextView = itemView.findViewById(R.id.titleTV)
        val descriptionTV : TextView = itemView.findViewById(R.id.descriptionTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_item,null,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = NewsArray[position]
        Picasso.get().load(currentItem.urlToImage).into(holder.imageView)
        holder.timeTV.text = currentItem.publishedAt
        holder.publisherTV.text = currentItem.name
        holder.titleTV.text = currentItem.title
        holder.descriptionTV.text = currentItem.description

    }

    override fun getItemCount(): Int {
        return NewsArray.size
    }

}