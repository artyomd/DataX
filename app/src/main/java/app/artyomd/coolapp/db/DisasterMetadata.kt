package app.artyomd.coolapp.db

import android.graphics.Bitmap

import java.io.Serializable

class DisasterMetadata : Serializable {
    var id: String? = null
    var url: String? = null
    var longitude: Float = 0.toFloat()
    var latitude: Float = 0.toFloat()
    var tags: List<String>? = null

    constructor(id: String, url: String, longitude: Float, latitude: Float, tags: List<String>) {
        this.id = id
        this.url = url
        this.longitude = longitude
        this.latitude = latitude
        this.tags = tags
    }

    constructor() {

    }
}
