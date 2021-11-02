package com.chufarnov.areyoulucky.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.chufarnov.areyoulucky.Impls.COLUMNS_NUM
import com.chufarnov.areyoulucky.Impls.ROWS_NUM
import com.chufarnov.areyoulucky.domain.entities.*
import com.example.areyoulucky.R
import java.util.*

private const val ITEM_IMAGE_OFFSET = 10

class GameMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var isClicksEnabled = true

    private lateinit var openedCellsCanvas: Canvas
    private lateinit var openedCellsBitmap: Bitmap

    private lateinit var closedCellsCanvas: Canvas
    private lateinit var closedCellsBitmap: Bitmap

    private var isSizesKnown = false

    private var cachedMap: MapEntity? = null

    private var cellWidth = 0.0f
    private var cellHeight = 0.0f

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }

    private var onClickListener: ((Int) -> Unit)? = null

    init {
        if (Locale.getDefault().language == "en") {
            textPaint.typeface = ResourcesCompat.getFont(context, R.font.game_font)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cellWidth = w.toFloat() / COLUMNS_NUM
        cellHeight = h.toFloat() / ROWS_NUM

        isSizesKnown = true

        textPaint.textSize = cellHeight / 2

        initBitmaps(w, h)

        cachedMap?.let {
            drawMap(it)
        }
    }

    private fun initBitmaps(w: Int, h: Int) {
        if (::closedCellsBitmap.isInitialized) closedCellsBitmap.recycle()
        closedCellsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        closedCellsCanvas = Canvas(closedCellsBitmap)

        if (::openedCellsBitmap.isInitialized) openedCellsBitmap.recycle()
        openedCellsBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        openedCellsCanvas = Canvas(openedCellsBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(openedCellsBitmap, 0f, 0f, null)
        canvas.drawBitmap(closedCellsBitmap, 0f, 0f, null)
    }

    fun setOnCellClickListener(listener: (Int) -> Unit) {
        onClickListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isClicksEnabled) return true

        if (event.action == MotionEvent.ACTION_DOWN) {
            onClickListener?.invoke(
                touchCoordinatesToPosition(event.x, event.y)
            )
        }

        return true
    }

    private fun touchCoordinatesToPosition(x: Float, y: Float): Int =
        (y / cellHeight).toInt() * COLUMNS_NUM + (x / cellWidth).toInt()

    fun openCell(position: Int) {
        closedCellsCanvas.apply {
            save()
            translate(getLeftOffset(position), getTopOffset(position))
            clipRect(getCellRectWithOffset())
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            restore()
        }
    }

    fun drawNewLevelMap(map: MapEntity) {
        if (isSizesKnown) {
            drawMap(map)
        }

        cachedMap = map
    }

    private fun drawMap(map: MapEntity) {
        closedCellsCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        openedCellsCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawCellsContent(map)
        drawClosedCells(map)
        invalidate()
    }

    private fun drawCellsContent(map: MapEntity) {
        var position = 0

        map.cells.forEach {
            if (it.content.type != ItemType.EMPTY) {
                drawCellContent(position, it.content)
            }

            position++
        }
    }

    private fun drawClosedCells(map: MapEntity) {
        var position = 0

        map.cells.forEach {
            if (it.state == CellState.CLOSED) {
                drawClosedCell(position, it.color)
            }

            position++
        }
    }

    private fun drawCellContent(position: Int, content: ItemEntity) {
        openedCellsCanvas.save()
        openedCellsCanvas.translate(getLeftOffset(position), getTopOffset(position))

        VectorDrawableCompat.create(context.resources, content.getDrawable(), null)?.apply {
            setTint(context.resources.getColor(content.getColor(), null))
            bounds = getCellRectWithOffset()
            draw(openedCellsCanvas)
        }

        if (content.value != 0) {
            drawCenterAlignedText(
                openedCellsCanvas,
                getCellRectWithOffset(),
                content.value.toString()
            )
        }

        openedCellsCanvas.restore()
    }

    private fun getCellRectWithOffset() = Rect(
        ITEM_IMAGE_OFFSET,
        ITEM_IMAGE_OFFSET,
        cellWidth.toInt() - ITEM_IMAGE_OFFSET,
        cellHeight.toInt() - ITEM_IMAGE_OFFSET
    )

    private fun drawCenterAlignedText(canvas: Canvas, cellRect: Rect, text: String) {
        val textRect = Rect()
        textPaint.getTextBounds(text, 0, text.length, textRect)

        canvas.drawText(
            text,
            cellRect.width() / 2f - textRect.width() / 2f - textRect.left,
            cellRect.height() / 2f + textRect.height() / 2f - textRect.bottom,
            textPaint
        )
    }

    private fun drawClosedCell(position: Int, color: GameColor) {
        closedCellsCanvas.apply {
            save()
            translate(getLeftOffset(position), getTopOffset(position))
            clipRect(getCellRectWithOffset())
            drawColor(resources.getColor(color.getResourceColor(), null))
            restore()
        }
    }

    private fun getLeftOffset(position: Int): Float {
        return cellWidth * (position % ROWS_NUM)
    }

    private fun getTopOffset(position: Int): Float {
        return cellHeight * (position - (position % COLUMNS_NUM)) / COLUMNS_NUM
    }
}