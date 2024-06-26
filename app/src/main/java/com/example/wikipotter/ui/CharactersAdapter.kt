package com.example.wikipotter.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikipotter.R
import com.example.wikipotter.model.Characters

class CharactersAdapter(private val email: String): RecyclerView.Adapter<CharacterViewHolder>() {
    var items : MutableList<Characters> = ArrayList<Characters>()
    private var filteredItems: MutableList<Characters> = ArrayList()
    private var originalItems: MutableList<Characters> = ArrayList()


    init {
        filteredItems = items
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_item, parent, false)
        if (originalItems.isEmpty()){originalItems = items}
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val charact = items[position]
        holder.name.text = charact.name
        Glide.with(holder.itemView.context).load(charact.image).into(holder.image)

        holder.itemView.setOnClickListener{
            val id = charact.id
            val intent = Intent(holder.itemView.context, CharacterActivity::class.java)

            intent.putExtra("id", id)
            intent.putExtra("email",email)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun update (lista: MutableList<Characters>){
        items = lista
        this.notifyDataSetChanged()
    }


    // Puedo sacar la funcion restoreOriginalList, agregarle a la funcion filter if (query.isNullorEmpty)
    // y acomodar el MainActivy para que funcione solo con la funcion filter
    fun filter(query: String) {
        items = originalItems
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
        update(filteredItems)
    }

    fun restoreOriginalList() {
        filteredItems.clear()
        update(originalItems)
        notifyDataSetChanged()
    }
}
