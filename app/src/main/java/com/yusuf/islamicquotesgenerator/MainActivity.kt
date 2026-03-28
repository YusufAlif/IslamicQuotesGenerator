package com.yusuf.islamicquotesgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.islamicquotesgenerator.model.Quote
import com.yusuf.islamicquotesgenerator.model.source
import com.yusuf.islamicquotesgenerator.ui.theme.IslamicQuotesGeneratorTheme

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

@Composable
fun IslamicQuotesScreen() {
    val quotesList = source.dummyquote
    var highlightedQuote by remember { mutableStateOf(quotesList.random()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Islamic Quotes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 4.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Inspirasi dari Al-Qur'an",
            fontSize = 14.sp,
            color = Color(0xFF388E3C),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Random Highlight Card
        HighlightCard(quote = highlightedQuote)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { highlightedQuote = quotesList.random() },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("🔀 Random Quote", color = Color.White)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Label semua quote
        Text(
            text = "Semua Kutipan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B5E20)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Semua quote ditampilkan
        quotesList.forEach { quote ->
            QuoteCard(quote = quote)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HighlightCard(quote: Quote) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Box {
                Image(
                    painter = painterResource(id = quote.imageRes),
                    contentDescription = quote.source,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "❝ ${quote.quotes} ❞",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "— ${quote.source}",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFA5D6A7),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun QuoteCard(quote: Quote) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = quote.imageRes),
                    contentDescription = quote.source,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        tint = if (isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quote.quotes,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = quote.source,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF388E3C)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IslamicQuotesPreview() {
    IslamicQuotesGeneratorTheme {
        IslamicQuotesScreen()
    }
}