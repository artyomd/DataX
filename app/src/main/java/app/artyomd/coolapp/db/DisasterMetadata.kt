package app.artyomd.coolapp.db

class DisasterMetadata {
    companion object {
        const val TAG_NATURAL = "Natural"
        const val TAG_FIRE = "Fire"
        const val TAG_TRASH = "Trash"
        const val TAG_CAR = "Traffic Collision"
        const val TAG_OTHER = "Other"
    }
    var title:String? = null
    var id: String? = null
    var url: String? = null
    var longitude: Double = 0.toDouble()
    var latitude: Double = 0.toDouble()
    var tag: List<String>? = null
    var comment: String? = null
}