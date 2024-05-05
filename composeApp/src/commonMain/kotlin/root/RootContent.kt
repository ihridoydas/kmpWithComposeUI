package root

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import detail.DetailContent
import list.ListContent

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {

    MaterialTheme {
        Children(
            stack = component.childStack,
            modifier = modifier,
            animation = stackAnimation(fade())
        ) {
            when(val child = it.instance) {
                is RootComponent.Child.ListChild -> ListContent(child.component)
                is RootComponent.Child.DetailChild -> DetailContent(child.component)
            }
        }
    }
}