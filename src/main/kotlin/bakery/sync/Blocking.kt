package bakery.sync

import bakery.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        thread {
            bakePretzels()
        }.join()
    }
    println("baking 5 pretzels took: $time ms")
}

fun bakePretzels(): List<FinishedPretzel> {
    val hotOven = preheatOven(ColdOven)
    val dough = prepareDough()
    val shapedPretzels: List<UncookedPretzel> = List(5) { shapePretzel(dough) }
    val bakedPretzels: List<CookedPretzel> = bake(hotOven, shapedPretzels)
    val topping: Topping = prepareTopping()
    return bakedPretzels.map { addTopping(it, topping) }
}

fun prepareDough(): Dough {
    Thread.sleep(PREPARE_DOUGH)
    return Dough
}

fun shapePretzel(dough: Dough): UncookedPretzel {
    Thread.sleep(SHAPE_PRETZEL)
    return UncookedPretzel
}

fun preheatOven(oven: ColdOven): HotOven {
    Thread.sleep(PREHEAT_OVEN)
    return HotOven
}

fun bake(oven: HotOven, pretzels: List<UncookedPretzel>): List<CookedPretzel> {
    Thread.sleep(BAKE)
    return pretzels.map { CookedPretzel }
}

fun prepareTopping(): Topping {
    Thread.sleep(PREPARE_TOPPING)
    return Topping
}

fun addTopping(pretzel: CookedPretzel, topping: Topping): FinishedPretzel {
    Thread.sleep(ADD_TOPPING)
    return FinishedPretzel
}
