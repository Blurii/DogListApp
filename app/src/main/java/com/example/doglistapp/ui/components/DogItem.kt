package com.example.doglistapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun DogItem(
    name: String,
    breed: String,
    photoUrl: String,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xff65558f), Color(0xf0eeb6e8)),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Dog photo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 18.sp)
                Text(breed, fontSize = 14.sp, color = Color.Gray)
            }

            IconButton(onClick = onFavoriteToggle) {
                val brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6A5ACD),
                        Color(0xFFFFC0CB)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(50f, 100f)
                )
                val icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

                Icon(
                    imageVector = icon,
                    contentDescription = "Toggle Favorite",
                    modifier = Modifier
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                if (isFavorite) {
                                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                                }
                            }
                        }
                )
            }


            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
