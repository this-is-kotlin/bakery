package bakery.async.concurrent

import bakery.ColdOven
import bakery.FinishedPretzel
import bakery.async.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


fun main() = runBlocking {
    //warm up
    List(10_000) {
        async(Dispatchers.Default) {
            bakePretzels()
        }
    }.awaitAll()

    val time = measureTimeMillis {
        val bakery = List(100_000) {
            async(Dispatchers.Default) {
                bakePretzels()
            }
        }
        bakery.awaitAll()
    }

    println("baking 500.000 pretzels took: $time ms")
}

suspend fun bakePretzels(): List<FinishedPretzel> = coroutineScope {
    val oven = async { preheatOven(ColdOven) }
    val dough = async { prepareDough() }
    val uncookedPretzels = List(5) { async { shapePretzel(dough.await()) } }
    val bakedPretzels = async { bake(oven.await(), uncookedPretzels.awaitAll()) }
    val topping = async { prepareTopping() }
    bakedPretzels.await().map { finishPretzel(it, topping.await()) }
}
