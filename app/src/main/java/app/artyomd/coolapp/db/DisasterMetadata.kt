package app.artyomd.coolapp.db

class DisasterMetadata {
    companion object {
        const val TAG_NATURAL = "natural"
        const val TAG_FIRE = "fire"
        const val TAG_TRASH = "trash"
        const val TAG_CAR = "car"
        const val TAG_OTHER = "other"
    }
    var title:String? = null
    var id: String? = null
    var url: String? = null
    var longitude: Double = 0.toDouble()
    var latitude: Double = 0.toDouble()
    var tag: List<String>? = null
    var comment: String? = null
}