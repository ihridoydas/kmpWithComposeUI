package root

import HomeViewmodel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import data.Product
import detail.DefaultDetailComponent
import detail.DetailComponent
import kotlinx.serialization.Serializable
import list.DefaultListComponent
import list.ListComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
        class ListChild(val component: ListComponent): Child()

        class DetailChild(val component: DetailComponent): Child()
    }

}

class DefaultRootComponent(
    private val componentContext : ComponentContext,
    private val homeViewmodel: HomeViewmodel
): RootComponent, ComponentContext by componentContext{

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.List,
            handleBackButton = true,
            childFactory = ::childFactory
        )
    private fun childFactory(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child{
        return when(config){
            is Config.List -> RootComponent.Child.ListChild(
                DefaultListComponent(
                    componentContext,
                    homeViewmodel
                ){item ->
                    navigation.push(Config.Detail(item))
                    //it will change the content of Detail
                }
            )
            is Config.Detail -> RootComponent.Child.DetailChild(
                DefaultDetailComponent(
                    componentContext,
                    item = config.item
                ){
                    onBackClicked()
                }
            )
        }

    }

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object List : Config
        @Serializable
        data class Detail(val item: Product) : Config

    }

}