package bakery.async

import bakery.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() = runBlocking {
    val shelf = bakeryPipeline()
    val job = cashDesk(shelf)
    delay(hours(8))
    shelf.cancel()
    job.cancelAndJoin()
}


fun CoroutineScope.bakeryPipeline() =
    produce<Pretzel>(context = Dispatchers.Default, capacity = 20) {
        while (isActive) {
            println("Start producing")
            bakePretzels().forEach {
                send(it)
                println("$it sent to shelf")
            }
        }
    }

fun CoroutineScope.cashDesk(shelf: ReceiveChannel<Pretzel>) = launch(Dispatchers.IO) {
    while (isActive) {
        println("How many pretzels?")
        val count = readLine()?.toIntOrNull()
        if (count != null) {
            (1..count).map { shelf.receive() }
                .forEach {
                    print("Here's your pretzel: $it")
                }
        } else {
            println("Command not recognized")
        }
    }
}

suspend fun bakePretzels(): List<FinishedPretzel> = coroutineScope {
    val oven = async { preheatOven(ColdOven) }
    val dough = async {
        println("starting dough preparation")
        delay(1000)
        println("preparing dough")
        prepareDough()
    }
    val shapedPretzels: List<UncookedPretzel> = List(5) { shapePretzel(dough.await()) }
    val bakedPretzels: List<CookedPretzel> = bake(oven.await(), shapedPretzels)
    val topping: Topping = prepareTopping()
    bakedPretzels.map { finishPretzel(it, topping) }
}

