package com.nothinglondon.sdkdemo.demos.glyphbutton

import android.content.Context
import com.nothing.ketchum.GlyphMatrixManager
import com.nothinglondon.sdkdemo.demos.GlyphMatrixService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GlyphButtonDemoService : GlyphMatrixService("CUPSIZE") {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var animationJob: Job? = null

    private val word = "CUPSIZE"

    private val font = mapOf(
        'C' to arrayOf("01110", "10001", "10000", "10000", "10000", "10001", "01110"),
        'U' to arrayOf("10001", "10001", "10001", "10001", "10001", "10001", "01110"),
        'P' to arrayOf("11110", "10001", "10001", "11110", "10000", "10000", "10000"),
        'S' to arrayOf("01111", "10000", "10000", "01110", "00001", "00001", "11110"),
        'I' to arrayOf("11111", "00100", "00100", "00100", "00100", "00100", "11111"),
        'Z' to arrayOf("11111", "00001", "00010", "00100", "01000", "10000", "11111"),
        'E' to arrayOf("11111", "10000", "10000", "11110", "10000", "10000", "11111")
    )

    override fun performOnServiceConnected(
        context: Context,
        glyphMatrixManager: GlyphMatrixManager
    ) {
        showStaticText()
    }

    override fun onTouchPointLongPress() {
        animationJob?.cancel()

        animationJob = scope.launch {
            val textWidth = word.length * 6 - 1

            for (offset in 0..(textWidth + 25)) {
                drawText(25 - offset)
                delay(70)
            }

            showStaticText()
        }
    }

    private fun showStaticText() {
        val textWidth = word.length * 6 - 1
        drawText((25 - textWidth) / 2)
    }

    private fun drawText(startX: Int) {
        val frame = IntArray(25 * 25)

        word.forEachIndexed { index, letter ->
            val pattern = font[letter] ?: return@forEachIndexed

            pattern.forEachIndexed { y, row ->
                row.forEachIndexed { x, pixel ->
                    val px = startX + index * 6 + x
                    val py = 9 + y

                    if (pixel == '1' && px in 0..24 && py in 0..24) {
                        frame[py * 25 + px] = 2047
                    }
                }
            }
        }

        glyphMatrixManager?.setMatrixFrame(frame)
    }

    override fun onDestroy() {
        animationJob?.cancel()
        scope.cancel()
        super.onDestroy()
    }
}
