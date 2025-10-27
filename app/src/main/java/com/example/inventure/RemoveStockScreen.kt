package com.example.inventure

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveStockScreen(
    viewModel: InventureViewModel,
    onBackClick: () -> Unit
) {
    val products = viewModel.products.collectAsState(initial = emptyList())
    var selectedProduct by remember { mutableStateOf<Inventure?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remove Stock") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (products.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No products available.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products.value) { product ->
                    RemoveStockCard(
                        product = product,
                        onClick = { selectedProduct = product }
                    )
                }
            }
        }
    }

    // Show dialog when product is selected
    selectedProduct?.let { product ->
        RemoveStockDialog(
            product = product,
            onDismiss = { selectedProduct = null },
            onConfirm = { quantityToRemove ->
                viewModel.updateProductQuantity(product, quantityToRemove)
                selectedProduct = null
            }
        )
    }
}

@Composable
fun RemoveStockCard(
    product: Inventure,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            if (product.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUri),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = product.name.take(1).uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4B0082)
                            )
                        }
                    }
                }
            }

            // Product Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Available: ${product.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Remove button indicator
            Text(
                text = "TAP TO REMOVE",
                fontSize = 12.sp,
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RemoveStockDialog(
    product: Inventure,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantityToRemove by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Remove Stock",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Available quantity: ${product.quantity}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B0082)
                )

                OutlinedTextField(
                    value = quantityToRemove,
                    onValueChange = {
                        quantityToRemove = it
                        errorMessage = null
                    },
                    label = { Text("Quantity to Remove") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null,
                    supportingText = {
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val amount = quantityToRemove.toIntOrNull()
                            when {
                                amount == null || amount <= 0 -> {
                                    errorMessage = "Enter a valid quantity"
                                }
                                amount > product.quantity -> {
                                    errorMessage = "Not enough stock (max: ${product.quantity})"
                                }
                                else -> {
                                    onConfirm(amount)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}