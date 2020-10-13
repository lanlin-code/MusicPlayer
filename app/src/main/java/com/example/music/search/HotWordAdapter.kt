package com.example.music.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.FragmentChangeListener
import com.example.music.R
import com.example.music.entity.HotWord
import com.example.music.search.result.SearchResultFragment

class HotWordAdapter(val hotWordList: MutableList<HotWord> = mutableListOf()) : RecyclerView.Adapter<HotWordAdapter.HotWordHolder>() {
    var hint: String = ""
    var listener: FragmentChangeListener? = null

    class HotWordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.hot_list_item_iv)
        val content = itemView.findViewById<TextView>(R.id.hot_list_item_content)
        val word = itemView.findViewById<TextView>(R.id.hot_list_item_word)
        val count = itemView.findViewById<TextView>(R.id.hot_list_item_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotWordHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_hot_list_item, parent, false)
        return HotWordHolder(view)
    }

    override fun onBindViewHolder(holder: HotWordHolder, position: Int) {
        val item = hotWordList[position]
        if (item.iconUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.iconUrl).into(holder.image)
        }

        holder.content.text = item.content
        holder.count.text = item.score.toString()
        holder.word.text = item.searchWord
        holder.itemView.setOnClickListener {
            listener?.onFragmentChange(SearchResultFragment(item.searchWord, hint))
            listener = null
        }

    }

    override fun getItemCount(): Int {
        return hotWordList.size
    }

}