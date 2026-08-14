package online.hicode.android.hihanfu.neumorphism.shape

import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import online.hicode.android.hihanfu.neumorphism.NeuStyle

abstract class NeuShape(open val cornerShape: CornerShape) {
    abstract fun draw(drawScope: ContentDrawScope, style: NeuStyle)
}