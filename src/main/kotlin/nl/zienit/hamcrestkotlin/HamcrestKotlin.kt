package nl.zienit.hamcrestkotlin

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import java.math.BigDecimal
import kotlin.reflect.KClass

@DslMarker
annotation class HamcrestKotlinMarker

object it {}

abstract class CommonMatchers<M : CommonMatchers<M>> {

    abstract val assertions: MutableList<Matcher<Any>>

    abstract fun eval(body: M.() -> Unit, factory: (M) -> Matcher<Any>)

    fun allOf(body: M.() -> Unit) {
        eval(body, { Matchers.allOf(it.assertions) })
    }

    fun both(body: M.() -> Unit) {
        eval(body, { Matchers.both(it.assertions.get(0)).and(it.assertions.get(1)) })
    }

    fun either(body: M.() -> Unit) {
        eval(body, { Matchers.either(it.assertions.get(0)).or(it.assertions.get(1)) })
    }

    fun not(body: M.() -> Unit) {
        eval(body, { Matchers.not(it.assertions.first()) })
    }

    fun anything() {
        eval({}, { Matchers.anything() as Matcher<Any> })
    }
}

@HamcrestKotlinMarker
class RegularMatchers<T>() : CommonMatchers<RegularMatchers<T>>(), IterableMatchersBase<Any>, MapMatchersBase<Any, Any>, ArrayMatchersBase<Any> {

    override val assertions: MutableList<Matcher<Any>> = mutableListOf()

    override fun eval(body: RegularMatchers<T>.() -> Unit, factory: (RegularMatchers<T>) -> Matcher<Any>) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(factory(m))
    }

    private fun add(matcher: Matcher<Any>) {
        assertions.add(matcher as Matcher<Any>)
    }

    infix fun it.equalTo(expected: T?) {
        add(Matchers.equalTo(expected))
    }

    infix fun it.containsString(expected: String) {
        add(Matchers.containsString(expected) as Matcher<Any>)
    }

    infix fun it.instanceOf(value: KClass<out Any>) {
        add(Matchers.instanceOf<Class<Any>>(value.java) as Matcher<Any>)
    }

    fun blankOrNullString() {
        add(Matchers.blankOrNullString() as Matcher<Any>)
    }

    fun blankString() {
        add(Matchers.blankString() as Matcher<Any>)
    }

    fun closeTo(expected: BigDecimal, error: BigDecimal) {
        add(Matchers.closeTo(expected, error) as Matcher<Any>)
    }

    fun closeTo(expected: Double, error: Double) {
        add(Matchers.closeTo(expected, error) as Matcher<Any>)
    }

    infix fun it.hasProperty(name: String) {
        add(Matchers.hasProperty<T>(name) as Matcher<Any>)
    }

    fun hasProperty(name: String, body: RegularMatchers<T>.() -> Unit) {
        eval(body, { Matchers.hasProperty(name, it.assertions.first()) })
    }

    infix fun it.lessThan(expected: T) {
        add(Matchers.lessThan(expected as Comparable<Any>) as Matcher<Any>)
    }

    infix fun it.lessThanOrEqualTo(expected: T) {
        add(Matchers.lessThanOrEqualTo(expected as Comparable<Any>) as Matcher<Any>)
    }

    infix fun it.greaterThan(expected: T) {
        add(Matchers.greaterThan(expected as Comparable<Any>) as Matcher<Any>)
    }

    infix fun it.greaterThanOrEqualTo(expected: T) {
        add(Matchers.greaterThanOrEqualTo(expected as Comparable<Any>) as Matcher<Any>)
    }
}

interface IterableMatchersBase<T> {

    val assertions: MutableList<Matcher<Any>>

    infix fun it.hasItem(expected: T?) {
        assertions.add(Matchers.hasItem(expected) as Matcher<Any>)
    }

    infix fun it.hasItem(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.hasItem(m.assertions.first()) as Matcher<Any>)
    }

    infix fun it.hasItems(expected: Array<out T>) {
        assertions.add(Matchers.hasItems(*expected) as Matcher<Any>)
    }

    infix fun it.everyItem(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.everyItem(m.assertions.first()) as Matcher<Any>)
    }
}

@HamcrestKotlinMarker
class IterableMatchers<T>() : CommonMatchers<IterableMatchers<T>>(), IterableMatchersBase<T> {

    override val assertions: MutableList<Matcher<Any>> = mutableListOf()

    override fun eval(body: IterableMatchers<T>.() -> Unit, factory: (IterableMatchers<T>) -> Matcher<Any>) {
        val m = IterableMatchers<T>()
        m.body()
        assertions.add(factory(m))
    }
}

interface MapMatchersBase<K, V> {

    val assertions: MutableList<Matcher<Any>>

    infix fun it.hasEntry(expected: Pair<K, V>) {
        assertions.add(Matchers.hasEntry(expected.first, expected.second) as Matcher<Any>)
    }

    fun it.hasEntry(keyBody: RegularMatchers<K>.() -> Unit, valueBody: RegularMatchers<V>.() -> Unit) {
        val key = RegularMatchers<K>()
        key.keyBody()
        val value = RegularMatchers<V>()
        value.valueBody()
        assertions.add(Matchers.hasEntry(key.assertions.first(), value.assertions.first()) as Matcher<Any>)
    }

    infix fun it.hasKey(expected: K) {
        assertions.add(Matchers.hasKey(expected) as Matcher<Any>)
    }

    infix fun it.hasKey(body: RegularMatchers<K>.() -> Unit) {
        val m = RegularMatchers<K>()
        m.body()
        assertions.add(Matchers.hasKey(m.assertions.first()) as Matcher<Any>)
    }

    infix fun it.hasValue(expected: V) {
        assertions.add(Matchers.hasValue(expected) as Matcher<Any>)
    }

    infix fun it.hasValue(body: RegularMatchers<V>.() -> Unit) {
        val m = RegularMatchers<V>()
        m.body()
        assertions.add(Matchers.hasValue(m.assertions.first()) as Matcher<Any>)
    }
}

@HamcrestKotlinMarker
class MapMatchers<K, V>() : CommonMatchers<MapMatchers<K, V>>(), MapMatchersBase<K, V> {

    override val assertions: MutableList<Matcher<Any>> = mutableListOf()

    override fun eval(body: MapMatchers<K, V>.() -> Unit, factory: (MapMatchers<K, V>) -> Matcher<Any>) {
        val m = MapMatchers<K, V>()
        m.body()
        assertions.add(factory(m))
    }

    private fun add(matcher: Matcher<Any>) {
        assertions.add(matcher)
    }
}

interface ArrayMatchersBase<T> {

    val assertions: MutableList<Matcher<Any>>

    infix fun it.arrayContaining(actuals: Array<out T>) {
        assertions.add(Matchers.arrayContaining(*actuals) as Matcher<Any>)
    }

    infix fun it.arrayContaining(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.arrayContaining(*m.assertions.toTypedArray()) as Matcher<Any>)
    }

    infix fun it.arrayContainingInAnyOrder(actuals: Array<out T>) {
        assertions.add(Matchers.arrayContainingInAnyOrder(*actuals) as Matcher<Any>)
    }

    infix fun it.arrayContainingInAnyOrder(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.arrayContainingInAnyOrder(*m.assertions.toTypedArray()) as Matcher<Any>)
    }

    infix fun it.arrayWithSize(actual: Int) {
        assertions.add(Matchers.arrayWithSize<T>(actual) as Matcher<Any>)
    }

    infix fun it.arrayWithSize(body: RegularMatchers<Int>.() -> Unit) {
        val m = RegularMatchers<Int>()
        m.body()
        assertions.add(Matchers.arrayWithSize<T>(m.assertions.first()) as Matcher<Any>)
    }
}

@HamcrestKotlinMarker
class ArrayMatchers<T>() : CommonMatchers<ArrayMatchers<T>>(), ArrayMatchersBase<T> {

    override val assertions: MutableList<Matcher<Any>> = mutableListOf()

    override fun eval(body: ArrayMatchers<T>.() -> Unit, factory: (ArrayMatchers<T>) -> Matcher<Any>) {
        val m = ArrayMatchers<T>()
        m.body()
        assertions.add(factory(m))
    }
}


class RegularAssert<T>(val actual: T?) {}

infix fun <T> RegularAssert<T>.assertThat(body: RegularMatchers<T>.() -> Unit) {
    val m = RegularMatchers<T>();
    m.body()
    m.assertions.forEach { matcher -> MatcherAssert.assertThat(actual, matcher as Matcher<in T?>) }
}

class IterableAssert<T>(val actual: Iterable<T>?) {}

infix fun <T> IterableAssert<T>.assertThat(body: IterableMatchers<T>.() -> Unit) {
    val m = IterableMatchers<T>();
    m.body()
    m.assertions.forEach { matcher -> MatcherAssert.assertThat(actual, matcher as Matcher<in Iterable<T?>?>) }
}

class MapAssert<K, V>(val actual: Map<K, V>?) {}

infix fun <K, V> MapAssert<K, V>.assertThat(body: MapMatchers<K, V>.() -> Unit) {
    val m = MapMatchers<K, V>();
    m.body()
    m.assertions.forEach { matcher -> MatcherAssert.assertThat(actual, matcher as Matcher<in Map<K, V?>?>) }
}

class ArrayAssert<T>(val actual: Array<T>?) {}

infix fun <T> ArrayAssert<T>.assertThat(body: ArrayMatchers<T>.() -> Unit) {
    val m = ArrayMatchers<T>();
    m.body()
    m.assertions.forEach { matcher -> MatcherAssert.assertThat(actual, matcher as Matcher<in Array<T>?>) }
}

fun <T> on(actual: T): RegularAssert<T> = RegularAssert(actual)
fun <T> on(actual: Iterable<T>?): IterableAssert<T> = IterableAssert(actual)
fun <K, V> on(actual: Map<K, V>?): MapAssert<K, V> = MapAssert(actual)
fun <T> on(actual: Array<T>?): ArrayAssert<T> = ArrayAssert(actual)