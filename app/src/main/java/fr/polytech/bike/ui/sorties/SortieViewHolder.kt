package fr.polytech.bike.ui.sorties

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import fr.polytech.bike.R

class SortieViewHolder(itemView: View): ViewHolder(itemView) {
    val lieuSortie: TextView = itemView.findViewById(R.id.tv_item_lieusortie)
    val dateSortie: TextView = itemView.findViewById(R.id.tv_item_datesortie)
    val distanceSortie: TextView = itemView.findViewById(R.id.tv_item_kmsortie)

}