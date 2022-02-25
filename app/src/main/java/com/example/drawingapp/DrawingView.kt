package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context,attrs: AttributeSet):View(context, attrs) {
    // customPath is a class that we need to create because
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()

    // start with color black for draw
    private var color = Color.BLACK

    //background , the white background that i want to draw on
    private var canvas: Canvas? = null
    private var mPaths = ArrayList<CustomPath>()
    private var mUndoPaths = ArrayList<CustomPath>()
    private var mRdoPaths = ArrayList<CustomPath>()


    init {
        setUpDrawing()
    }
    // delete (undo)
    fun onClickUndo(){
        if (mPaths.size > 0 ){

            mUndoPaths.add(mPaths.removeAt(mPaths.size -1))

            invalidate()
        }
    }
    // redo
    fun onClickRedo(){
        if (mRdoPaths.size > 0 ){


            mRdoPaths = mUndoPaths

            invalidate()

        }
    }
    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
       // mBrushSize = 20.toFloat()//  Move to Main Activity
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //what should happen when we want to draw by specific method following this
    //change Canvas to canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCanvasBitmap?.let {
            canvas.drawBitmap(it, 0f,   0f, mCanvasPaint)
        }

       // canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for (p in mPaths) {
            mDrawPaint?.strokeWidth = p.brushThickness
            mDrawPaint?.color = p.color
            canvas.drawPath(p, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // create variable which is going to store the value of where it was touched.
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = color
                mDrawPath?.brushThickness = mBrushSize

                mDrawPath?.reset() // Clear any lines and curves from the path, making it empty.
                mDrawPath?.moveTo(
                    touchX!!,
                    touchY!!
                ) // Set the beginning of the next contour to the point (x,y).
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo(
                    touchX!!,
                    touchY!!
                ) // Add a line from the last point to the specified point (x,y).
            }

            MotionEvent.ACTION_UP -> {

                mPaths.add(mDrawPath!!) //Add when to stroke is drawn to canvas and added in the path arraylist

                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()
        return true
    }
    fun setSizeForBrush(newSize:Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        newSize,resources.displayMetrics
        )
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    fun setColor(newColor:String){
    color = Color.parseColor(newColor)
    mDrawPaint!!.color= color
    }

   internal inner class CustomPath(var color:Int, var brushThickness:Float) : Path() {

    }
}
