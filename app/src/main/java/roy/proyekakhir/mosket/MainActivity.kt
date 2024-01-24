package roy.proyekakhir.mosket

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import roy.proyekakhir.mosket.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var mClassifier: Classifier
    private lateinit var mBitmap: Bitmap
    private lateinit var mainBinding: ActivityMainBinding

    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "modelFinal.tflite"
    private val mLabelPath = "labels.txt"
    private val mSamplePath = "bgimg.jpg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
        hideButton(false)

        resources.assets.open(mSamplePath).use {
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
            mainBinding.mPhotoImageView.setImageBitmap(mBitmap)
        }

        mainBinding.mCameraButton.setOnClickListener {
            val message: String? = "1. Pastikan Dalam Keadaan Terang \n" +
                                    "\n" +
                                    "2. Arahkan Kamera Ke Salah Satu Motif \n" +
                                    "\n" +
                                    "3. Jarak Pengambilan 5 CM dari Motif\n"

            showCustomDialogBox(message)
        }
//

        mainBinding.mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
            hideButton(true)
        }


        mainBinding.mDetectButton.setOnClickListener {
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            val intent = Intent(this, OutputActivity::class.java)

            intent.putExtra("motif", results?.title+"\nAkurasi: "+ results?.confidence + "%")
            intent.putExtra("nama", results?.title)

            val stream = ByteArrayOutputStream()
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            intent.putExtra("image",byteArray)

            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = data.extras!!.get("data") as Bitmap
                mBitmap = scaleImage(mBitmap)
                mainBinding.mPhotoImageView.setImageBitmap(mBitmap)
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                println("Success!!!")
                mBitmap = scaleImage(mBitmap)
                mainBinding.mPhotoImageView.setImageBitmap(mBitmap)

            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()

        }
    }
    fun scaleImage(bitmap: Bitmap?): Bitmap {
        val orignalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / orignalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
    }

    private fun showCustomDialogBox(message: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)

        tvMessage.text = message

        btnYes.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, mCameraRequestCode)
            dialog.dismiss()
            hideButton(true)
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun hideButton(state: Boolean) {
        if (state) {
            mainBinding.mDetectButton.visibility = View.VISIBLE
        } else {
            mainBinding.mDetectButton.visibility = View.GONE
        }
    }
}