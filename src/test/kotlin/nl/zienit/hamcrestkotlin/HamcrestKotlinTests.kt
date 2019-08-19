package nl.zienit.hamcrestkotlin;

import org.junit.Test
import java.math.BigDecimal

class HamcrestKotlinTests {

    @Test
    fun testEqualTo() {
        val y = "hello"

        examine(y) assertThat {
            it equalTo "hello"
        }
    }

    @Test(expected = AssertionError::class)
    fun testEqualToFailure() {
        val y = "hello"

        examine(y) assertThat {
            it equalTo "hi"
        }
    }

    @Test
    fun testhasItem() {
        val x = listOf(1, 2, 3, 4)
        examine(x) assertThat {
            it hasItem 4
        }
    }

    @Test
    fun testhasItemThat() {
        val x = listOf(1, 2, 3, 4)

        examine(x) assertThat {
            hasItem { it equalTo 3 }
        }
    }

    @Test(expected = AssertionError::class)
    fun testhasItemFailure() {
        val x = listOf(1, 2, 3, 4)

        examine(x) assertThat {
            it hasItem 5
        }
    }

    @Test
    fun testhasItemMatcher() {
        val x = listOf("foo", "bar")

        examine(x) assertThat {
            hasItem { it containsString "oo" }
        }
    }

    @Test(expected = AssertionError::class)
    fun testhasItemMatcherFailure() {
        val x = listOf("foo", "bar")

        examine(x) assertThat {
            hasItem { it containsString "xx" }
        }
    }

    @Test
    fun testContainsString() {
        val y = "hello"

        examine(y) assertThat {
            it containsString "ll"
        }
    }

    @Test(expected = AssertionError::class)
    fun testContainsStringFailure() {
        val y = "hello"

        examine(y) assertThat {
            it containsString "xx"
        }
    }

    @Test
    fun testHasEntry() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        examine(y) assertThat {
            it hasEntry (2 to "two")
        }

        examine(y) assertThat {
            it hasKey 2
        }

        examine(y) assertThat {
            it hasValue "two"
        }
    }

    @Test
    fun testHasEntryMatcher() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        examine(y) assertThat {
            it.hasEntry({
                it equalTo 2
            }, {
                it equalTo "two"
            })
        }

        examine(y) assertThat {
            hasKey { it equalTo 2 }
        }

        examine(y) assertThat {
            hasValue { it containsString "two" }
        }
    }

    @Test
    fun testHasEntryAll() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        examine(y) assertThat {
            allOf {
                hasKey { it equalTo 2 }
                hasValue { it containsString "two" }
            }
        }
    }

    @Test(expected = AssertionError::class)
    fun testHasEntryFailure() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        examine(y) assertThat {
            it hasEntry (4 to "four")
        }
    }

    @Test
    fun testInstanceOf() {
        val x = 5

        examine(x) assertThat {
            it instanceOf Int::class
        }
    }

    @Test(expected = AssertionError::class)
    fun testInstanceOfFailure() {
        val x = 5

        examine(x) assertThat {
            it instanceOf String::class
        }
    }

    @Test
    fun testAllOf() {
        val t = listOf("foo", "bar")

        examine(t) assertThat {
            allOf {
                it hasItem "foo"
                it hasItem "bar"
            }
        }
    }

    @Test(expected = AssertionError::class)
    fun testNullActual() {
        val l: Int? = null

        examine(l) assertThat { it equalTo 3 }
    }

    @Test(expected = AssertionError::class)
    fun testNullExpected() {
        val l = 3

        examine(l) assertThat { it equalTo null }
    }

    @Test(expected = AssertionError::class)
    fun testNullIterable() {
        val l: List<Int?>? = null

        examine(l) assertThat { it hasItem 3 }
    }

    @Test(expected = AssertionError::class)
    fun testNullMap() {
        val l: Map<Int, String?>? = null

        examine(l) assertThat { it hasEntry (3 to "foo") }
    }

    @Test
    fun testBoth() {
        val s = "foo"

        examine(s) assertThat {
            both {
                it containsString "oo"
                it containsString "fo"
            }
        }
    }

    @Test
    fun testEither() {
        val s = "foo"

        examine(s) assertThat {
            either {
                it containsString "oo"
                it containsString "xx"
            }
        }
    }

    @Test
    fun testEveryItem() {
        val l = listOf("foo", "far")

        examine(l) assertThat {
            everyItem {
                it containsString "f"
            }
        }
    }

    @Test
    fun testAnything() {
        val l = listOf("foo", "far")

        examine(l) assertThat {
            anything()
        }
    }

    @Test(expected = AssertionError::class)
    fun testAnythingFailure() {
        val l = listOf("foo", "far")

        examine(l) assertThat {
            not {
                anything()
            }
        }
    }

    @Test
    fun testHasItemNull() {
        val l = listOf(1, null, 3)

        examine(l) assertThat {
            it hasItem null
        }
    }

    @Test
    fun testArrayContainging() {
        val a: Array<String?>? = arrayOf("foo", "bar")
        examine(a) assertThat {
            arrayContaining {
                it equalTo "foo"
                it equalTo "bar"
            }
            it arrayContaining arrayOf("foo", "bar")
            it arrayWithSize 2
            arrayWithSize { it equalTo 2 }
        }
    }

    @Test
    fun testArrayContaingingInAnyOrder() {
        val a: Array<String?>? = arrayOf("foo", "bar")
        examine(a) assertThat {
            arrayContainingInAnyOrder {
                it equalTo "bar"
                it equalTo "foo"
            }
            it arrayContainingInAnyOrder arrayOf("bar", "foo")
        }
    }

    @Test
    fun testBlankString() {
        val s = " "

        examine(s) assertThat {
            blankOrNullString()
            blankString()
        }
    }

    @Test
    fun testCloseTo() {
        val actual: BigDecimal = BigDecimal("12.3")
        val expected: BigDecimal = BigDecimal("12.5")
        val error: BigDecimal = BigDecimal("0.25")

        examine(actual) assertThat {
            closeTo(expected, error)
        }
    }

    @Test
    fun testHasProperty() {

        class Foo(val bar: String) {}

        val foo = Foo("foobar")

        examine(foo) assertThat {
            it hasProperty "bar"
            hasProperty("bar") { it containsString "oba" }
        }
    }

    @Test
    fun testComparable() {

        val i = 5

        examine(i) assertThat {
            it greaterThan 3
            it lessThan 8
        }
    }

    @Test
    fun testComparableList() {

        val l = listOf(3, 4, 5)

        examine(l) assertThat {
            everyItem {
                it greaterThanOrEqualTo 3
            }
        }
    }

    @Test
    fun testMe() {
        val l = listOf(1,3,5,7)
        examine(l) assertThat {
            hasItem {
                it greaterThan 4
                it lessThan 6
            }
        }
    }
}
