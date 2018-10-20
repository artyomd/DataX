package app.artyomd.coolapp.share


/**
 * Created by maxim on 27/02/2017.
 */

class ImageResponse {
    var success: Boolean = false
    var status: Int = 0
    var data: UploadedImage? = null

    class UploadedImage {
        var id: String? = null
        var title: String? = null
        var description: String? = null
        var type: String? = null
        var animated: Boolean = false
        var width: Int = 0
        var height: Int = 0
        var size: Int = 0
        var views: Int = 0
        var bandwidth: Int = 0
        var vote: String? = null
        var favorite: Boolean = false
        var account_url: String? = null
        var deletehash: String? = null
        var name: String? = null
        var link: String? = null

        override fun toString(): String {
            return "UploadedImage{" +
                    "id='" + id + '\''.toString() +
                    ", title='" + title + '\''.toString() +
                    ", description='" + description + '\''.toString() +
                    ", type='" + type + '\''.toString() +
                    ", animated=" + animated +
                    ", width=" + width +
                    ", height=" + height +
                    ", size=" + size +
                    ", views=" + views +
                    ", bandwidth=" + bandwidth +
                    ", vote='" + vote + '\''.toString() +
                    ", favorite=" + favorite +
                    ", account_url='" + account_url + '\''.toString() +
                    ", deletehash='" + deletehash + '\''.toString() +
                    ", name='" + name + '\''.toString() +
                    ", link='" + link + '\''.toString() +
                    '}'.toString()
        }
    }

    override fun toString(): String {
        return "ImageResponse{" +
                "success=" + success +
                ", status=" + status +
                ", data=" + data!!.toString() +
                '}'.toString()
    }
}