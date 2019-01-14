# cjkrad4j

Porting of [cjkradlib](https://github.com/patarapolw/cjkradlib) to Kotlin

## Installation

It is now available in `mavenCentral()`, so add this to your dependencies

```groovy
implementation 'io.github.patarapolw:cjkrad:0.1.2'
```

The database file can be found here, [cjkrad4j.db](/cjkrad4j.db)

## Example

```kotlin
import cjkrad4j.RadicalFinder

fun main(args: Array<String>) {
    val finder = RadicalFinder("jdbc:sqlite:path/to/cjkrad4j.db")
    val result = finder['你']
    println(result.subCompositions)
    println(result.superCompositions)
    println(result.variants)
    println(result.flatten())
}
```

And the run the script, with `kotlin script.kt` or `./gradlew run` and you should see

```commandline
[亻, 尔]
[您]
[妳]
RadicalDataClass(subCompositions=[亻, 尔], superCompositions=[您], variants=[妳]
```
