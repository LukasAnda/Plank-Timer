package tech.annexflow.precompose.navigation.typesafe

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPEALIAS
)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
annotation class ExperimentalTypeSafeApi
