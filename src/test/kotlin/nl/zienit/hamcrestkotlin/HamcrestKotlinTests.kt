package nl.zienit.hamcrestkotlin;

import org.junit.Test
import java.math.BigDecimal
import java.util.regex.Pattern

class HamcrestKotlinTests {

    @Test
    fun testEqualTo() {
        val y = "hello"

        examine(y) assertThat {
            it equalTo "hello"
            it comparesEqualTo "hello"
            it containsStringIgnoringCase "HELL"
            it endsWith "lo"
            it endsWithIgnoringCase "LO"
            it startsWith "he"
            it startsWithIgnoringCase  "HE"
            it not "foo"
            stringContainsInOrder("he","lo")
            it stringContainsInOrder listOf("h","llo")
        }
    }

    @Test
    fun testString() {

        examine("this    text contains   irregular    whitespace") assertThat {
            it equalToCompressingWhiteSpace "this text contains irregular whitespace"
        }

        examine("FOOBAR") assertThat {
            it equalToIgnoringCase "foobar"
        }

        examine("foo") assertThat {
            it hasLength 3
        }

        examine(43) assertThat {
            it hasToString "43"
            hasToString { it hasLength 2 }
        }
    }

    @Test
    fun testEqualToObject() {

        examine("foo") assertThat {
            not {
                it equalToObject 42
            }
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
            it isA Int::class
            it any Int::class
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
            arrayContaining("foo", "bar")
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
            arrayContainingInAnyOrder("bar", "foo")
        }
    }

    @Test
    fun testBlankString() {
        val s = ""

        examine(s) assertThat {
            blankOrNullString()
            blankString()
            emptyOrNullString()
            emptyString()
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
            hasProperty("bar") { it equalTo "foobar" }
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
    fun testAMapWithSize() {

        val m = mapOf(1 to "one", 2 to "two")

        examine(m) assertThat {
            it aMapWithSize 2
            aMapWithSize { it lessThan 3 }
            not { anEmptyMap() }
        }
    }

    @Test
    fun testContains() {

        val l = listOf(5, 10, 15, 20)

        examine(l) assertThat {
            contains(5, 10, 15, 20)
            contains(*l.toTypedArray())
            contains {
                it equalTo 5
                it equalTo 10
                it equalTo 15
                it equalTo 20
            }
            containsInAnyOrder(10, 5, 20, 15)
            containsInRelativeOrder(10, 20)
            containsInRelativeOrder {
                it greaterThanOrEqualTo 5
                it greaterThanOrEqualTo 10
            }
        }
    }

    @Test
    fun testEmpty() {
        val l: List<Int> = listOf()

        examine(l) assertThat {
            empty()
        }

        val a = emptyArray<Int>()

        examine(a) assertThat {
            emptyArray()
        }
    }

    @Test
    fun testHasItemInArray() {

        examine(arrayOf(1, 2, 3)) assertThat {
            it hasItemInArray 2
            hasItemInArray {
                it greaterThanOrEqualTo 2
                it lessThan 3
            }
        }
    }

    @Test
    fun testHasSize() {

        examine(listOf("foo", "bar")) assertThat {
            it hasSize 2
            hasSize {
                it greaterThan 1
                it lessThan 3
            }
        }
    }

    @Test
    fun testIn() {

        examine(42) assertThat {
            `in`(41, 42, 43)
            `in`(listOf(41, 42, 43))
        }
    }

    @Test
    fun testRegex() {
        val s = "foobar";

        val p = Pattern.compile(".o.b.r")

        examine(s) assertThat {
            it matchesPattern p
            it matchesPattern "f..b.r"

            it matchesRegex p
            it matchesRegex ".oo.ar"
        }
    }

    @Test
    fun testTheInstance() {

        class A {}

        val i = A();

        examine(i) assertThat {
            it theInstance i
            it sameInstance i
        }
    }

    @Test
    fun testMe() {
        val l = listOf(1, 3, 5, 7)
        examine(l) assertThat {
            hasItem {
                it greaterThan 4
                it lessThan 6
            }
        }
    }
}
