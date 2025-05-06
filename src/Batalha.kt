import kotlin.random.Random

fun mostrarMenu(){
    println("1 Escolher POkemons  ")
    println("2 COMEÇAR BATALHA ")
    println("3 Ver Pokemons ESCOLHIDOS ")
    println("4 Sair ")
}

val pokemonsDisponiveis = Pokemons()// Supondo que Pokemons() seja a lista ou mapa de pokémons

fun main() {
    val jogadores = mutableMapOf<String, MutableList<Map<String, Any>>>()
    var opcao: String

    do {
        println("\n== MENU ==")
        println("1 - Cadastrar Jogador")
        println("2 - Escolher Pokémon para o time")
        println("3 - Remover Pokémon do time")
        println("4 - Iniciar Batalha")
        println("5 - Listar Jogadores e Times")
        println("0 - Sair")
        print("Escolha uma opção: ")
        opcao = readLine()?.trim().orEmpty()

        when (opcao) {
            "1" -> {
                print("Digite o nome do jogador: ")
                val nome = readLine()?.trim().orEmpty()
                if (nome.isNotEmpty()) {
                    if (jogadores.containsKey(nome)) {
                        println("Jogador já cadastrado!")
                    } else {
                        jogadores[nome] = mutableListOf()
                        println("Jogador $nome cadastrado com sucesso!")
                    }
                }
            }

            "2" -> {
                println("Jogadores disponíveis:")
                jogadores.keys.forEach { println("- $it") }
                print("Escolha o jogador para montar o time: ")
                val nome = readLine()?.trim().orEmpty()
                if (jogadores.containsKey(nome)) {
                    jogadores[nome] = escolherPokemons(nome)
                } else {
                    println("Jogador não encontrado.")
                }
            }

            "3" -> {
                println("Jogadores disponíveis:")
                jogadores.keys.forEach { println("- $it") }
                print("Escolha o jogador para remover um Pokémon do time: ")
                val nome = readLine()?.trim().orEmpty()
                val time = jogadores[nome]
                if (time != null && time.isNotEmpty()) {
                    println("Time atual:")
                    time.forEachIndexed { i, p -> println("${i + 1} - ${p["nome"]}") }
                    print("Digite o número do Pokémon a remover: ")
                    val indice = readLine()?.toIntOrNull()
                    if (indice != null && indice in 1..time.size) {
                        val removido = time.removeAt(indice - 1)
                        println("Pokémon ${removido["nome"]} removido do time de $nome.")
                    } else {
                        println("Índice inválido.")
                    }
                } else {
                    println("Time vazio ou jogador inválido.")
                }
            }

            "4" -> {
                println("Jogadores disponíveis:")
                jogadores.keys.forEach { println("- $it") }
                print("Digite o nome do Jogador 1: ")
                val j1 = readLine()?.trim().orEmpty()
                print("Digite o nome do Jogador 2: ")
                val j2 = readLine()?.trim().orEmpty()
                val time1 = jogadores[j1]
                val time2 = jogadores[j2]

                if (time1 != null && time2 != null && time1.isNotEmpty() && time2.isNotEmpty()) {
                    batalhar(time1, time2, j1, j2)
                } else {
                    println("Ambos os jogadores devem ter times com Pokémon.")
                }
            }

            "5" -> {
                println("== Jogadores e seus times ==")
                jogadores.forEach { (nome, time) ->
                    println("Jogador: $nome")
                    if (time.isEmpty()) println("  Time vazio.")
                    else time.forEach { p -> println("  - ${p["nome"]}") }
                }
            }

            "0" -> println("Encerrando a batalha. Até logo!")

            else -> println("Opção inválida. Tente novamente.")
        }

    } while (opcao != "0")
}





fun escolherPokemons(nomeJogador: String): MutableList<Map<String, Any>> {
    val listaPokemons = pokemonsDisponiveis.getLista() // Método que retorna os pokémons disponíveis
    val timeEscolhido = mutableListOf<Map<String, Any>>()
    val nomesDisponiveis = listaPokemons.map { it["nome"] as String }

    println("\n$nomeJogador, escolha 3 Pokémon para o seu time:")

    // Exibe a lista de Pokémons disponíveis uma vez antes de começar a escolha
    println("\nPokémons disponíveis:")
    nomesDisponiveis.forEachIndexed { index, nome -> println("${index + 1}. $nome") }

    for (i in 1..3) {
        while (true) {
            print("Escolha o ${i}º Pokémon (nome ou número): ")
            val entrada = readLine()?.trim()

            val pokemonSelecionado: Map<String, Any>? = when {
                entrada?.toIntOrNull() != null -> {
                    val indice = entrada.toInt() - 1
                    if (indice in listaPokemons.indices) listaPokemons[indice] else null
                }
                else -> listaPokemons.find { it["nome"].toString().equals(entrada, ignoreCase = true) }
            }

            if (pokemonSelecionado != null && !timeEscolhido.contains(pokemonSelecionado)) {
                // Cópia do mapa para evitar alterações no original
                timeEscolhido.add(pokemonSelecionado.toMutableMap())
                break
            } else {
                println("Escolha inválida ou Pokémon já selecionado. Tente novamente.")
            }
        }
    }

    return timeEscolhido
}

fun batalhar(time1: MutableList<Map<String, Any>>, time2: MutableList<Map<String, Any>>, nomeJogador1: String, nomeJogador2: String) {
    val timeJogador1 = time1.map { it.toMutableMap() }
    val timeJogador2 = time2.map { it.toMutableMap() }

    var atual1 = timeJogador1[0]
    var atual2 = timeJogador2[0]

    while (timeJogador1.any { it["hp"] as Int > 0 } && timeJogador2.any { it["hp"] as Int > 0 }) {
        Thread.sleep(1000)
        println("\n${atual1["nome"]} (HP: ${atual1["hp"]}) vs ${atual2["nome"]} (HP: ${atual2["hp"]})")

        if ((atual1["velocidade"] as Int) >= (atual2["velocidade"] as Int)) {
            Thread.sleep(1000)
            val dano = atacar(atual1, atual2, (atual1["tipos"] as List<String>)[0])
            println("${atual1["nome"]} causou $dano de dano em ${atual2["nome"]}!")

            if (atual2["hp"] as Int <= 0) {
                Thread.sleep(1000)
                println("${atual2["nome"]} foi derrotado!")
                atual2 = timeJogador2.find { it["hp"] as Int > 0 } ?: break
                continue
            }

            val dano2 = atacar(atual2, atual1, (atual2["tipos"] as List<String>)[0])
            println("${atual2["nome"]} causou $dano2 de dano em ${atual1["nome"]}!")
            if (atual1["hp"] as Int <= 0) {
                println("${atual1["nome"]} foi derrotado!")
                atual1 = timeJogador1.find { it["hp"] as Int > 0 } ?: break
            }

        } else {
            val dano = atacar(atual2, atual1, (atual2["tipos"] as List<String>)[0])
            println("${atual2["nome"]} causou $dano de dano em ${atual1["nome"]}!")
            if (atual1["hp"] as Int <= 0) {
                Thread.sleep(1000)
                println("${atual1["nome"]} foi derrotado!")
                atual1 = timeJogador1.find { it["hp"] as Int > 0 } ?: break
                continue
            }

            val dano2 = atacar(atual1, atual2, (atual1["tipos"] as List<String>)[0])
            println("${atual1["nome"]} causou $dano2 de dano em ${atual2["nome"]}!")
            if (atual2["hp"] as Int <= 0) {
                Thread.sleep(1000)
                println("${atual2["nome"]} foi derrotado!")
                atual2 = timeJogador2.find { it["hp"] as Int > 0 } ?: break
            }
        }
    }

    if (timeJogador1.any { it["hp"] as Int > 0 }) {
        println("\nVitória de $nomeJogador1!")
    } else {
        println("\nVitória de $nomeJogador2!")
    }
}
// realiza o cálculo do dano de um ataque entre dois Pokémon, levando em consideração atributos como ataque, defesa, velocidade, tipo do ataque, e eficiência de tipo.
fun atacar(pokemonAtacante: Map<String, Any>, pokemonDefensor: MutableMap<String, Any>, ataqueTipo: String): Int {
    val ataque = pokemonAtacante["ataque"] as Int
    val defesa = pokemonDefensor["defesa"] as Int
    val hpDefensor = pokemonDefensor["hp"] as Int
    val tiposDefesa = pokemonDefensor["tipos"] as List<String>
    val velocidadeAtacante = pokemonAtacante["velocidade"] as Int
    val velocidadeDefensor = pokemonDefensor["velocidade"] as Int

    val eficienciaTipo = calcularEficiencia(ataqueTipo, tiposDefesa)

    // leva em conta a relação entre o valor de ataque do atacante e o valor de defesa do defensor
    val danoBase = (ataque.toDouble() * (100.0 / (100 + defesa))).toInt()

    //o fator crítico é calculado com base em uma chance de 10% (probabilidade de um crítico ser acionado) que dobra o dano
    val fatorCritico = if (Random.nextDouble() < 0.1) 2.0 else 1.0

    //a chance de esquiva é baseada na diferença de velocidade entre os Pokémon e como isso pode evitar o ataquea chance de esquiva é baseada na diferença de velocidade entre os Pokémon e como isso pode evitar o ataque
    val chanceEsquiva = (velocidadeDefensor - velocidadeAtacante).coerceAtLeast(0) * 0.01
    val esquivou = Random.nextDouble() < chanceEsquiva
    if (esquivou) {
        println("${pokemonDefensor["nome"]} esquivou do ataque!")
        return 0
    }

    // dano final é multiplicado pela eficiência do tipo (calculada pela função calcularEficiencia) e pelo fator crítico (se houver)v
    val danoFinal = (danoBase * eficienciaTipo * fatorCritico).toInt()

    //o HP do defensor é reduzido pelo dano calculado, com um limite mínimo de 0 (não permitindo valores negativos para HP)
    pokemonDefensor["hp"] = (hpDefensor - danoFinal).coerceAtLeast(0)

    return danoFinal
}

//esta função calcula a eficácia do ataque, levando em conta o tipo do ataque e o tipo de defesa do Pokémon defensor.
fun calcularEficiencia(tipoAtaque: String, tiposDefesa: List<String>): Double {
    // Tabela de eficiências: ataque vs defesa (primeira geração)
    val tabelaEficacia = mapOf(
        "Normal" to mapOf(
            "Pedra" to 0.5,
            "Fantasma" to 0.0,
            "Aco" to 0.5
        ),
        "Fogo" to mapOf(
            "Planta" to 2.0,
            "Gelo" to 2.0,
            "Inseto" to 2.0,
            "Aco" to 2.0,
            "Fogo" to 0.5,
            "Agua" to 0.5,
            "Pedra" to 0.5,
            "Dragao" to 0.5
        ),
        "Agua" to mapOf(
            "Fogo" to 2.0,
            "Pedra" to 2.0,
            "Terra" to 2.0,
            "Agua" to 0.5,
            "Planta" to 0.5,
            "Dragao" to 0.5
        ),
        "Eletrico" to mapOf(
            "Agua" to 2.0,
            "Voador" to 2.0,
            "Eletrico" to 0.5,
            "Planta" to 0.5,
            "Dragao" to 0.5,
            "Terra" to 0.0
        ),
        "Planta" to mapOf(
            "Agua" to 2.0,
            "Terra" to 2.0,
            "Pedra" to 2.0,
            "Fogo" to 0.5,
            "Planta" to 0.5,
            "Voador" to 0.5,
            "Inseto" to 0.5,
            "Dragao" to 0.5,
            "Veneno" to 0.5,
            "Aco" to 0.5
        ),
        "Gelo" to mapOf(
            "Planta" to 2.0,
            "Terra" to 2.0,
            "Voador" to 2.0,
            "Dragao" to 2.0,
            "Fogo" to 0.5,
            "Agua" to 0.5,
            "Gelo" to 0.5,
            "Aco" to 0.5
        ),
        "Lutador" to mapOf(
            "Normal" to 2.0,
            "Gelo" to 2.0,
            "Pedra" to 2.0,
            "Aco" to 2.0,
            "Sombrio" to 2.0,
            "Veneno" to 0.5,
            "Voador" to 0.5,
            "Psiquico" to 0.5,
            "Inseto" to 0.5,
            "Fantasma" to 0.0,
            "Fada" to 0.5
        ),
        "Veneno" to mapOf(
            "Planta" to 2.0,
            "Fada" to 2.0,
            "Veneno" to 0.5,
            "Terra" to 0.5,
            "Pedra" to 0.5,
            "Fantasma" to 0.5,
            "Aco" to 0.0
        ),
        "Terra" to mapOf(
            "Fogo" to 2.0,
            "Eletrico" to 2.0,
            "Veneno" to 2.0,
            "Pedra" to 2.0,
            "Aco" to 2.0,
            "Planta" to 0.5,
            "Inseto" to 0.5,
            "Voador" to 0.0
        ),
        "Voador" to mapOf(
            "Planta" to 2.0,
            "Lutador" to 2.0,
            "Inseto" to 2.0,
            "Eletrico" to 0.5,
            "Pedra" to 0.5,
            "Aco" to 0.5
        ),
        "Psiquico" to mapOf(
            "Lutador" to 2.0,
            "Veneno" to 2.0,
            "Psiquico" to 0.5,
            "Aco" to 0.5,
            "Sombrio" to 0.0
        ),
        "Inseto" to mapOf(
            "Planta" to 2.0,
            "Psiquico" to 2.0,
            "Sombrio" to 2.0,
            "Fogo" to 0.5,
            "Lutador" to 0.5,
            "Voador" to 0.5,
            "Fantasma" to 0.5,
            "Aco" to 0.5,
            "Fada" to 0.5
        ),
        "Pedra" to mapOf(
            "Fogo" to 2.0,
            "Gelo" to 2.0,
            "Voador" to 2.0,
            "Inseto" to 2.0,
            "Lutador" to 0.5,
            "Terra" to 0.5,
            "Aco" to 0.5
        ),
        "Fantasma" to mapOf(
            "Psiquico" to 2.0,
            "Fantasma" to 2.0,
            "Normal" to 0.0,
            "Lutador" to 0.0,
            "Sombrio" to 0.5
        ),
        "Dragao" to mapOf(
            "Dragao" to 2.0,
            "Aco" to 0.5,
            "Fada" to 0.0
        ),
        "Sombrio" to mapOf(
            "Psiquico" to 2.0,
            "Fantasma" to 2.0,
            "Lutador" to 0.5,
            "Sombrio" to 0.5,
            "Fada" to 0.5
        ),
        "Aco" to mapOf(
            "Gelo" to 2.0,
            "Pedra" to 2.0,
            "Fada" to 2.0,
            "Fogo" to 0.5,
            "Agua" to 0.5,
            "Eletrico" to 0.5,
            "Aco" to 0.5
        ),
        "Fada" to mapOf(
            "Lutador" to 2.0,
            "Dragao" to 2.0,
            "Sombrio" to 2.0,
            "Fogo" to 0.5,
            "Veneno" to 0.5,
            "Aco" to 0.5
        )
    )




    var eficiencia = 1.0
    for (tipo in tiposDefesa) {
        eficiencia *= tabelaEficacia.getOrDefault(tipoAtaque, mapOf())[tipo] ?: 1.0
    }

    return eficiencia
}
