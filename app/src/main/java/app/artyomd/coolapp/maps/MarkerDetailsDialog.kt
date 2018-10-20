package app.artyomd.coolapp.maps

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.artyomd.coolapp.R
import app.artyomd.coolapp.db.DisasterMetadata
import com.squareup.picasso.Picasso

class MarkerDetailsDialog(context: Context, metadata: DisasterMetadata) : Dialog(context) {

    private var metadata: DisasterMetadata? = metadata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.marker_details_dialog)
        val imageView: ImageView = findViewById(R.id.dialog_image)
        val commentTextView: TextView = findViewById(R.id.dialog_comment)
        val title: TextView = findViewById(R.id.dialog_title)

        if (!TextUtils.isEmpty(metadata!!.url)) {
            imageView.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(metadata!!.title)) {
            title.visibility = View.GONE
        }
        Picasso.get().load(metadata!!.url).into(imageView)
        commentTextView.text = metadata!!.comment
        title.text = metadata!!.title
    }
}
