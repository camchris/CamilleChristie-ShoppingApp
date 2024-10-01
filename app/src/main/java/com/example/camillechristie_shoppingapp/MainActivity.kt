package com.example.camillechristie_shoppingapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.example.camillechristie_shoppingapp.ui.theme.CamilleChristieShoppingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CamilleChristieShoppingAppTheme {
                Column {
                    mainScreen()
                }
            }
        }
    }
}

data class Product(
    val name: String,
    val price: String,
    val description: String
)

@Composable
fun mainScreen() {
    val navController = rememberNavController() // for navigation
    NavHost(navController = navController, startDestination = "shoppingList") {
        composable("shoppingList") { ShoppingList(navController) }
        composable(
            route = "portraitDetails?product={product}",
            arguments = listOf(navArgument("product") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            val productJson = it.arguments?.getString("product")
            val product = Gson().fromJson(productJson, Product::class.java)
            PortraitDetails(navController, product = product)
        }
    }

}

@Composable
fun PortraitDetails(navController: NavController, product: Product?) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("ShoppingList") }) {
            Text("Back")
        }
        Text(
            text = "Price: ${product!!.price}",
            modifier = Modifier.padding(20.dp)
        )
        Text(
            text = product.description,
            modifier = Modifier.padding(20.dp)
        )
    }

}

@Composable
fun ShoppingList(navController: NavController) { // screen 1
    val configuration = LocalConfiguration.current
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val products = listOf(
        Product("Carrots", "$2", "Carrots. Maybe the baby kind. Not much else to say about them."),
        Product("Grapes", "$4", "Green grapes. Extra plump. Extra juicy. Refrigerated. Very hydrating."),
        Product("Bananas", "$700", "Bunch of 3 bananas. Price adjusted to discourage people from purchasing because bananas taste bad."),
        Product("Peanut Butter", "$5", "Extra chunky peanut butter. Absolute game changer. Can get through all of CS501 lecture with a single spoonful."),
        Product("Instant Oatmeal", "$8", "Strawberries and Cream flavored. Will not make you feel full at all but very yummy.")
    )

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row {
                LazyColumn {
                    items(
                        items = products,
                        key = { product -> product.name }
                    ) { product ->
                        Text(
                            text = product.name,
                            modifier = Modifier
                                .clickable {
                                    selectedProduct = product
                                }
                                .padding(20.dp)
                        )
                    }
                }
                if (selectedProduct != null) {
                    Text(
                        text = "Price: ${selectedProduct!!.price}",
                        modifier = Modifier.padding(20.dp)
                    )
                    Text(
                        text = selectedProduct!!.description,
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    Text(
                        text = "No product currently selected",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
        else -> {
            LazyColumn {
                items(
                    items = products,
                    key = { product -> product.name }
                ) { product ->
                    Text(
                        text = product.name,
                        modifier = Modifier
                            .clickable {
                                selectedProduct = product
                                val productJson = Gson().toJson(product)
                                navController.navigate("portraitDetails?product=$productJson")
                            }
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}
