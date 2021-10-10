import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.Key

class Player : GameObject, RoundBoundingObject {
    override val position = Vector2D(0f, 0f)
    val tears = ArrayList<NormalTear>()
    var range: Long = 800 // ms
    var fireRate: Float = 2f // 2 rps
    var moveSpeed: Float = 800f // 1 pixel per second?
    var tearSpeed: Float = 2000f
    val shooter = FireController(this, (1000 / fireRate).toLong())

    val velocity = Vector2D(0f, 0f)

    var life = 10
    override var radius: Int = 50

    override fun render(drawScope: DrawScope) = with(drawScope) {
        println("Render")
        drawCircle(
            color = Color.Cyan,
            center = Offset(x = position.x, y = position.y),
            radius = radius.toFloat()
        )
    }

    override fun update(timeDelta: Float) {
        velocity.x = 0f
        velocity.y = 0f

        if (Game.isKeyDown(Key.W)) {
            velocity.y += -moveSpeed
        }
        if (Game.isKeyDown(Key.A)) {
            velocity.x += -moveSpeed
        }

        if (Game.isKeyDown(Key.S)) {
            velocity.y += moveSpeed
        }
        if (Game.isKeyDown(Key.D)) {
            velocity.x += moveSpeed
        }
        position += velocity * timeDelta

        if (Game.isKeyDown(Key.DirectionRight)) {
            shooter.startFiring(Direction4.RIGHT)
        } else if (Game.isKeyDown(Key.DirectionLeft)) {
            shooter.startFiring(Direction4.LEFT)
        } else if (Game.isKeyDown(Key.DirectionDown)) {
            shooter.startFiring(Direction4.BOTTOM)
        } else if (Game.isKeyDown(Key.DirectionUp)) {
            shooter.startFiring(Direction4.TOP)
        } else {
            shooter.stopFiring()
        }
        shooter.update(timeDelta)
    }

    override fun onCollide(other: BoundingObject) {
    }

    fun damage(i: Int) {
        life -= i
    }
}

class FireController(val player: Player, var cool: Long) : Updatable {
    var isFiring = false
    var lastShootTime = 0L
    private var direction: Direction4? = null
    fun startFiring(direction4: Direction4) {
        isFiring = true
        direction = direction4
    }

    fun stopFiring() {
        isFiring = false
        direction = null
    }


    override fun update(timeDelta: Float) {
        if (!isFiring)
            return
        shoot()
    }

    private fun shoot() {
        val currentTime = System.currentTimeMillis()
        if (lastShootTime + cool > currentTime)
            return
        direction?.let {
            val normalTear = NormalTear(player, player.position, player.velocity, it.vector, player.range, player.tearSpeed)
            Game.newObject(normalTear)
            lastShootTime = currentTime
        }
    }
}