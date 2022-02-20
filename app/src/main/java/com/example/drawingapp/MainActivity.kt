package com.example.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView?=null

    private var mImageButtonCurrentPaint:ImageButton?=null


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
                this, R.drawable.pallet_pressed
            ))
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
}