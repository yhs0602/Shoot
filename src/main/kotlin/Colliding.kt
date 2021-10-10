interface BoundingObject {
    fun onCollide(other: BoundingObject)
    fun isCollide(other: BoundingObject): Boolean
}

interface RoundBoundingObject : BoundingObject {
    var radius: Int
    val position: Vector2D
    override fun isCollide(other: BoundingObject): Boolean {
        if (other is RoundBoundingObject) {
            val minDist = radius + other.radius
            return (position - other.position).lengthSquared() <= minDist * minDist
        }
        return false
    }
}
