package nl.zienit.hamcrestkotlin

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import java.math.BigDecimal
import java.util.regex.Pattern
import kotlin.reflect.KClass

@DslMarker
annotation class HamcrestKotlinMarker

object it {}

abstract class CommonMatchers<M : CommonMatchers<M>> {

    abstract val assertions: MutableList<Matcher<Any>>

    abstract fun eval(body: M.() -> Unit, factory: (M) -> Matcher<Any>)

    fun squash(): Matcher<Any> =
            when (assertions.size) {
                0 -> Matchers.anything()
                1 -> assertions.first()
                else -> Matchers.allOf(assertions)
            }

    fun allOf(body: M.() -> Unit) {
        eval(body, { Matchers.allOf(it.assertions) })
    }

    fun anyOf(body: M.() -> Unit) {
        eval(body, { Matchers.anyOf(it.assertions) })
    }

    fun both(body: M.() -> Unit) {
        eval(body, { Matchers.both(it.assertions.get(0)).and(it.assertions.get(1)) })
    }

    fun either(body: M.() -> Unit) {
        eval(body, { Matchers.either(it.assertions.get(0)).or(it.assertions.get(1)) })
    }

    fun not(body: M.() -> Unit) {
        eval(body, { Matchers.not(it.squash()) })
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

    infix fun it.any(value: KClass<out Any>) {
        add(Matchers.any(value.java) as Matcher<Any>)
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

    infix fun it.comparesEqualTo(expected: T?) {
        add(Matchers.comparesEqualTo(expected as Comparable<Any>) as Matcher<Any>)
    }

    infix fun it.containsString(expected: String) {
        add(Matchers.containsString(expected) as Matcher<Any>)
    }

    infix fun it.containsStringIgnoringCase(expected: String) {
        add(Matchers.containsStringIgnoringCase(expected) as Matcher<Any>)
    }

    fun emptyOrNullString() {
        add(Matchers.emptyOrNullString() as Matcher<Any>)
    }

    fun emptyString() {
        add(Matchers.emptyString() as Matcher<Any>)
    }

    infix fun it.endsWith(expected: String) {
        add(Matchers.endsWith(expected) as Matcher<Any>)
    }

    infix fun it.endsWithIgnoringCase(expected: String) {
        add(Matchers.endsWithIgnoringCase(expected) as Matcher<Any>)
    }

    infix fun it.equalTo(expected: T?) {
        add(Matchers.equalTo(expected))
    }

    infix fun it.equalToCompressingWhiteSpace(expected: String) {
        add(Matchers.equalToCompressingWhiteSpace(expected) as Matcher<Any>)
    }

    infix fun it.equalToIgnoringCase(expected: String) {
        add(Matchers.equalToIgnoringCase(expected) as Matcher<Any>)
    }

    infix fun it.equalToObject(expected: Any) {
        add(Matchers.equalToObject(expected))
    }

    infix fun it.hasLength(expected: Int) {
        add(Matchers.hasLength(expected) as Matcher<Any>)
    }

    infix fun it.hasToString(expected: String) {
        add(Matchers.hasToString<T>(expected) as Matcher<Any>)
    }

    fun hasToString(body: RegularMatchers<T>.() -> Unit) {
        eval(body, { Matchers.hasToString(it.squash()) })
    }

    fun `in`(vararg actual : T) {
        add(Matchers.`in`(actual) as Matcher<Any>)
    }

    fun `in`(actual : Collection<T>) {
        add(Matchers.`in`(actual) as Matcher<Any>)
    }

    infix fun it.instanceOf(value: KClass<out Any>) {
        add(Matchers.instanceOf<Class<Any>>(value.java) as Matcher<Any>)
    }

    infix fun it.isA(value: KClass<out Any>) {
        add(Matchers.isA<Class<Any>>(value.java) as Matcher<Any>)
    }

    infix fun it.matchesPattern(pattern: Pattern) {
        add(Matchers.matchesPattern(pattern) as Matcher<Any>)
    }

    infix fun it.matchesPattern(regex: String) {
        add(Matchers.matchesPattern(regex) as Matcher<Any>)
    }

    infix fun it.matchesRegex(pattern: Pattern) {
        add(Matchers.matchesRegex(pattern) as Matcher<Any>)
    }

    infix fun it.matchesRegex(regex: String) {
        add(Matchers.matchesRegex(regex) as Matcher<Any>)
    }

    infix fun it.not(actual: T) {
        add(Matchers.not(actual))
    }

    infix fun it.hasProperty(name: String) {
        add(Matchers.hasProperty<T>(name) as Matcher<Any>)
    }

    fun hasProperty(name: String, body: RegularMatchers<Any>.() -> Unit) {
        eval(body as RegularMatchers<T>.() -> Unit, { Matchers.hasProperty(name, it.squash()) })
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

    infix fun it.sameInstance(expected: T) {
        add(Matchers.sameInstance(expected));
    }

    infix fun it.startsWith(expected: String) {
        add(Matchers.startsWith(expected) as Matcher<Any>)
    }

    infix fun it.startsWithIgnoringCase(expected: String) {
        add(Matchers.startsWithIgnoringCase(expected) as Matcher<Any>)
    }

    fun stringContainsInOrder(vararg substrings: String) {
        add(Matchers.stringContainsInOrder(*substrings) as Matcher<Any>)
    }

    infix fun it.stringContainsInOrder(substrings: Iterable<String>) {
        add(Matchers.stringContainsInOrder(substrings) as Matcher<Any>)
    }

    infix fun it.theInstance(expected: T) {
        add(Matchers.theInstance(expected));
    }
}

interface IterableMatchersBase<T> {

    val assertions: MutableList<Matcher<Any>>

    fun contains(vararg expected: T) {
        assertions.add(Matchers.contains(*expected) as Matcher<Any>)
    }

    fun contains(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.contains(m.assertions) as Matcher<Any>)
    }

    fun containsInAnyOrder(vararg expected: T) {
        assertions.add(Matchers.containsInAnyOrder(*expected) as Matcher<Any>)
    }

    fun containsInAnyOrder(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.containsInAnyOrder(m.assertions) as Matcher<Any>)
    }

    fun containsInRelativeOrder(vararg expected: T) {
        assertions.add(Matchers.containsInRelativeOrder(*expected) as Matcher<Any>)
    }

    fun containsInRelativeOrder(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.containsInRelativeOrder(m.assertions) as Matcher<Any>)
    }

    fun empty() {
        assertions.add(Matchers.empty<T>() as Matcher<Any>)
    }

    infix fun everyItem(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.everyItem(m.squash()) as Matcher<Any>)
    }

    infix fun it.hasItem(expected: T?) {
        assertions.add(Matchers.hasItem(expected) as Matcher<Any>)
    }

    fun hasItem(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.hasItem(m.squash()) as Matcher<Any>)
    }

    fun hasItems(vararg expected: T) {
        assertions.add(Matchers.hasItems(*expected) as Matcher<Any>)
    }

    infix fun it.hasSize(expected: Int) {
        assertions.add(Matchers.hasSize<T>(expected) as Matcher<Any>)
    }

    fun hasSize(body: RegularMatchers<Int>.() -> Unit) {
        val m = RegularMatchers<Int>()
        m.body()
        assertions.add(Matchers.hasSize<T>(m.squash()) as Matcher<Any>)
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
        assertions.add(Matchers.hasEntry(key.squash(), value.squash()) as Matcher<Any>)
    }

    infix fun it.hasKey(expected: K) {
        assertions.add(Matchers.hasKey(expected) as Matcher<Any>)
    }

    fun hasKey(body: RegularMatchers<K>.() -> Unit) {
        val m = RegularMatchers<K>()
        m.body()
        assertions.add(Matchers.hasKey(m.squash()) as Matcher<Any>)
    }

    infix fun it.hasValue(expected: V) {
        assertions.add(Matchers.hasValue(expected) as Matcher<Any>)
    }

    fun hasValue(body: RegularMatchers<V>.() -> Unit) {
        val m = RegularMatchers<V>()
        m.body()
        assertions.add(Matchers.hasValue(m.squash()) as Matcher<Any>)
    }

    infix fun it.aMapWithSize(expected: Int) {
        assertions.add(Matchers.aMapWithSize<K, V>(expected) as Matcher<Any>)
    }

    fun aMapWithSize(body: RegularMatchers<Int>.() -> Unit) {
        val m = RegularMatchers<Int>()
        m.body()
        assertions.add(Matchers.aMapWithSize<K, V>(m.squash()) as Matcher<Any>)
    }

    fun anEmptyMap() {
        assertions.add(Matchers.anEmptyMap<K, V>() as Matcher<Any>)
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

    fun arrayContaining(vararg actuals: T) {
        assertions.add(Matchers.arrayContaining(*actuals) as Matcher<Any>)
    }

    fun arrayContaining(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.arrayContaining(*m.assertions.toTypedArray()) as Matcher<Any>)
    }

    fun arrayContainingInAnyOrder(vararg actuals: T) {
        assertions.add(Matchers.arrayContainingInAnyOrder(*actuals) as Matcher<Any>)
    }

    fun arrayContainingInAnyOrder(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.arrayContainingInAnyOrder(*m.assertions.toTypedArray()) as Matcher<Any>)
    }

    infix fun it.arrayWithSize(actual: Int) {
        assertions.add(Matchers.arrayWithSize<T>(actual) as Matcher<Any>)
    }

    fun arrayWithSize(body: RegularMatchers<Int>.() -> Unit) {
        val m = RegularMatchers<Int>()
        m.body()
        assertions.add(Matchers.arrayWithSize<T>(m.squash()) as Matcher<Any>)
    }

    fun emptyArray() {
        assertions.add(Matchers.emptyArray<T>() as Matcher<Any>)
    }

    infix fun it.hasItemInArray(expected: T?) {
        assertions.add(Matchers.hasItemInArray(expected) as Matcher<Any>)
    }

    fun hasItemInArray(body: RegularMatchers<T>.() -> Unit) {
        val m = RegularMatchers<T>()
        m.body()
        assertions.add(Matchers.hasItemInArray(m.squash()) as Matcher<Any>)
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

fun <T> examine(actual: T): RegularAssert<T> = RegularAssert(actual)
fun <T> examine(actual: Iterable<T>?): IterableAssert<T> = IterableAssert(actual)
fun <K, V> examine(actual: Map<K, V>?): MapAssert<K, V> = MapAssert(actual)
fun <T> examine(actual: Array<T>?): ArrayAssert<T> = ArrayAssert(actual)