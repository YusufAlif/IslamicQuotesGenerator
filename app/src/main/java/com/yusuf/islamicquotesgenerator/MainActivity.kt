package com.yusuf.islamicquotesgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yusuf.islamicquotesgenerator.model.Quote
import com.yusuf.islamicquotesgenerator.model.source
import com.yusuf.islamicquotesgenerator.ui.theme.IslamicQuotesGeneratorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IslamicQuotesGeneratorTheme {
                IslamicQuotesScreen()
            }
        }
    }
}

val kategoriQuote = listOf("Semua", "Sabar", "Syukur", "Ikhlas", "Tawakal", "Doa")

@Composable
fun IslamicQuotesScreen() {
    val quotesList = source.dummyquote
    var highlightedQuote by remember { mutableStateOf(quotesList.random()) }
    var selectedKategori by remember { mutableStateOf("Semua") }

    // State untuk Coroutine dan Snackbar (Modul 9 Hal 8)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Text(
                    text = "Islamic Quotes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Inspirasi dari Al-Qur'an",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Kategori Chips
            item {
                Text(
                    text = "Kategori",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(kategoriQuote) { kategori ->
                        KategoriChip(
                            label = kategori,
                            isSelected = selectedKategori == kategori,
                            onClick = { selectedKategori = kategori }
                        )
                    }
                }
            }

            // Random Quote Section (Implementasi Coroutine & Loading)
            item {
                Text(
                    text = "Quote Hari Ini",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                HighlightCard(quote = highlightedQuote)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Komponen Tombol dengan Lingkaran Loading
                RandomQuoteButton(
                    onRefresh = { highlightedQuote = quotesList.random() },
                    scope = scope,
                    snackbarHostState = snackbarHostState
                )
            }

            item {
                Text(
                    text = "Semua Kutipan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(quotesList) { quote ->
                QuoteCard(quote = quote, scope = scope, snackbarHostState = snackbarHostState)
            }
        }
    }
}

@Composable
fun RandomQuoteButton(onRefresh: () -> Unit, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            scope.launch {
                isLoading = true
                delay(1500) // Simulasi loading
                onRefresh()
                isLoading = false
                snackbarHostState.showSnackbar("Quote baru telah dimuat!")
            }
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mencari...")
        } else {
            Text("🔀 Random Quote")
        }
    }
}

@Composable
fun QuoteCard(quote: Quote, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    var isFavorite by remember { mutableStateOf(false) }
    var isCopying by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = quote.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "❝ ${quote.quotes} ❞", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "— ${quote.source}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isCopying = true
                            delay(1000)
                            isCopying = false
                            snackbarHostState.showSnackbar("Berhasil menyalin quote!")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCopying
                ) {
                    if (isCopying) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Menyalin...")
                    } else {
                        Text("📋 Salin Quote")
                    }
                }
            }
        }
    }
}

@Composable
fun HighlightCard(quote: Quote) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "❝ ${quote.quotes} ❞", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun KategoriChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) }
    )
}