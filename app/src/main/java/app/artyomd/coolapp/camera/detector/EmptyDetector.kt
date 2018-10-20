package app.artyomd.coolapp.camera.detector

import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame

class CapturingDetector : Detector<Unit>() {
    override fun detect(frame: Frame): SparseArray<Unit> {
        return SparseArray()
    }

}