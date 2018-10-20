package app.artyomd.coolapp.share

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.artyomd.coolapp.db.DisasterMetadata
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import app.artyomd.coolapp.R


class TagAdapter: RecyclerView.Adapter<TagAdapter.TagViewHolder>() {
    private val listChecked:MutableList<String> = mutableListOf();
    private val tagList:MutableList<String> = mutableListOf(DisasterMetadata.TAG_FIRE,
        DisasterMetadata.TAG_TRASH, DisasterMetadata.TAG_CAR, DisasterMetadata.TAG_OTHER)

    fun  getSeltectd():List<String>{
        return listChecked
    }

    public fun addSudgestions(list:List<String>){
        tagList.remove(DisasterMetadata.TAG_OTHER)
        tagList.addAll(list)
        tagList.add(DisasterMetadata.TAG_OTHER)
        notifyDataSetChanged()
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
        holder.textView.text = string
        holder.checkBox.setOnCheckedChangeListener { _, _ -> {
            if(holder.checkBox.isChecked){
                listChecked.add(holder.textView.text.toString())
            }else{
                listChecked.remove(holder.textView.text.toString())
            }
        } }
    }

    class TagViewHolder(view:View):RecyclerView.ViewHolder(view){
        val textView:TextView = view.findViewById(R.id.textView)
        val checkBox:CheckBox = view.findViewById(R.id.checkbox)



    }
}