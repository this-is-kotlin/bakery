package bakery

object Topping

object Dough

sealed interface Oven
object ColdOven : Oven
object HotOven : Oven

sealed interface Pretzel
object UncookedPretzel : Pretzel
object CookedPretzel : Pretzel
object FinishedPretzel : Pretzel {
    override fun toString(): String = "FinishedPretzel"
}
