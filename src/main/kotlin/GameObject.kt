interface GameObject : Renderable, Updatable {
}

interface DyingGameObject : GameObject {
    var alive: Boolean
}