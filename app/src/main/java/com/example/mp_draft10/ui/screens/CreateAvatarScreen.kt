package com.example.mp_draft10.ui.screens

//import java.lang.reflect.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mp_draft10.R
import com.example.mp_draft10.database.AddNewUserViewModel

@Composable
fun CreateAvatarScreen( addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {

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

    var selectedAvatarIndex by remember { mutableStateOf(0) }
    var selectedColorIndex by remember { mutableStateOf(0) }

    // Use indices to get the current selections
    val selectedAvatar = avatarImages[selectedAvatarIndex]
    val selectedColor = colors[selectedColorIndex]

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar preview with selected background color
        Row {
            Button(
                onClick = {
                    // Now, pass the selected indices or directly pass the selected items depending on what your save function requires
                    addNewUserViewModel.saveUserAvatar(
                        selectedColorIndex,
                        selectedAvatarIndex
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Save Avatar")
            }
        }
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(selectedColor)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = selectedAvatar),
                contentDescription = "Selected Avatar",
                modifier = Modifier
                    .size(90.dp)
                    .padding(5.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Choose your avatar",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
            // Avatar preview and other UI elements remain unchanged

            // When displaying avatar options
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp), // Choose an appropriate minSize for your items
                contentPadding = PaddingValues(all = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Display avatar options
                itemsIndexed(avatarImages) { index, avatar ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = if (selectedAvatarIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .aspectRatio(1f)
                            .clickable { selectedAvatarIndex = index }
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                        )
                    }
                }

                // Insert a spacer item between the avatars and colors
                item {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)) // Adjust the height as needed to create space
                }

                // Display color options
                itemsIndexed(colors) { index, color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                2.dp,
                                if (selectedColorIndex == index) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                CircleShape
                            )
                            .clickable { selectedColorIndex = index }
                            .padding(8.dp)
                    )
                }
            }
    }
}

@Composable
fun ColorBox(
    color: Color,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 1.dp,
                color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onColorSelected(color) }
            .padding(8.dp)
    )
}

@Preview(showBackground = true, name = "Create Avatar Screen Preview")
@Composable
fun CreateAvatarScreenPreview() {
    CreateAvatarScreen()
}
