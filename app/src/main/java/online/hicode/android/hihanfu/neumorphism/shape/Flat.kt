package online.hicode.android.hihanfu.neumorphism.shape

import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import online.hicode.android.hihanfu.neumorphism.NeuStyle
import online.hicode.android.hihanfu.neumorphism.drawBackgroundShadows

class Flat(override val cornerShape: CornerShape) : NeuShape(cornerShape) {
    override fun draw(drawScope: ContentDrawScope, style: NeuStyle) {
        drawScope.drawBackgroundShadows(this, style)
        drawScope.drawContent()
    }
}