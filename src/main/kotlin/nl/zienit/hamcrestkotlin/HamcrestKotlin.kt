package nl.zienit.hamcrestkotlin

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class MyMatchers {

    val assertions: MutableList<Pair<Any?, Matcher<out Any>>> = mutableListOf()

    private fun Any?.eval(body: MyMatchers.(Any?) -> Unit, creator: (MyMatchers) -> Matcher<out Any>) {
        val m = MyMatchers()
        m.body(this)
        if (m.assertions.any { (a, _) -> a !== this }) {
            throw IllegalArgumentException("actuals in the body must be the same as the top level actual. Simply use 'it'")
        }
        assertions.add(this to creator(m))
    }

    private fun Any?.eval(creator: () -> Matcher<out Any>) {
        assertions.add(this to creator())
    }

    infix fun Any?.equalTo(value: Any?) {
        eval { if (value == null) Matchers.nullValue() else Matchers.equalTo(value) }
    }

    infix fun Any?.hasItem(value: Any) {
        eval { Matchers.hasItem(value) }
    }

    infix fun Any?.hasItems(values: Array<out Any>) {
        eval { Matchers.hasItems(*values) }
    }

    infix fun Any?.hasEntry(entry: Pair<Any, Any>) {
        eval { Matchers.hasEntry(entry.first, entry.second) }
    }

    infix fun Any?.containsString(value: Any) {
        eval { Matchers.containsString(value as String) }
    }

    infix fun Any?.instanceOf(value: KClass<out Any>) {
        eval { Matchers.instanceOf<Class<Any>>(value.java) }
    }

    infix fun Any?.not(body: MyMatchers.(Any?) -> Unit) {
        eval(body) { Matchers.not(it.assertions.map { (_, m) -> m }.first()) }
    }

    infix fun Any?.hasItem(body: MyMatchers.(Any?) -> Unit) {
        eval(body) { Matchers.hasItem(it.assertions.map { (_, m) -> m }.first()) }
    }

    infix fun Any?.allOf(body: MyMatchers.(Any?) -> Unit) {
        eval(body) { Matchers.allOf(it.assertions.map { (_, m) -> m }) }
    }

    infix fun Any?.anyOf(body: MyMatchers.(Any?) -> Unit) {
        eval(body) { Matchers.anyOf(it.assertions.map { (_, m) -> m }) }
    }

    infix fun String.describing(body: MyMatchers.(Any?) -> Unit) {
        val m = MyMatchers()
        m.body(this)
        m.assertions.forEach { (actual, matcher) ->
            assertions.add(actual to Matchers.describedAs(this, matcher))
        }
    }
}

fun assertThat(body: MyMatchers.() -> Unit) {
    val m = MyMatchers();
    m.body()
    m.assertions.forEach { (actual, matcher) -> MatcherAssert.assertThat(actual, matcher as Matcher<in Any?>) }
}
