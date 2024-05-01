import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        AppContent(viewmodel = HomeViewmodel())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(viewmodel: HomeViewmodel) {
    val products = viewmodel.products.collectAsState()

    BoxWithConstraints {
        val scope = this
        val maxWidth = scope.maxWidth

        var cols = 2
        var modifier = Modifier.fillMaxWidth()
        if (maxWidth >= 840.dp) {
            cols = 3
            modifier = Modifier.widthIn(max = 1080.dp)
        }

        val scrollState = rememberLazyGridState()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(cols),
                state = scrollState,
                contentPadding = PaddingValues(16.dp)
            ) {

                item(span = { GridItemSpan(cols) }) {
                    Column {
                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            query = "",
                            active = false,
                            onActiveChange = {},
                            onQueryChange = {},
                            onSearch = {},
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Products"
                                )
                            },
                            placeholder = { Text("Search Products") }
                        ) {}
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                }

                items(
                    items = products.value,
                    key = { product -> product.id.toString() }
                ) { products ->
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val painter = rememberImagePainter(products.image.toString())
                            Image(
                                painter = painter,
                                modifier = Modifier.height(130.dp).padding(8.dp),
                                contentDescription = products.description
                            )
                            Text(
                                text = products.title.toString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 16.dp).heightIn(min = 40.dp)
                            )
                            Spacer(modifier = Modifier.heightIn(16.dp))
                            Text(
                                text = "${products.price.toString()} BDT",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 16.dp).heightIn(min = 40.dp)
                            )

                        }


                    }

                }

            }

        }

    }


}