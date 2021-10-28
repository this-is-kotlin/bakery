package bakery.async.sequential

import bakery.*
import bakery.async.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {
    //warm up
    List(10_000) {
        launch {
            bakePretzels()
        }
    }.joinAll()

    val time = measureTimeMillis {
        List(100_000) {
            async(Dispatchers.Default) {
                bakePretzels()
            }
        }.awaitAll()
    }

    println("baking 500.000 pretzels took: $time ms")
}

suspend fun bakePretzels(): List<FinishedPretzel> {
    val oven = preheatOven(ColdOven)
    val dough = prepareDough()
    val shapedPretzels: List<UncookedPretzel> = List(5) { shapePretzel(dough) }
    val bakedPretzels: List<CookedPretzel> = bake(oven, shapedPretzels)
    val topping: Topping = prepareTopping()
    return bakedPretzels.map { finishPretzel(it, topping) }
}
