package com.example.testschedule.presentation.account.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.testschedule.R
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.settings.additional.BasicListItem
import com.example.testschedule.presentation.account.settings.additional.ButtonEmailUnconfirmedItem
import com.example.testschedule.presentation.account.settings.additional.DialogType
import com.example.testschedule.presentation.account.settings.additional.Space
import com.example.testschedule.presentation.account.settings.additional.WithCheckBoxListItem
import com.example.testschedule.presentation.account.settings.features.DialogBio
import com.example.testschedule.presentation.account.settings.features.DialogChangeEmail
import com.example.testschedule.presentation.account.settings.features.DialogConfirmEmail
import com.example.testschedule.presentation.account.settings.features.DialogLinks
import com.example.testschedule.presentation.account.settings.features.DialogOutlook
import com.example.testschedule.presentation.account.settings.features.DialogPassword
import com.example.testschedule.presentation.account.settings.features.DialogSkills
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    emailViewModel: EmailSettingViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    var enabled by remember { mutableStateOf(true) }
    LaunchedEffect(viewModel.errorText.value, emailViewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword" || emailViewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }

    /** photo | skills | links | settings | outlook **/
    val userAccountData by remember { viewModel.userInfo }

    /** phone | email | fio **/
    val userBasicData by remember { viewModel.basicInfo }

    var canViewProfile by remember { viewModel.canViewProfile }
    var canViewRating by remember { viewModel.canViewRating }
    var canSearchJob by remember { viewModel.canSearchJob }

    var selectedDialog by remember { viewModel.selectedDialog }

    val clipboardManager = LocalClipboardManager.current
    val copiedText = stringResource(id = R.string.copied)

    val toast = fun(text: String) {
        Toast.makeText(cnt, text, Toast.LENGTH_SHORT).show()
    }

    val copy = fun(text: String) {
        clipboardManager.setText(AnnotatedString(text))
        toast(copiedText)
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val showSnack = fun(text: String) = scope.launch {
        snackBarHostState.showSnackbar(
            message = text,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }

//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    val context = LocalContext.current
//    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? -> imageUri = uri }
//
//    LaunchedEffect(imageUri) {
//        imageUri?.let {
//            if (Build.VERSION.SDK_INT < 28) {
//                bitmap.value = MediaStore.Images
//                    .Media.getBitmap(context.contentResolver, it)
//
//            } else {
//                val source = ImageDecoder
//                    .createSource(context.contentResolver, it)
//                bitmap.value = ImageDecoder.decodeBitmap(source)
//            }
//        }
//        if(bitmap.value != null) viewModel.updatePhoto(bitmap.value!!)
//    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            BasicTopBar(
                onBackPressed = { onBackPressed(); enabled = false },
                title = stringResource(id = R.string.account_settings_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty()
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {

        val passWrongPass = stringResource(id = R.string.login_error_wrong_password)
        val passConnection =
            stringResource(id = R.string.account_settings_password_error_connection)
        val passOther = stringResource(id = R.string.account_settings_password_error_other)
        val passOk = stringResource(id = R.string.account_settings_password_success)
        val bioOk = stringResource(id = R.string.account_settings_info_bio_success)
        val bioOther = stringResource(id = R.string.account_settings_info_bio_error_other)
        val bioConnection = stringResource(id = R.string.account_settings_info_bio_error_connection)

        val skillsOk = stringResource(id = R.string.account_settings_info_skills_success)
        val skillsOther = stringResource(id = R.string.account_settings_info_skills_error_other)
        val skillsConnection =
            stringResource(id = R.string.account_settings_info_skills_error_connection)

        val linksOk = stringResource(id = R.string.account_settings_info_links_success)
        val linksOther = stringResource(id = R.string.account_settings_info_links_error_other)
        val linksConnection =
            stringResource(id = R.string.account_settings_info_links_error_connection)

        val emailOk = stringResource(id = R.string.account_settings_email_update_success)
        val emailOther = stringResource(id = R.string.account_settings_email_update_error_other)
        val emailConnection =
            stringResource(id = R.string.account_settings_email_update_error_connection)

        val confirmOk = stringResource(id = R.string.account_settings_email_confirm_success)
        val confirmOther = stringResource(id = R.string.account_settings_email_confirm_error_other)
        val confirmConnection =
            stringResource(id = R.string.account_settings_email_confirm_error_connection)
        val confirmCode =
            stringResource(id = R.string.account_settings_email_confirm_error_wrong_code)

        var expTime by remember { emailViewModel.expiredTime }

        if (userAccountData != null) {

            when (selectedDialog) {
                DialogType.OUTLOOK -> DialogOutlook(
                    login = userAccountData?.outlookLogin.toString(),
                    password = userAccountData?.outlookPassword.toString(),
                    copy = copy
                ) { selectedDialog = DialogType.NONE }

                DialogType.CONFIRM_EMAIL -> {
                    DialogConfirmEmail(
                        onGetCodeClicked = {
                            emailViewModel.getCode()
                        },
                        onSendCodeClicked = { code ->
                            emailViewModel.confirmCode(code, onSuccess = {
                                viewModel.confirmedEmail()
                                selectedDialog = DialogType.NONE
                                showSnack(confirmOk)
                            }) {
                                when (emailViewModel.errorTextForDialog.value) {
                                    "ConnectionFailed" -> toast(confirmConnection)
                                    "WRONG_CODE" -> toast(confirmCode)
                                    else -> toast(confirmOther)
                                }
                            }
                        },
                        errorString = emailViewModel.errorTextForDialog.value,
                        leftTime = expTime,
                        numberOfAttempt = emailViewModel.numberOfAttempts.intValue
                    ) {
                        selectedDialog = DialogType.NONE
                        expTime = 0L
                        emailViewModel.errorTextForDialog.value = ""
                        emailViewModel.errorText.value = ""
                    }
                }

                DialogType.EMAIL -> {
                    DialogChangeEmail(
                        onSaveClick = { m ->
                            emailViewModel.updateEmail(m, onError = {
                                if (emailViewModel.errorText.value == "ConnectionFailed")
                                    toast(emailConnection)
                                if (emailViewModel.errorText.value == "OtherError")
                                    toast(emailOther)
                            }) {
                                selectedDialog = DialogType.NONE
                                showSnack("$emailOk $m")
                                viewModel.updateEmail(m)
                            }
                        },
                        curEmail = emailViewModel.email.value.let { m ->
                            m.ifBlank {
                                userBasicData?.email ?: ""
                            }
                        },
                        errorText = emailViewModel.errorTextForDialog.value
                    ) {
                        selectedDialog = DialogType.NONE
                        emailViewModel.errorTextForDialog.value = ""
                    }
                }

                DialogType.PASSWORD -> {
                    DialogPassword(
                        onSaveClick = { op, np ->
                            viewModel.updatePassword(
                                op,
                                np,
                                onSuccess = {
                                    selectedDialog = DialogType.NONE; showSnack(passOk)
                                    viewModel.errorPassText.value = ""
                                },
                                onWrongOldPassword = {
                                    toast(passWrongPass)
                                    selectedDialog = DialogType.PASSWORD
                                },
                                onError = { isNetwork ->
                                    toast(if (isNetwork) passConnection else passOther)
                                    selectedDialog = DialogType.PASSWORD
                                }
                            )
                        },
                        errorText = viewModel.errorPassText.value,
                        isLoading = viewModel.isLoadingPass.value,
                    ) { selectedDialog = DialogType.NONE; viewModel.errorPassText.value = "" }
                }

                DialogType.BIO -> {
                    DialogBio(
                        onSaveClick = { bio ->
                            viewModel.updateBio(
                                bio,
                                onSuccess = {
                                    selectedDialog = DialogType.NONE
                                    showSnack(bioOk + bio)
                                    viewModel.errorBioText.value = ""
                                },
                                onError = { isNetwork ->
                                    toast(if (isNetwork) bioConnection else bioOther)
                                    selectedDialog = DialogType.BIO
                                }
                            )
                        },
                        text = userAccountData?.bio ?: "",
                        isLoading = viewModel.isLoadingBio.value,
                    ) { selectedDialog = DialogType.NONE; viewModel.errorBioText.value = "" }
                }

                DialogType.LINKS -> {
                    DialogLinks(
                        onSaveClick = { links ->
                            viewModel.updateLinks(
                                links,
                                onSuccess = {
                                    selectedDialog = DialogType.NONE
                                    showSnack(linksOk)
                                    viewModel.errorLinksText.value = ""
                                },
                                onError = { isNetwork ->
                                    toast(if (isNetwork) linksConnection else linksOther)
                                    selectedDialog = DialogType.LINKS
                                }
                            )
                        },
                        curLinks = userAccountData?.references ?: emptyList(),
                        isLoading = viewModel.isLoadingLinks.value,
                        copy = copy,
                        toast = toast
                    ) { selectedDialog = DialogType.NONE; viewModel.errorLinksText.value = "" }
                }

                DialogType.SKILLS -> {
                    DialogSkills(
                        onSaveClick = { skills ->
                            viewModel.updateSkills(
                                skills,
                                onSuccess = {
                                    selectedDialog = DialogType.NONE
                                    showSnack(skillsOk)
                                    viewModel.errorSkillsText.value = ""
                                },
                                onError = { isNetwork ->
                                    toast(if (isNetwork) skillsConnection else skillsOther)
                                    selectedDialog = DialogType.SKILLS
                                }
                            )
                        },
                        curSkills = userAccountData?.skills ?: emptyList(),
                        isLoading = viewModel.isLoadingSkills.value,
                        copy = copy,
                        toast = toast
                    ) { selectedDialog = DialogType.NONE; viewModel.errorSkillsText.value = "" }
                }

                DialogType.NONE -> {}
            }

            Column {

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

                    stickyHeader {
                        AnimatedVisibility(
                            visible = userBasicData?.hasNotConfirmedContact == true,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                ButtonEmailUnconfirmedItem {
                                    selectedDialog = DialogType.CONFIRM_EMAIL
                                }
                            }
                        }
                    }

//                    // Picture
//                    item {
//                        ChangePhotoItem(photoUrl = userAccountData?.photoUrl ?: "") {
//                      //      launcher.launch("image/*")
//                        }
//                    }

                    // Email
                    item {
                        val email = if (emailViewModel.isLoading.value)
                            stringResource(id = R.string.account_settings_email_loading)
                        else
                            emailViewModel.email.value.let { m ->
                                m.ifBlank {
                                    userBasicData?.email ?: ""
                                }
                            }

                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_email),
                            descText = stringResource(id = R.string.account_settings_email_desc),
                            additionalText = stringResource(
                                id = R.string.account_settings_email_info,
                                email
                            )
                        ) { selectedDialog = DialogType.EMAIL }
                    }

                    // Phone
                    item {
                        val t = stringResource(id = R.string.account_settings_phone_desc)
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_phone),
                            descText = t,
                            additionalText = userBasicData?.phone?.let { data ->
                                stringResource(id = R.string.account_settings_phone_info, data)
                            } ?: ""
                        ) { showSnack(t) }
                    }

                    // Password
                    item {
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_password),
                            descText = stringResource(id = R.string.account_settings_password_desc),
                        ) { selectedDialog = DialogType.PASSWORD }
                    }

                    // Spacer
                    item { Space() }

                    // BIO
                    item {
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_info_bio),
                            descText = stringResource(id = R.string.account_settings_info_bio_desc),
                            additionalText = if (userAccountData?.bio?.isNotEmpty() == true)
                                stringResource(
                                    id = R.string.account_settings_info_bio_info,
                                    userAccountData?.bio.toString()
                                ) else null
                        ) { selectedDialog = DialogType.BIO }
                    }

                    // Links
                    item {
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_info_links),
                            descText = stringResource(id = R.string.account_settings_info_links_desc),
                        ) { selectedDialog = DialogType.LINKS }
                    }

                    // Skills
                    item {
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_info_skills),
                            descText = stringResource(id = R.string.account_settings_info_skills_desc),
                        ) { selectedDialog = DialogType.SKILLS }
                    }

                    // Spacer
                    item { Space() }

                    // Settings | Profile
                    item {
                        val sucText = successUpdateText(
                            R.string.account_settings_access_profile,
                            stringResource(id = if (!canViewProfile) R.string.account_settings_access_snackbar_allow else R.string.account_settings_access_snackbar_dont_allow)
                        )

                        val errText = errorUpdateText(R.string.account_settings_access_profile)

                        WithCheckBoxListItem(
                            mainText = stringResource(id = R.string.account_settings_access_profile),
                            descText = stringResource(id = R.string.account_settings_access_profile_desc),
                            canViewProfile,
                            !viewModel.isLoading.value
                        ) {
                            viewModel.updateViewInfo(
                                viewProfile = !canViewProfile,
                                viewRating = null,
                                viewJob = null,
                                onSuccess = {
                                    showSnack(sucText); canViewProfile = !canViewProfile
                                },
                                onError = { showSnack(errText) }
                            )
                        }
                    }

                    // Settings | Rating
                    item {

                        val sucText = successUpdateText(
                            R.string.account_settings_access_rating,
                            stringResource(id = if (!canViewRating) R.string.account_settings_access_snackbar_allow else R.string.account_settings_access_snackbar_dont_allow)
                        )

                        val errText = errorUpdateText(R.string.account_settings_access_rating)

                        WithCheckBoxListItem(
                            mainText = stringResource(id = R.string.account_settings_access_rating),
                            descText = stringResource(id = R.string.account_settings_access_rating_desc),
                            canViewRating,
                            canViewProfile && !viewModel.isLoading.value
                        ) {
                            viewModel.updateViewInfo(
                                viewProfile = null,
                                viewRating = !canViewRating,
                                viewJob = null,
                                onSuccess = {
                                    showSnack(sucText); canViewRating = !canViewRating
                                },
                                onError = { showSnack(errText) }
                            )
                        }
                    }

                    // Settings | Work
                    item {

                        val sucText = successUpdateText(
                            R.string.account_settings_access_work,
                            stringResource(id = if (!canSearchJob) R.string.account_settings_access_snackbar_allow else R.string.account_settings_access_snackbar_dont_allow)
                        )

                        val errText = errorUpdateText(R.string.account_settings_access_work)

                        WithCheckBoxListItem(
                            mainText = stringResource(id = R.string.account_settings_access_work),
                            descText = stringResource(id = R.string.account_settings_access_work_desc),
                            canSearchJob,
                            canViewProfile && !viewModel.isLoading.value
                        ) {
                            viewModel.updateViewInfo(
                                viewProfile = null,
                                viewRating = null,
                                viewJob = !canSearchJob,
                                onSuccess = { showSnack(sucText); canSearchJob = !canSearchJob },
                                onError = { showSnack(errText) }
                            )
                        }
                    }

                    // Spacer
                    item { Space() }

                    // Office info
                    item {
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_outlook),
                            descText = stringResource(id = R.string.account_settings_outlook_desc),
                        ) { selectedDialog = DialogType.OUTLOOK }
                    }

                    // Spacer
                    item { Space() }

                    // Clear cache
                    item {
                        val textDone =
                            stringResource(id = R.string.account_settings_advanced_clear_cache_done)
                        BasicListItem(
                            mainText = stringResource(id = R.string.account_settings_advanced_clear_cache),
                            descText = stringResource(id = R.string.account_settings_advanced_clear_cache_desc),
                        ) {
                            coil.ImageLoader(cnt).diskCache?.clear()
                            coil.ImageLoader(cnt).memoryCache?.clear()
                            toast(textDone)
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun successUpdateText(id: Int, newValueText: String): String = stringResource(
    id = R.string.account_settings_access_snackbar_success,
    stringResource(id = id),
    newValueText
)

@Composable
fun errorUpdateText(id: Int) = stringResource(
    id = R.string.account_settings_access_snackbar_error,
    stringResource(id = id),
)