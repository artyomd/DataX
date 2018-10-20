package app.artyomd.coolapp.share

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.artyomd.coolapp.CommonConstants
import app.artyomd.coolapp.R
import app.artyomd.coolapp.db.DB
import app.artyomd.coolapp.db.DisasterMetadata

class ShareFragment : Fragment() {

    private var imagePath: String? = null
    private var imageView: ImageView? = null
    private var db: DB? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater.inflate(R.layout.fragment_share, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        db = DB.getInstance()
        imageView = view.findViewById(R.id.share_image)
        imagePath = args!!.getString(CommonConstants.EXTRA_IMAGE_PATH)
        val image = BitmapFactory.decodeFile(imagePath)
        if (imagePath != null) {
            imageView!!.setImageBitmap(image)
        }
        val metadata = DisasterMetadata()
        metadata.image = image
        metadata.latitude = args.getFloat(CommonConstants.EXTRA_IMAGE_LATITUDE)
        metadata.longitude = args.getFloat(CommonConstants.EXTRA_IMAGE_LONGITUDE)
        metadata.tags = args.getStringArrayList(CommonConstants.EXTRA_IMAGE_TAGS)
        db!!.uploadDisaster(metadata)
        super.onViewCreated(view, savedInstanceState)
    }

}
