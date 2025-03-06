package com.seifmortada.applications.aroundegypt.home.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.ui.components.ErrorScreen
import com.seifmortada.applications.aroundegypt.core.ui.components.ExperienceLikesSection
import com.seifmortada.applications.aroundegypt.core.ui.components.ExperienceViewCount
import com.seifmortada.applications.aroundegypt.core.ui.components.LoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onExperienceClick: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val recommendedExperiences by
    viewModel.recommendedExperiencesState.collectAsStateWithLifecycle()
    val recentExperiences by viewModel.recentExperiencesState.collectAsStateWithLifecycle()
    val searchExperiencesState by viewModel.searchExperiencesState.collectAsStateWithLifecycle()
    HomeScreen(
        loadingState = loadingState,
        errorState = errorState,
        recommendedExperiencesState = recommendedExperiences,
        recentExperiencesState = recentExperiences,
        searchExperiencesState = searchExperiencesState,
        onSearchExperiences = viewModel::searchExperiences,
        onLikeClick = viewModel::likeExperience,
        onRefreshExperience = viewModel::refresh,
        onExperienceClick = onExperienceClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    loadingState: Boolean,
    errorState: String?,
    recommendedExperiencesState: List<Experience>,
    recentExperiencesState: List<Experience>,
    searchExperiencesState: List<Experience>,
    onRefreshExperience: () -> Unit = {},
    onSearchExperiences: (String) -> Unit = {},
    onExperienceClick: (String) -> Unit = {},
    onLikeClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            SearchTopAppbar(
                scrollBehavior = scrollBehavior,
                searchQuery = searchQuery,
                onSearchQueryChanged = {
                    if (it.isBlank()) {
                        onRefreshExperience()
                        searchQuery = it
                    } else {
                        searchQuery = it
                        onSearchExperiences(it)
                    }
                },
                onSearchTriggered = {}
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                loadingState -> LoadingScreen()
                errorState != null -> ErrorScreen(errorState)
                searchExperiencesState.isNotEmpty() -> SearchedExperiencesSection(
                    searchExperiencesState,
                    onLikeClick,
                    onExperienceClick
                )
                else -> DefaultContent(
                    recommendedExperiencesState,
                    recentExperiencesState,
                    onLikeClick,
                    onExperienceClick
                )
            }
        }
    }
}

@Composable
private fun SearchedExperiencesSection(
    searchExperiencesState: List<Experience>,
    onLikeClick: (String) -> Unit,
    onExperienceClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Searched Experiences",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(searchExperiencesState) { item ->
            ExperienceCard(
                experience = item,
                onLikeClick,
                onExperienceClick
            )
        }
    }
}

@Composable
private fun DefaultContent(
    recommendedExperiencesState: List<Experience>,
    recentExperiencesState: List<Experience>,
    onLikeClick: (String) -> Unit,
    onExperienceClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            WelcomeSection()
        }
        item {
            RecommendedExperiencesSection(
                recommendedExperiencesState,
                onLikeClick,
                onExperienceClick
            )
        }
        item {
            RecentExperiencesSection(
                recentExperiencesState,
                onLikeClick,
                onExperienceClick
            )
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Welcome!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Now you can explore any experience in 360 degrees and get all the details about it all in one place",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RecommendedExperiencesSection(
    recommendedExperiencesState: List<Experience>,
    onLikeClick: (String) -> Unit,
    onExperienceClick: (String) -> Unit
) {
    Column {
        Text(
            text = "Recommended Experiences",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(recommendedExperiencesState) { item ->
                ExperienceCard(experience = item, onLikeClick, onExperienceClick)
            }
        }
    }
}
@Composable
private fun RecentExperiencesSection(
    recentExperiencesState: List<Experience>,
    onLikeClick: (String) -> Unit,
    onExperienceClick: (String) -> Unit
) {
    Column {
        Text(
            text = "Most Recent Experiences",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        recentExperiencesState.forEach { item ->
            ExperienceCard(
                experience = item,
                onLikeClick,
                onExperienceClick
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppbar(
    scrollBehavior: TopAppBarScrollBehavior,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            SearchTextField(
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = onSearchTriggered,
                searchQuery = searchQuery,
            )
        },
        modifier = modifier
    )
}

@Composable
fun ExperienceCard(
    experience: Experience,
    onLikeClick: (String) -> Unit,
    onExperienceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .width(350.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable { onExperienceClick(experience.id) },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                ImageSection(experience.imgSrc)
                if (experience.recommended == 1) {
                    Text(
                        "⭐ Recommended",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(4.dp),
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    ExperienceViewCount(experience.numberOfViews)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleSection(experience.title)
            ExperienceLikesSection(experience, onLikeClick)
        }
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (searchQuery.isBlank()) return@onKeyEvent false
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isBlank()) return@KeyboardActions
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
        placeholder = { Text("try Luxor") }
    )
}

@Composable
private fun ImageSection(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)

    )
}

@Composable
private fun TitleSection(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        loadingState = false,
        errorState = null,
        recommendedExperiencesState = listOf(
            Experience(
                "1",
                "Pyramids Tour",
                imgSrc = "https://example.com/image.jpg",
                description = "A tour of the Pyramids",
                numberOfLikes = 1000,
                numberOfViews = 500,
                recommended = 0,
                isLiked = false,
                address = ""
            ),
            Experience(
                "2", "Nile Cruise",
                description = "A cruise through the Nile",
                "https://aroundegypt-production.s3.eu-central-1.amazonaws.com/2/buEw5QAr0MJf8HCLqd3TdlbJ8ETZOP-metaZnZDMUhJMjBNNkM3T1ZaVmVwak1sSWxpdEJPR2VvSzZ1WHJoaVQydC5qcGVn-.jpg?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIASZMRQEMKBKVP4NHO%2F20250303%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Date=20250303T075136Z&X-Amz-SignedHeaders=host&X-Amz-Expires=172800&X-Amz-Signature=75bd1f766a5655f4ec47f1c7af0cf46251acc3314d06d87bcdc06970be753f6",
                2000,
                800, 1, false,
                ""
            )
        ),
        recentExperiencesState = listOf(
            Experience(
                "3",
                "Desert Safari",
                description = "A safari in the desert",
                "https://aroundegypt-production.s3.eu-central-1.amazonaws.com/51/VGaX2MatfRrmisLpAwch92Za4jjuNB-metaYjdtQm9mT2VLOUFWa2hScXFGWmlsUmxOWjlLU0lNdG5NcWM3dnVSUS5qcGVn-.jpg?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIASZMRQEMKBKVP4NHO%2F20250303%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Date=20250303T075136Z&X-Amz-SignedHeaders=host&X-Amz-Expires=172800&X-Amz-Signature=831696b262c5c9799b9f144acb2e3666b8e050aba5e31a899ac0f7396e278866",
                500,
                300, 0, true,
                ""
            )
        ), emptyList(), { "" }, {}, {}
    )
}
