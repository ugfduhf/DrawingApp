package com.example.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView= findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())
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
}