import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

open class NormalTear(
    val shooter: Player?,
    val origin: Vector2D,
    val initVelocity: Vector2D,
    val direction: Vector2D,
    val range: Long,
    val speed: Float,
) : DyingGameObject, RoundBoundingObject {
    override var radius = 20
    override val position = Vector2D(origin)

    override fun onCollide(other: BoundingObject) {
        if (other is NormalTear) {
            return
        }
        if (other is Player) {
            if (other == shooter) {
                return
            }
            other.damage(1)
        }
    }

    var moved: Long = 0L
    override var alive = true
    val velocity = direction * speed + initVelocity

    override fun render(drawScope: DrawScope) = with(drawScope) {
        drawCircle(
            color = Color.Blue,
            center = Offset(x = position.x, y = position.y),
            radius = radius.toFloat()
        )
    }

    override fun update(timeDelta: Float) {
        val delta = velocity * timeDelta
        moved += (timeDelta * 1000).toLong()
        if (moved <= range) {
            position += delta
        } else { // out of range. die.
            alive = false
            Game.requestDie(this)
        }
    }
}

class HomingTear(
    shooter: Player?,
    origin: Vector2D,
    initVelocity: Vector2D,
    direction: Vector2D,
    range: Long,
    speed: Float,
    val target: Player
) : NormalTear(shooter, origin, initVelocity, direction, range, speed) {
    override fun render(drawScope: DrawScope) = with(drawScope) {
        drawCircle(
            color = Color.Green,
            center = Offset(x = position.x, y = position.y),
            radius = radius.toFloat()
        )
    }

    override fun update(timeDelta: Float) {
        val diffPos = (target.position - position).normalized()
        val newVelocity = (velocity.normalized() + diffPos) * 10000f
        val delta = newVelocity.normalized() * timeDelta * speed
        moved += (timeDelta * 1000).toLong()
        if (moved <= range) {
            position += delta
        } else { // out of range. die.
            alive = false
            Game.requestDie(this)
        }
    }
}

