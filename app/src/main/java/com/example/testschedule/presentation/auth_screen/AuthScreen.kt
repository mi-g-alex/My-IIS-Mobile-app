package com.example.testschedule.presentation.auth_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R

@Composable
fun AuthScreen(
    goBack: () -> Unit,
    goToProfile: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var isNavNext by remember { mutableStateOf(true) }
    var state by remember { mutableStateOf(StateOfLoginScreen.INPUT_LOGIN) }
    var showPasswordState by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isValidLogin by remember { mutableStateOf(false) }
    var isValidPassword by remember { mutableStateOf(false) }

    var photoUrl by remember { mutableStateOf<String?>(null) }
    //var saveLoginBox by remember { mutableStateOf(true) }
    var addGroupToSchedulesBox by remember { mutableStateOf(true) }
    var setGroupByDefaultBox by remember { mutableStateOf(true) }
    val isAlreadySavedGroup = viewModel.isAlreadyAdded
    val isAlreadySetDefault = viewModel.isAlreadySetDefault


    // INPUT LOGIN STATE
    Scaffold(
        topBar = {
            TopAuthBar(
                stringResource(id = R.string.login_main_title),
                goBack,
                viewModel.isLoading.value
            )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LaunchedEffect(loginText) {
                val regex = Regex("^\\d{8}$")
                isValidLogin = regex.containsMatchIn(loginText)
            }

            // Input Login Fragment
            AnimatedVisibility(
                visible = state == StateOfLoginScreen.INPUT_LOGIN,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                enter = slideInHorizontally(
                    initialOffsetX = { pos -> if (isNavNext) pos else -pos }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { pos -> if (isNavNext) -pos else pos }
                ),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.login_title),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = loginText,
                            onValueChange = { text ->
                                loginText = text.filter { c -> c in ('0'..'9') }
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = true,
                            readOnly = false,
                            isError = !isValidLogin,
                            supportingText = {
                                Text(stringResource(id = R.string.login_support_text))
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (isValidLogin) {
                                        isNavNext = true
                                        state = StateOfLoginScreen.INPUT_PASSWORD
                                        keyboardController?.hide()
                                    }
                                }
                            ),
                            singleLine = true
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                isNavNext = true
                                state = StateOfLoginScreen.INPUT_PASSWORD
                                keyboardController?.hide()
                            },
                            enabled = isValidLogin,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.login_next_stage),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }

            // Input Password Fragment
            LaunchedEffect(passwordText) {
                isValidPassword = passwordText.length > 7
            }

            AnimatedVisibility(
                visible = state == StateOfLoginScreen.INPUT_PASSWORD,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                enter = slideInHorizontally(
                    initialOffsetX = { pos -> if (isNavNext) pos else -pos }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { pos -> if (isNavNext) -pos else pos }
                ),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = loginText,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        IconButton(
                            onClick = {
                                isNavNext = false
                                state = StateOfLoginScreen.INPUT_LOGIN
                                passwordText = ""
                            }
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                stringResource(id = R.string.login_edit_button)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = passwordText,
                            onValueChange = { text ->
                                passwordText = text
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = true,
                            readOnly = false,
                            isError = viewModel.errorText.value.isNotEmpty(),
                            supportingText = {
                                if (viewModel.errorText.value.isNotEmpty()) {
                                    val text = when (viewModel.errorText.value) {
                                        "WrongPassword" -> stringResource(id = R.string.login_error_wrong_password)
                                        "ConnectionFailed" -> stringResource(id = R.string.login_error_connection_fail)
                                        else -> stringResource(id = R.string.login_error_other)
                                    }
                                    Text(text)
                                } else {
                                    Text("")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    if (isValidPassword && !viewModel.isLoading.value)
                                        viewModel.loginToAccount(
                                            username = loginText,
                                            password = passwordText
                                        )
                                }
                            ),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = { showPasswordState = !showPasswordState }) {
                                    if (!showPasswordState) {
                                        Icon(
                                            painterResource(id = R.drawable.password_hide),
                                            stringResource(id = R.string.password_show_title),
                                        )
                                    } else {
                                        Icon(
                                            painterResource(id = R.drawable.password_show),
                                            stringResource(id = R.string.password_hide_title),
                                        )
                                    }
                                }
                            },
                            visualTransformation =
                            if (!showPasswordState) PasswordVisualTransformation() else VisualTransformation.None
                        )
                    }
                    /*Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = saveLoginBox,
                            onCheckedChange = { saveLoginBox = !saveLoginBox })
                        Text(
                            text = stringResource(id = R.string.login_save_login_box),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }*/
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.loginToAccount(
                                    username = loginText,
                                    password = passwordText
                                )
                            },
                            modifier = Modifier.padding(vertical = 8.dp),
                            enabled = isValidPassword && !viewModel.isLoading.value
                        ) {
                            Text(
                                stringResource(id = R.string.login_to_account_button),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }

            LaunchedEffect(viewModel.resultData.value) {
                if (viewModel.resultData.value != null) {
                    state = StateOfLoginScreen.PROFILE_VIEW
                    photoUrl = viewModel.resultData.value?.photoUrl ?: ""
                    viewModel.resultData.value?.group?.let { it1 ->
                        viewModel.checkIsSaved(it1)
                    }
                }
            }

            viewModel.resultData.value?.let { res ->
                AnimatedVisibility(
                    visible = state == StateOfLoginScreen.PROFILE_VIEW,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    enter = slideInHorizontally(
                        initialOffsetX = { pos -> if (isNavNext) pos else -pos }
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { pos -> if (isNavNext) -pos else pos }
                    ),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(25.dp)
                    ) {
                        Row {
                            Box(
                                Modifier
                                    .size(48.dp)
                                    .padding(bottom = 8.dp, end = 8.dp)
                            ) {
                                var showIcon by remember { mutableStateOf(true) }
                                if (showIcon) {
                                    Icon(
                                        Icons.Outlined.AccountCircle,
                                        null,
                                        Modifier.size(48.dp)
                                    )
                                }
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photoUrl)
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(48.dp),
                                    onSuccess = { showIcon = false }
                                )
                            }
                            Column {
                                Text(
                                    text = res.fio,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.login_group_info,
                                        res.group
                                    ),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = addGroupToSchedulesBox,
                                onCheckedChange = {
                                    addGroupToSchedulesBox = !addGroupToSchedulesBox
                                },
                                enabled = !isAlreadySavedGroup.value
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.login_add_schedule_box,
                                    res.group
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = setGroupByDefaultBox,
                                onCheckedChange = { setGroupByDefaultBox = !setGroupByDefaultBox },
                                enabled = addGroupToSchedulesBox && !isAlreadySetDefault.value
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.login_set_by_default_box,
                                    res.group
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    if (addGroupToSchedulesBox && !isAlreadySavedGroup.value)
                                        viewModel.addGroupToSaved(res.group)
                                    if (setGroupByDefaultBox && addGroupToSchedulesBox && !isAlreadySetDefault.value)
                                        viewModel.setOpenByDefault(res.group)
                                    goToProfile()
                                },
                                modifier = Modifier.padding(vertical = 8.dp),
                                enabled = isValidPassword && !viewModel.isLoading.value
                            ) {
                                Text(
                                    stringResource(id = R.string.login_go_to_profile),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class StateOfLoginScreen {
    INPUT_LOGIN, INPUT_PASSWORD, PROFILE_VIEW
}

@Composable
fun TopAuthBar(title: String, goBack: () -> Unit, isLoading: Boolean) = TopAppBar(
    title = {
        Text(text = title)
    },
    navigationIcon = {
        IconButton(onClick = { goBack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
    },
    actions = {
        if (isLoading)
            CircularProgressIndicator()
    }
)


