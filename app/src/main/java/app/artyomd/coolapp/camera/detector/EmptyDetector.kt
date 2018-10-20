package app.artyomd.coolapp.camera.detector

import android.graphics.Bitmap
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame

class CapturingDetector : Detector<Unit>() {
    private var bitmap: Bitmap? = null
    override fun detect(frame: Frame): SparseArray<Unit> {
        this.bitmap = frame.bitmap
        return SparseArray()
    }

    fun getLastBitmap(): Bitmap? {
        return bitmap
    }

}