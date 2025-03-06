package com.seifmortada.applications.aroundegypt.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.aroundegypt.core.domain.Experience

@Composable
fun ExperienceLikesSection(
    experience: Experience,
    onLikeClick: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            enabled = !experience.isLiked,
            onClick = { onLikeClick(experience.id) }) {
            Icon(
                imageVector = if (experience.isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "Like",
                tint = Color.Red
            )
        }
        Text(text = experience.numberOfLikes.toString(), fontSize = 14.sp)
    }
}