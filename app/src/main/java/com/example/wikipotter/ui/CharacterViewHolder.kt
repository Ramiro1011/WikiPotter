package com.example.wikipotter.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipotter.R

class CharacterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.txtName)
    val image: ImageView = itemView.findViewById(R.id.imgVItm)
}