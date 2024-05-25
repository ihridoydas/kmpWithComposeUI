package di

import HomeRepository
import HomeViewmodel
import org.koin.dsl.module
import root.DefaultRootComponent
import root.RootComponent

fun commonModule() = networkModule() + module {

    single {
        HomeRepository(get())
    }

    single {
        HomeViewmodel(get())
    }

    single<RootComponent> {
        DefaultRootComponent(
            componentContext = get(),
            homeViewModel = get()
        )
    }

}