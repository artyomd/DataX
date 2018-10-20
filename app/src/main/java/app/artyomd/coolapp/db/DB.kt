package app.artyomd.coolapp.db

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DB private constructor() {

    fun uploadDisaster(metadata: DisasterMetadata) {
        reference!!.child(metadata.id!!).setValue(metadata)
    }

    companion object {

        private var db: DB? = null
        private var firebaseDatabase: FirebaseDatabase? = null
        private var reference: DatabaseReference? = null

        val instance: DB
            get() {
                db = DB()
                firebaseDatabase = FirebaseDatabase.getInstance()
                reference = firebaseDatabase!!.getReference("disasters")
                return db!!
            }
    }
}
