package eu.tutorials.licenta_yummy.models.instructions_models

data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>,
    val equipment: List<Equipment>
)

