package app.artyomd.coolapp.share

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.artyomd.coolapp.CommonConstants
import app.artyomd.coolapp.R
import app.artyomd.coolapp.api.ReliefService
import app.artyomd.coolapp.db.DB
import app.artyomd.coolapp.db.DisasterMetadata
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.max


class ShareFragment : Fragment() {

    private var imagePath: String? = null
    private var imageView: ImageView? = null
    private var db: DB? = null
    private var chosenFile: File? = null
    private var commentEditText: EditText? = null
    private var shareButton: Button? = null
    private lateinit var recyclerView: RecyclerView
    private val tagAdapter = TagAdapter();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        db = DB.instance
        commentEditText = view.findViewById(R.id.comment)
        shareButton = view.findViewById(R.id.share_button)
        imageView = view.findViewById(R.id.share_image)
        imagePath = args!!.getString(CommonConstants.EXTRA_IMAGE_PATH)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = tagAdapter

        Picasso.get().load(File(imagePath)).into(imageView)
        val metadata = DisasterMetadata()

        metadata.id = UUID.randomUUID().toString()
        metadata.latitude = args.getDouble(CommonConstants.EXTRA_IMAGE_LATITUDE)
        metadata.longitude = args.getDouble(CommonConstants.EXTRA_IMAGE_LONGITUDE)
        metadata.tag = args.getStringArrayList(CommonConstants.EXTRA_IMAGE_TAGS)

        chosenFile = File(imagePath)
        val bitmap = downscaleImage(chosenFile!!)
        runVision(bitmap)

        shareButton!!.setOnClickListener { upload(metadata) }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun downscaleImage(file: File):Bitmap{
        var bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val width = bitmap.width;
        val height = bitmap.height
        val maxSize = max(width, height)
        if(maxSize > 500){
            val scale = 500f/maxSize.toFloat()
            val newHeght = scale*height
            val newWidth = scale*width
            bitmap = ReliefService.createResizedScaledBitmap(bitmap, newWidth.toInt(), newHeght.toInt(), null)
            file.delete();
            try {
                FileOutputStream(file.absoluteFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    private fun runVision(bitmap: Bitmap){
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionLabelDetectorOptions.Builder().setConfidenceThreshold(0.8f).build();
            FirebaseVision.getInstance()
            .getVisionLabelDetector(options).detectInImage(image)
            .addOnSuccessListener { it ->
                val list = mutableListOf<String>()
                it.forEach{
                    val string = it.label
                    if(!(string == "fire" || string == "trash" || string == "car accident") && !list.contains(string)){
                        list.add(string)
                    }
                    tagAdapter.addSudgestions(list)
                }
            }.addOnFailureListener{
                it.printStackTrace()
            }


    }

    companion object {
        @JvmStatic
        fun newInstance(path: String, latitude: Double, longitude: Double) = ShareFragment().apply {
            arguments = Bundle().apply {
                putString(CommonConstants.EXTRA_IMAGE_PATH, path)
                putDouble(CommonConstants.EXTRA_IMAGE_LATITUDE, latitude)
                putDouble(CommonConstants.EXTRA_IMAGE_LONGITUDE, longitude)
            }
        }
    }

    private fun upload(metadata: DisasterMetadata) {
        if (chosenFile == null) {
            Toast.makeText(this@ShareFragment.context, "Choose a file before upload.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        metadata.tag = tagAdapter.getSeltectd()
        val progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Please wait")
        progressDialog.show()
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
                    metadata.comment = commentEditText!!.text.toString()
                    db!!.uploadDisaster(metadata)
                    progressDialog.dismiss()
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
