package com.example.mp_draft10.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mp_draft10.R

@Composable
fun DisplaySavedAvatarAndColor(
    avatarImageString: String?,
    backgroundColorString: String?,
    iconSize: Int
) {
    // Lists of avatars and colors (make sure these are the same lists used in the saving part)
    val avatarImages = mutableListOf(
        R.drawable.bee,
        R.drawable.butterfly,
        R.drawable.clover,
        R.drawable.dove,
        R.drawable.elf,
        R.drawable.pineapple,
        R.drawable.pizza,
        R.drawable.plant,
        R.drawable.sakura,
        R.drawable.sleet,
        R.drawable.snails,
        R.drawable.turtle,
        R.drawable.coffee,
        R.drawable.custard,
        R.drawable.bonfire,
        R.drawable.gimbap,
        R.drawable.ham,
        R.drawable.mango,
        R.drawable.experiment,
        R.drawable.tea,
        R.drawable.bavarian,
        R.drawable.cactus,
        R.drawable.camel,
        R.drawable.cow,
        R.drawable.garlic,
        R.drawable.juice,
        R.drawable.meditation,
        R.drawable.pumpkin,
        R.drawable.reindeer
    )
    val colors = mutableListOf(
        Color(0xFFF0EEBB),
        Color(0xFFF1E3B7),
        Color(0xFFF4D6B0),
        Color(0xFFF1C5A8),
        Color(0xFFEFB5A6),
        Color(0xFFEDB6B9),
        Color(0xFFEEB8C6),
        Color(0xFFEDB9CD),
        Color(0xFFE1B8D0),
        Color(0xFFD9B1CD),
        Color(0xFFCBAFCD),
        Color(0xFFBCAACC),
        Color(0xFFB1A5CE),
        Color(0xFFAAAAD0),
        Color(0xFFA9B3D6),
        Color(0xFFA5BEDE),
        Color(0xFFA4C7E7),
        Color(0xFFA6D2EC),
        Color(0xFFA7D4E7),
        Color(0xFFABD4CE),
        Color(0xFFAAD3C1),
        Color(0xFFACD2B9),
        Color(0xFFB9D7B8),
        Color(0xFFCBDFBA),
    )

    val avatarIndex = avatarImageString?.toIntOrNull()
    val backgroundColorIndex = backgroundColorString?.toIntOrNull()

    val safeBackgroundColorIndex = backgroundColorIndex ?: 0
    val safeAvatarImageIndex = avatarIndex ?: 0

    Box(
        modifier = Modifier
            .size(iconSize.dp)
            .clip(CircleShape)
            .background(colors.getOrElse(safeBackgroundColorIndex) { colors.first() })
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = avatarImages.getOrElse(safeAvatarImageIndex) { avatarImages.first() }),
            contentDescription = "Selected Avatar",
            modifier = Modifier
                .size(150.dp)
                .padding(1.dp)
        )
    }
}