package bakery.async

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import bakery.*

suspend fun prepareDough(): Dough {
    delay(PREPARE_DOUGH)
    return Dough
}

suspend fun shapePretzel(dough: Dough): UncookedPretzel {
    delay(SHAPE_PRETZEL)
    return UncookedPretzel
}

suspend fun preheatOven(oven: Oven): HotOven = withContext(Dispatchers.IO) {
    when (oven) {
        is ColdOven -> {
            delay(PREHEAT_OVEN) //network call or file operations
            HotOven
        }
        is HotOven -> oven
    }
}

suspend fun bake(oven: HotOven, pretzels: List<UncookedPretzel>): List<CookedPretzel> {
    delay(BAKE)
    return pretzels.map { CookedPretzel }
}

suspend fun prepareTopping(): Topping {
    delay(PREPARE_TOPPING)
    return Topping
}

suspend fun finishPretzel(pretzel: CookedPretzel, topping: Topping): FinishedPretzel {
    delay(ADD_TOPPING)
    return FinishedPretzel
}
