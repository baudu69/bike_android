package fr.polytech.bike.ui.sorties

import android.content.Context
import android.view.*
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import fr.polytech.bike.R
import fr.polytech.bike.data.model.Sortie
import java.time.format.DateTimeFormatter


class SortieViewAdapter(private var sorties: List<Sortie>, private val context: Context, private val recyclerView: RecyclerView): Adapter<SortieViewHolder>() {

    private val _click: MutableLiveData<Sortie> = MutableLiveData()
    val click: LiveData<Sortie>
        get() = _click

    private var onClickListener: View.OnClickListener = View.OnClickListener {
        val itemPosition: Int = recyclerView.getChildLayoutPosition(it)
        val item: Sortie = sorties[itemPosition]
        _click.postValue(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_sortie,
            parent, false)
        itemView.setOnClickListener(onClickListener)

        return SortieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SortieViewHolder, position: Int) {
        val currentItem = sorties[position]
        holder.lieuSortie.text = currentItem.lieuDepart
        holder.dateSortie.text = currentItem.dateSortie.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " à " + currentItem.heureDepart.format(DateTimeFormatter.ofPattern("HH:mm"))
        holder.distanceSortie.text = currentItem.distanceParcourue.toString() + " km"
    }

    fun setSorties(sorties: List<Sortie>) {
        this.sorties = sorties
        notifyDataSetChanged()
    }


    override fun getItemCount() = sorties.size
}

//class SortieRecyclerClickListener(
//    context: Context?,
//    recyclerView: RecyclerView,
//    private val mListener: OnItemClickListener?
//) :
//    OnItemTouchListener {
//    interface OnItemClickListener {
//        fun onItemClick(view: View?, position: Int)
//        fun onLongItemClick(view: View?, position: Int)
//    }
//
//    var mGestureDetector: GestureDetector
//
//    init {
//        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
//            override fun onSingleTapUp(e: MotionEvent): Boolean {
//                return true
//            }
//
//            override fun onLongPress(e: MotionEvent) {
//                val child = recyclerView.findChildViewUnder(e.x, e.y)
//                if (child != null && mListener != null) {
//                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
//                }
//            }
//        })
//    }
//
//    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
//        val childView = view.findChildViewUnder(e.x, e.y)
//        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
//            return true
//        }
//        return false
//    }
//
//    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
//    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
//}