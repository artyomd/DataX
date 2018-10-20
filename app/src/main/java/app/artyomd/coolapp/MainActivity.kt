package app.artyomd.coolapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import app.artyomd.coolapp.camera.CameraFragment
import app.artyomd.coolapp.maps.MapsFragment

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private var currentFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openComponent(Component.MAP)
    }

    private fun openComponent(component: Component) {
        currentFragment =
            when (component) {
                Component.MAP -> MapsFragment()
                Component.CAMERA -> CameraFragment()
            }
        currentFragment!!.retainInstance = true
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container, currentFragment!!)
        transaction.commit()
    }

    override fun openCamera() {
        openComponent(Component.CAMERA)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != CameraFragment.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // we have permission, so create the camerasource
            (currentFragment as CameraFragment).createCameraSource()
            return
        }

        val listener = DialogInterface.OnClickListener { _, _ -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission")
            .setMessage(R.string.no_camera_permission)
            .setPositiveButton(R.string.ok, listener)
            .show()
    }

    companion object {
        enum class Component {
            MAP, CAMERA
        }
    }
}
