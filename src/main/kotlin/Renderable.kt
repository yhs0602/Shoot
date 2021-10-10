import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope

interface Renderable {
    fun render(drawScope: DrawScope)
}