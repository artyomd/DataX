package app.artyomd.coolapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import app.artyomd.coolapp.camera.CameraFragment
import app.artyomd.coolapp.maps.MapsFragment
import app.artyomd.coolapp.share.ShareFragment

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
                Component.SHARE -> ShareFragment()
            }
        currentFragment!!.retainInstance = true
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container, currentFragment!!)
        transaction.commit()
    }

    override fun openCamera() {
        openComponent(Component.CAMERA)
    }

    override fun openShare(path:String) {
        openComponent(Component.SHARE)
    }

    companion object {
        enum class Component {
            MAP, CAMERA, SHARE
        }
    }
}
