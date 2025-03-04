package com.seifmortada.applications.aroundegypt.detail.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun DetailRoute(
    experienceId: String,
    viewModel: DetailViewModel = koinViewModel()
) {
    LaunchedEffect(experienceId) {
        viewModel.getExperience(experienceId)
    }
    val experience by viewModel.experience.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    DetailScreen(
        experience, loadingState, errorState, onLikeClick = viewModel::likeExperience
    )

}

@Composable
fun DetailScreen(
    experience: Experience?,
    loadingState: Boolean,
    errorState: String?,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        loadingState -> LoadingState()
        errorState != null -> ErrorState(errorState)
        experience != null -> ExperienceContent(experience, onLikeClick)
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $errorMessage", color = Color.Red)
    }
}

@Composable
private fun ExperienceContent(experience: Experience, onLikeClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ExperienceImage(experience.imgSrc, experience.numberOfViews)

        Spacer(modifier = Modifier.height(16.dp))

        ExperienceInfo(experience, onLikeClick)

        Spacer(modifier = Modifier.height(16.dp))

        ExperienceDescription(experience.description)
    }
}

@Composable
private fun ExperienceImage(imageUrl: String, views: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                ViewCount(views)
            }
        }
    }
}

@Composable
private fun ExperienceInfo(experience: Experience, onLikeClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = experience.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = experience.address,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        LikeButton(experience.id, experience.isLiked, experience.numberOfLikes, onLikeClick)
    }
}

@Composable
private fun LikeButton(
    experienceId: String,
    isLiked: Boolean,
    likesNo: Int,
    onLikeClick: (String) -> Unit
) {
    var isLiked by remember { mutableStateOf(isLiked) }
    var likesCount by remember { mutableStateOf(likesNo) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = !isLiked,
            onClick = {
                isLiked = true
                onLikeClick(experienceId)
            }) {
            Icon(
                imageVector = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "Like",
                tint = Color.Red
            )
        }
        Text(
            text = if (isLiked) (likesCount + 1).toString() else likesCount.toString(),
            fontSize = 16.sp
        )
    }
}

@Composable
private fun ExperienceDescription(description: String) {
    Text(
        text = "Description",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = description,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Composable
private fun ViewCount(views: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.RemoveRedEye,
            contentDescription = "Views",
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = views.toString(), fontSize = 14.sp, color = Color.White)
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        experience = null,
        loadingState = false,
        errorState = null,
        {}
    )

}
