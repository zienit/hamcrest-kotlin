# hamcrest-kotlin
Kotlin DSL for Hamcrest

This DSL allows you to build simple or complex assertions with the familiar Hamcrest building blocks aka matchers.
Its use is best explained with a few examples.

#### Example 1
A simple assertion on the contents of a string. Note that 'it' here is not Kotlin's it but some syntactic sugar to allow a nice infix expression without parentheses.

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
A variation of example 2. Here at least one of the clauses must be true.

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

### Example 5
If a block contains more than one clause, those clauses are automatically wrapped inside an implicit allOf clause. 
In this example the list must contain an item that is > 4 and < 6.
```
val l = listOf(1,3,5,7)

examine(l) assertThat {
    hasItem {
        it greaterThan 4
        it lessThan 6
    }
}
```