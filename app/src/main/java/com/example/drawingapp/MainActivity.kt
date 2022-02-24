package com.example.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import java.security.Permissions

class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView?=null

    private var mImageButtonCurrentPaint:ImageButton?=null

    val openGalleryLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data!=null){
                val imageBackground:ImageView = findViewById(R.id.iv_background)

                imageBackground.setImageURI(result.data?.data)
            }
        }

    val requestPermission:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            Permissions ->
            Permissions.entries.forEach {
                //so that's why i can just check if is granted ok,because it will be either true or false.
                //get it name permission individual
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted){// this is granted
                    Toast.makeText(this,"Permission granted now you can read the storage files.",
                        Toast.LENGTH_LONG).show()
                    val pickIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)

                }else{ // this not granted
                    if (permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this," you just denied the permission.",
                            Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView= findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        //give color of linear layout
        val linearLayoutPaintColor:LinearLayout = findViewById(R.id.ll_paint_colors)
        mImageButtonCurrentPaint = linearLayoutPaintColor[1] as ImageButton
        // here use inside our applications
            mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(
                this, R.drawable.pallet_pressed)
            )

        ib_gallery_men.setOnClickListener {
            requestStoragePermission()
        }

       val ib_brush: ImageButton = findViewById(R.id.ib_brush_men)
        ib_brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

    }
    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")
        val smallBtn = brushDialog.iBtn_small_brush
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(8.toFloat())
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.iBtn_medium_brush
        mediumBtn.setOnClickListener {
            drawingView?.setSizeForBrush(16.toFloat())
            brushDialog.dismiss()
        }
        val largeBtn = brushDialog.iBtn_large_brush
        largeBtn.setOnClickListener {
            drawingView?.setSizeForBrush(24.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()

    }

    fun paintClicked(view: View){
        if (view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)
           imageButton.setImageDrawable(ContextCompat.getDrawable(
                this, R.drawable.pallet_pressed
            ))
            mImageButtonCurrentPaint?.setImageDrawable(ContextCompat.getDrawable(
                this, R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint = view

        }
       // Toast.makeText(this,"click paint",Toast.LENGTH_LONG).show()
    }
    private fun requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            showRationaleDialog("Kids Drawing App","Kids Drawing App "+
                    "need to Access Your External Storage")
        }else{
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE


            ))
        }
    }

    private fun showRationaleDialog(title:String,message:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("cancel"){dialog,_->
                dialog.dismiss()
            }
        builder.create().show()
    }
}