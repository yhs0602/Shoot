import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.Key
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Game : GameObject {
    private val players: MutableList<Player> = ArrayList()
    lateinit var currentPlayer: Player
    private val keyMap = HashMap<Long, Boolean>()
    private val objects = ArrayList<GameObject>()
    private val toDie = ArrayList<DyingGameObject>()
    private val toBorn = ArrayList<GameObject>()

    private val _frameTime = MutableStateFlow<Long>(0)
    val frameTime = _frameTime as StateFlow<Long>


    fun newObject(ob: GameObject) {
        toBorn.add(ob)
    }

    fun onKeyUp(code: Long) {
        keyMap[code] = false
    }

    fun onKeyDown(code: Long) {
        keyMap[code] = true
    }

    fun isKeyDown(key: Key): Boolean {
        return keyMap.getOrPut(key.keyCode) {
            false
        }
    }

    override fun render(drawScope: DrawScope) {
        for (obj in objects) {
            obj.render(drawScope)
        }
        renderHUD(drawScope)
    }

    private fun renderHUD(drawScope: DrawScope) = with(drawScope) {
        println("Render HUD")
        for (i in 0 until currentPlayer.life) {
            drawCircle(
                color = Color.Red,
                center = Offset(x = (i + 1) * 45f, y = 20f),
                radius = 20f
            )
        }
    }

    override fun update(timeDelta: Float) {
        println("Update $timeDelta")
        _frameTime.value += (timeDelta * 1000).toLong()
        for (obj in toBorn) {
            objects.add(obj)
        }
        toBorn.clear()
        for (obj in objects) {
            obj.update(timeDelta)
        }
        // collision check
        val boundingObjects: List<BoundingObject> = objects.mapNotNull { it as? BoundingObject }

        boundingObjects.permuteEach {
            if (it.first.isCollide(it.second)) {
                it.first.onCollide(it.second)
                it.second.onCollide(it.first)
            }
        }

        for (obj in toDie) {
            objects.remove(obj)
        }
        toDie.clear()
        println("Update done")
    }

    fun requestDie(obj: DyingGameObject) {
        toDie.add(obj)
    }

    fun init() {
        val player = Player()
        val enemy = Enemy()
        currentPlayer = player
        players.add(player)
        newObject(player)
        newObject(enemy)
    }
}

fun <T> List<T>.permuteEach(action: (Pair<T, T>) -> Unit) {
    for (i in indices) {
        val s = this[i]
        for (j in (i + 1) until size) {
            val p = Pair(s, this[j])
            action(p)
        }
    }
}