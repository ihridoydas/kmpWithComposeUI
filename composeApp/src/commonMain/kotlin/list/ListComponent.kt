package list

import HomeViewmodel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface ListComponent {

    val model: Value<Model>

    fun onItemClicked(item: Product)

    data class Model(
        val items: List<Product>
    )
}

class DefaultListComponent(
    private val componentContext : ComponentContext,
    private val homeViewmodel: HomeViewmodel,
    private val onItemSelected: (item: Product) -> Unit
):ListComponent, ComponentContext by componentContext {
    private val _model = MutableValue(ListComponent.Model(items = emptyList()))
    override val model: Value<ListComponent.Model> = _model
    override fun onItemClicked(item: Product) {
        onItemSelected(item)
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            homeViewmodel.products.collect{
                _model.value = ListComponent.Model(items = it)
            }
        }
    }
}