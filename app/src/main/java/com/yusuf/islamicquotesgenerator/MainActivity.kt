    package com.yusuf.islamicquotesgenerator

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
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
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.text.font.FontStyle
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import coil.compose.AsyncImage
    import com.yusuf.islamicquotesgenerator.model.Quote
    import com.yusuf.islamicquotesgenerator.model.source
    import com.yusuf.islamicquotesgenerator.ui.theme.IslamicQuotesGeneratorTheme
    import kotlinx.coroutines.launch
    import androidx.compose.runtime.rememberCoroutineScope

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header
            item {
                Text(
                    text = "Islamic Quotes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp),
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

            // LazyRow - Kategori chip
            item {
                Text(
                    text = "Kategori",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(kategoriQuote) { kategori ->
                        KategoriChip(
                            label = kategori,
                            isSelected = selectedKategori == kategori,
                            onClick = { selectedKategori = kategori }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Highlight Card Random
            item {
                Text(
                    text = "Quote Hari Ini",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                HighlightCard(quote = highlightedQuote)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { highlightedQuote = quotesList.random() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "🔀 Random Quote",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Label semua kutipan
            item {
                Text(
                    text = "Semua Kutipan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // LazyColumn - semua quote
            items(quotesList) { quote ->
                QuoteCard(quote = quote)
            }
        }
    }

    // Chip kategori
    @Composable
    fun KategoriChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = label,
                style = if (isSelected)
                    MaterialTheme.typography.titleSmall
                else
                    MaterialTheme.typography.bodyMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

    // Highlight card besar
    @Composable
    fun HighlightCard(quote: Quote) {
        var isFavorite by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box {
                    AsyncImage(
                        model = quote.imageUrl,
                        contentDescription = quote.source,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite Icon",
                            tint = if (isFavorite) Color.Red
                            else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "❝ ${quote.quotes} ❞",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "— ${quote.source}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }

    // Card list bawah
    @Composable
    fun QuoteCard(quote: Quote) {
        var isFavorite by remember { mutableStateOf(false) }
        var isCopied by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box {
                        AsyncImage(
                            model = quote.imageUrl,
                            contentDescription = quote.source,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { isFavorite = !isFavorite },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite Icon",
                                tint = if (isFavorite) Color.Red
                                else Color.White
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "❝ ${quote.quotes} ❞",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "— ${quote.source}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isCopied = true
                                    snackbarHostState.showSnackbar("Quote berhasil disalin! ✅")
                                    isCopied = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isCopied,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isCopied) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Menyalin...",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    "📋 Salin Quote",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun IslamicQuotesPreview() {
        IslamicQuotesGeneratorTheme {
            IslamicQuotesScreen()
        }
    }