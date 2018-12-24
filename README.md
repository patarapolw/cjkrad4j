# cjkrad4j

Porting of [cjkradlib](https://github.com/patarapolw/cjkradlib) to Kotlin

## Building and Installation

- Clone the project from GitHub and `cd` into the project.
- run `./gradlew build`
- Copy `build/libs/cjkrad4j-0.1.0.jar` into `libs/` of the project of your choice. Create the folder if it isn't existed yet.
- Add the follow lines into `dependencies` of your `build.gradle`.

```groovy
    compile fileTree(include: ['*.jar'], dir: 'libs')
```

- Then, run `./gradlew build`.
- You should be able to `import cjkrad4j.RadicalFinder`

## Example

```kotlin
import cjkrad4j.RadicalFinder

fun main(args: Array<String>) {
    val finder = RadicalFinder()
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
