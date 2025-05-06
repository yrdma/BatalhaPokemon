class Efetividade{
    val vantagemTipos = mapOf(
        "Normal" to listOf(),
        "Fogo" to listOf("Grama", "Gelo", "Inseto", "Aco"),
        "Agua" to listOf("Fogo", "Terrestre", "Pedra"),
        "Planta" to listOf("Agua", "Terrestre", "Pedra"),
        "Eletrico" to listOf("Agua", "Voador"),
        "Gelo" to listOf("Grama", "Terrestre", "Voador", "Dragao"),
        "Lutador" to listOf("Normal", "Gelo", "Pedra", "Sombra", "Aco"),
        "Venenoso" to listOf("Grama", "Fada"),
        "Terrestre" to listOf("Fogo", "Eletrico", "Venenoso", "Pedra", "Aco"),
        "Voador" to listOf("Grama", "Lutador", "Inseto"),
        "Psiquico" to listOf("Lutador", "Venenoso"),
        "Inseto" to listOf("Grama", "Psiquico", "Sombra"),
        "Pedra" to listOf("Fogo", "Gelo", "Voador", "Inseto"),
        "Sombra" to listOf("Psiquico", "Sombra"),
        "Dragao" to listOf("Dragao"),
        "Aco" to listOf("Gelo", "Pedra", "Fada"),
        "Fada" to listOf("Lutador", "Dragao", "Sombra")
    )
}