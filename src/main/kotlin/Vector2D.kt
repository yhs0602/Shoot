import kotlin.math.sqrt

data class Vector2D(var x: Float, var y: Float) {
    operator fun times(fl: Float): Vector2D {
        return Vector2D(x * fl, y * fl)
    }

    operator fun plusAssign(other: Vector2D) {
        x += other.x
        y += other.y
    }

    fun norm(): Float {
        return sqrt(lengthSquared())
    }

    operator fun plus(other: Vector2D): Vector2D {
        return Vector2D(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2D): Vector2D {
        return Vector2D(x - other.x, y - other.y)
    }

    fun lengthSquared(): Float {
        return x * x + y * y
    }

    fun normalized(): Vector2D {
        return this / norm()
    }

    operator fun div(scalar: Float): Vector2D {
        return Vector2D(x / scalar, y / scalar)
    }

    constructor(other: Vector2D) : this(other.x, other.y)
}