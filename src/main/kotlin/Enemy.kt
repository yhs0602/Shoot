import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

class Enemy : GameObject {
    val tears = ArrayList<NormalTear>()
    var fireRate: Float = 2f // 2 rps
    var tearSpeed: Float = 2000f

    override fun render(drawScope: DrawScope) = with(drawScope) {
    }

    override fun update(timeDelta: Float) {
        if (Random.nextInt() % 100 < 85) {
            val startX = (Random.nextInt(5000, 7000) * (Random.nextInt(2) * 2 - 1)).toFloat()
            val startY = (Random.nextInt(5000, 7000) * (Random.nextInt(2) * 2 - 1)).toFloat()
            println("StartX: $startX, starty: $startY")

            val velX = (-startX % 3000) * Random.nextFloat()
            val velY = (-startY % 3000) * Random.nextFloat()
            Game.newObject(
                HomingTear(
                    null,
                    Vector2D(startX, startY),
                    Vector2D(0f, 0f),
                    Vector2D(velX, velY),
                    100000,
                    1000f,
                    Game.currentPlayer
                )
            )
        }
    }
}
