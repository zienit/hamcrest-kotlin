package nl.zienit.hamcrestkotlin;

import org.junit.Test

class HamcrestKotlinTests {

    @Test
    fun testEqualTo() {
        val y = "hello"

        assertThat {
            y equalTo "hello"
        }
    }

    @Test(expected = AssertionError::class)
    fun testEqualToFailure() {
        val y = "hello"

        assertThat {
            y equalTo "hi"
        }
    }

    @Test
    fun testhasItem() {
        val x = listOf(1, 2, 3, 4)

        assertThat {
            x hasItem 2
        }
    }

    @Test(expected = AssertionError::class)
    fun testhasItemFailure() {
        val x = listOf(1, 2, 3, 4)

        assertThat {
            x hasItem 5
        }
    }

    @Test
    fun testhasItemMatcher() {
        val x = listOf("foo", "bar")

        assertThat {
            x hasItem { it containsString "oo" }
        }
    }

    @Test(expected = AssertionError::class)
    fun testhasItemMatcherFailure() {
        val x = listOf("foo", "bar")

        assertThat {
            x hasItem { it containsString "xx" }
        }
    }


    @Test
    fun testContainsString() {
        val y = "hello"

        assertThat {
            y containsString "ll"
        }
    }

    @Test(expected = AssertionError::class)
    fun testContainsStringFailure() {
        val y = "hello"

        assertThat {
            y containsString "xx"
        }
    }

    @Test
    fun testHasEntry() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        assertThat {
            y hasEntry (2 to "two")
        }
    }

    @Test(expected = AssertionError::class)
    fun testHasEntryFailure() {
        val y = mapOf(1 to "one", 2 to "two", 3 to "three")

        assertThat {
            y hasEntry (4 to "four")
        }
    }

    @Test
    fun testDescribing() {
        val life = 42

        assertThat {
            "the meaning of life is 42" describing { life equalTo 42 }
        }

        assertThat {
            life allOf {
                "the meaning of life is 42" describing { life equalTo 42 }
            }
        }
    }

    @Test
    fun testInstanceOf() {
        val x = 5

        assertThat {
            x instanceOf Int::class
        }
    }

    @Test(expected = AssertionError::class)
    fun testInstanceOfFailure() {
        val x = 5

        assertThat {
            x instanceOf String::class
        }
    }

    @Test
    fun test2() {
        print("test2")
        val x = listOf(1, 2, 3, 4)
        val y = "hello"
        val z = listOf("foo", "bar", "baz")
        val q = null

        assertThat {
            x hasItems arrayOf(1, 2)
            q equalTo null
            y not { it containsString "xx" }
            x allOf {
                it hasItem 3
                it hasItem 4
                it hasItem 1
            }
        }
    }
}
