package app.artyomd.coolapp.share

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import app.artyomd.coolapp.R
import app.artyomd.coolapp.db.DisasterMetadata


class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {
    private val listChecked: MutableList<String> = mutableListOf();
    private val tagList: MutableList<String> = mutableListOf(
        DisasterMetadata.TAG_FIRE,
        DisasterMetadata.TAG_TRASH, DisasterMetadata.TAG_CAR, DisasterMetadata.TAG_OTHER
    )

    fun getSeltectd(): List<String> {
        if(listChecked.isEmpty()){
            listChecked.add(DisasterMetadata.TAG_OTHER)
        }
        return listChecked
    }

    private var added:Boolean = false

    @Synchronized
    public fun addSudgestions(list: List<String>) {
        if(added){
            return
        }
        tagList.remove(DisasterMetadata.TAG_OTHER)
        tagList.addAll(list)
        tagList.add(DisasterMetadata.TAG_OTHER)
        notifyDataSetChanged()
        added = true
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TagViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(v)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val string = tagList[position]
        holder.checkBox.text = string
        holder.checkBox.setOnCheckedChangeListener { _, _ ->
            run {
                if (holder.checkBox.isChecked) {
                    listChecked.add(holder.checkBox.text.toString())
                } else {
                    listChecked.remove(holder.checkBox.text.toString())
                }
            }
        }
    }

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)


    }
}