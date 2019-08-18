# hamcrest-kotlin
Kotlin DSL for Hamcrest

examples

```
val y = "hello"

on (y) assertThat {
    it equalTo "hello"
}
```