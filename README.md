# hamcrest-kotlin
Kotlin DSL for Hamcrest

This DSL allows you to build simple or complex assertions with the familiar Hamcrest building blocks aka matchers.
Its use is best explained with a few examples.

#### Example 1
A simple assertion on the contents of a string. Note that 'it' here is not Kotlin's it but some syntactic sugar to allow a nice infix expression without parantheses.

```
val f = "foobar"

examine (f) assertThat {
    it equalTo "foobar"
}
```

### Example 2
A more involved example where a string is to contain two substrings. Note that all clauses inside the assertThat block must be true.

```
val f = "foobar"

examine (f) assertThat {
    it containsString "foo"
    it containsString "bar"
}
```

### Example 3
A variation of example 2. Here at least on of the clauses must be true.

```
val f = "foobar"
examine(f) assertThat {
    anyOf {
        it containsString "foo"
        it containsString "baz"
    }
}
```

### Example 4
An assertion on a list of integers where each item must be greater than or equal to 3

```
val l = listOf(3,4,5)

examine(l) assertThat {
    everyItem {
        it greaterThanOrEqualTo 3
    }
```