package khoina.weatherforecast.view

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalDividerDecoration(
    private val divider: Drawable
) : RecyclerView.ItemDecoration() {

    private val dividerBounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, dividerBounds)
            val bottom: Int = dividerBounds.bottom + Math.round(child.translationY)
            val top: Int = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }
}
