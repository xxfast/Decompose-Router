# How to Use Decompose Router

Decompose router supports the following navigation models, that is driven by a [model](#model-driven-navigation)

1. [Stack navigation](using-stack-navigation.md)
2. Page navigation 🚧
3. Slot navigation 🚧
   
These are the same ones supported by [Decompose](https://github.com/arkivanov/Decompose)

<deflist type="narrow" sorted="desc">
    <def title="Stack navigation">
        Stack is a navigation model for managing a stack of components
    </def>
    <def title="Page navigation">
        Pages is a navigation model for managing a list of components (pages) with one selected (active) component. 
    </def>
    <def title="Slot navigation">
        Slot is a navigation model that allows only one child component at a time, or none.
    </def>
</deflist>

## Model driven navigation

Model driven navigation is the idea that each node of your navigation hierarchy is represented by a model with a strict schema (as opposed to a URL string).
This strict schema makes all the navigation arguments type-safe 

For example, a typical list/detail screen flow would look like this
```kotlin
@Serializable
sealed class Screens {
  @Serializable data object List: Screens()
  @Serializable data class Details(val number: Int): Screens()
}
```

> `@Serializable` is optional but **preferred** if you want your screen state to 
>    1. Be retained across configuration changes (on Android) 
>    2. Survive process death (on Android)  
> 
>  If these are not a concern for the given screens - you may omit `@Serialisable` from the given screen.

