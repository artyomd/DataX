package app.artyomd.coolapp.share

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList
import android.widget.Toast
import okhttp3.RequestBody
import okhttp3.MultipartBody
import android.util.Log
import android.widget.EditText
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class ShareFragment : Fragment() {

    private var imagePath: String? = null
    private var imageView: ImageView? = null
    private var db: DB? = null
    private var chosenFile: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        db = DB.instance
        imageView = view.findViewById(R.id.share_image)
        imagePath = args!!.getString(CommonConstants.EXTRA_IMAGE_PATH)
        Picasso.get().load(imagePath).into(imageView)
        val metadata = DisasterMetadata()

        metadata.id = UUID.randomUUID().toString()
        metadata.latitude = args.getFloat(CommonConstants.EXTRA_IMAGE_LATITUDE)
        metadata.longitude = args.getFloat(CommonConstants.EXTRA_IMAGE_LONGITUDE)
        metadata.tags = args.getStringArrayList(CommonConstants.EXTRA_IMAGE_TAGS)

        chosenFile = File(imagePath)
        upload(metadata)

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String) = ShareFragment().apply {
            arguments = Bundle().apply {
                putString(CommonConstants.EXTRA_IMAGE_PATH, path)
            }
        }
    }

    private fun upload(metadata: DisasterMetadata) {
        if (chosenFile == null) {
            Toast.makeText(this@ShareFragment.context, "Choose a file before upload.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val imgurService = ImgurService.retrofit.create(ImgurService::class.java)

        val call = imgurService.postImage(
            metadata.id!!,
            "", "", "",
            MultipartBody.Part.createFormData(
                "image",
                chosenFile!!.name,
                RequestBody.create(MediaType.parse("image/*"), chosenFile)
            )
        )

        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>?) {
                if (response == null) {
                    Toast.makeText(this@ShareFragment.context, "fail", Toast.LENGTH_SHORT).show()
                    return
                }
                if (response.isSuccessful) {
                    Toast.makeText(this@ShareFragment.activity, "Upload successful !", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("URL Picture", "http://imgur.com/" + response.body()!!.data!!.id)
                    metadata.url = response.body()!!.data!!.link
                    db!!.uploadDisaster(metadata)
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Toast.makeText(this@ShareFragment.activity, "An unknown error has occured.", Toast.LENGTH_SHORT)
                    .show()
                t.printStackTrace()
            }
        })
    }
}
