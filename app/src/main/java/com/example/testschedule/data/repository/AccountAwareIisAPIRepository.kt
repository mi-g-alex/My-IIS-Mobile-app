package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.dto.account.headman.create_omissions.HeadmanCreateOmissionsDto
import com.example.testschedule.data.remote.dto.account.settings.email.SendConfirmMessageResponseDto
import com.example.testschedule.data.remote.dto.account.settings.password.ChangePasswordDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.domain.model.account.settings.email.ContactsModel
import com.example.testschedule.domain.model.account.settings.email.ContactsUpdateRequestModel
import com.example.testschedule.domain.model.account.settings.email.SendConfirmCodeRequestModel
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.certificate.CreateCertificateModel
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.CreateMarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.MarkSheetSubjectsModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import okhttp3.ResponseBody
import retrofit2.Call

class AccountAwareIisAPIRepository(
    private val realRepository: IisAPIRepository,
    private val mockRepository: IisAPIRepository,
    private val databaseRepository: UserDatabaseRepository
) : IisAPIRepository by realRepository {

    private fun repositoryFor(cookies: String): IisAPIRepository =
        if (cookies == MockFixtures.COOKIE) mockRepository else realRepository

    private suspend fun currentRepository(): IisAPIRepository =
        repositoryFor(databaseRepository.getCookie())

    override suspend fun getListOfGroups(): List<ListOfGroupsModel> =
        currentRepository().getListOfGroups()

    override suspend fun getListOfEmployees(): List<ListOfEmployeesModel> =
        currentRepository().getListOfEmployees()

    override suspend fun getSchedule(id: String): ScheduleModel =
        currentRepository().getSchedule(id)

    override suspend fun getCurrentWeek(): Int = currentRepository().getCurrentWeek()

    override suspend fun loginToAccount(
        username: String,
        password: String
    ): Call<UserBasicDataDto?> = if (
        username == MockFixtures.credentials.username &&
        password == MockFixtures.credentials.password
    ) {
        mockRepository.loginToAccount(username, password)
    } else {
        realRepository.loginToAccount(username, password)
    }

    override suspend fun getAccountProfile(cookies: String): AccountProfileModel =
        repositoryFor(cookies).getAccountProfile(cookies)

    override suspend fun getNotifications(cookies: String): List<NotificationModel> =
        repositoryFor(cookies).getNotifications(cookies)

    override suspend fun getUnreadNotificationsCount(cookies: String): Int =
        repositoryFor(cookies).getUnreadNotificationsCount(cookies)

    override suspend fun readNotifications(cookies: String, data: List<Int>) =
        repositoryFor(cookies).readNotifications(cookies, data)

    override suspend fun getDormitory(cookies: String): List<DormitoryModel> =
        repositoryFor(cookies).getDormitory(cookies)

    override suspend fun getPrivileges(cookies: String): List<PrivilegesModel> =
        repositoryFor(cookies).getPrivileges(cookies)

    override suspend fun getUserGroup(cookies: String): GroupModel =
        repositoryFor(cookies).getUserGroup(cookies)

    override suspend fun getMarkBook(cookies: String): MarkBookModel =
        repositoryFor(cookies).getMarkBook(cookies)

    override suspend fun getOmissions(cookies: String): List<OmissionsModel> =
        repositoryFor(cookies).getOmissions(cookies)

    override suspend fun getPenalty(cookies: String): List<PenaltyModel> =
        repositoryFor(cookies).getPenalty(cookies)

    override suspend fun getRating(cookies: String): RatingModel =
        repositoryFor(cookies).getRating(cookies)

    override suspend fun getCertificates(cookies: String): List<CertificateModel> =
        repositoryFor(cookies).getCertificates(cookies)

    override suspend fun getNewCertificatePlaces(cookies: String): List<NewCertificatePlacesModel> =
        repositoryFor(cookies).getNewCertificatePlaces(cookies)

    override suspend fun createCertificate(
        request: CreateCertificateModel,
        cookies: String
    ): Call<Any> = repositoryFor(cookies).createCertificate(request, cookies)

    override suspend fun closeCertificate(id: Int, cookies: String) =
        repositoryFor(cookies).closeCertificate(id, cookies)

    override suspend fun getMarkSheets(cookies: String): List<MarkSheetModel> =
        repositoryFor(cookies).getMarkSheets(cookies)

    override suspend fun closeMarkSheet(id: Int, cookies: String) =
        repositoryFor(cookies).closeMarkSheet(id, cookies)

    override suspend fun createMarkSheet(
        request: CreateMarkSheetModel,
        cookies: String
    ): Call<Any> = repositoryFor(cookies).createMarkSheet(request, cookies)

    override suspend fun getMarkSheetTypes(cookies: String): List<MarkSheetTypeModel> =
        repositoryFor(cookies).getMarkSheetTypes(cookies)

    override suspend fun getMarkSheetSubjects(cookies: String): List<MarkSheetSubjectsModel> =
        repositoryFor(cookies).getMarkSheetSubjects(cookies)

    override suspend fun searchEmployeeById(
        thId: Int?,
        focsId: Int?,
        cookies: String
    ): List<SearchEmployeeMarkSheetModel> =
        repositoryFor(cookies).searchEmployeeById(thId, focsId, cookies)

    override suspend fun searchEmployeeByName(name: String): List<SearchEmployeeMarkSheetModel> =
        currentRepository().searchEmployeeByName(name)

    override suspend fun settingsUpdateBio(profile: AccountProfileModel, cookies: String) =
        repositoryFor(cookies).settingsUpdateBio(profile, cookies)

    override suspend fun settingsUpdateSkills(
        skills: List<AccountProfileModel.SkillModel>,
        cookies: String
    ): Call<ResponseBody?> = repositoryFor(cookies).settingsUpdateSkills(skills, cookies)

    override suspend fun settingsUpdateLinks(
        links: List<AccountProfileModel.ReferenceModel>,
        cookies: String
    ): Any = repositoryFor(cookies).settingsUpdateLinks(links, cookies)

    override suspend fun settingsUpdateViewProfile(profile: AccountProfileModel, cookies: String) =
        repositoryFor(cookies).settingsUpdateViewProfile(profile, cookies)

    override suspend fun settingsUpdateViewRating(profile: AccountProfileModel, cookies: String) =
        repositoryFor(cookies).settingsUpdateViewRating(profile, cookies)

    override suspend fun settingsUpdateViewJob(profile: AccountProfileModel, cookies: String) =
        repositoryFor(cookies).settingsUpdateViewJob(profile, cookies)

    override suspend fun settingsUpdatePassword(
        password: ChangePasswordDto,
        cookies: String
    ): Call<ResponseBody?> = repositoryFor(cookies).settingsUpdatePassword(password, cookies)

    override suspend fun settingsEmailGetContacts(cookies: String): ContactsModel =
        repositoryFor(cookies).settingsEmailGetContacts(cookies)

    override suspend fun settingsEmailUpdate(
        mail: ContactsUpdateRequestModel,
        cookies: String
    ): Call<ResponseBody?> = repositoryFor(cookies).settingsEmailUpdate(mail, cookies)

    override suspend fun settingsEmailGetConfirmCode(
        id: Int,
        cookies: String
    ): Call<SendConfirmMessageResponseDto> =
        repositoryFor(cookies).settingsEmailGetConfirmCode(id, cookies)

    override suspend fun settingsEmailConfirmMessage(
        code: SendConfirmCodeRequestModel,
        cookies: String
    ): Call<ResponseBody?> = repositoryFor(cookies).settingsEmailConfirmMessage(code, cookies)

    override suspend fun settingsUpdatePhoto(
        base64: String,
        cookies: String
    ): Call<String?> = repositoryFor(cookies).settingsUpdatePhoto(base64, cookies)

    override suspend fun headmanGetOmissionsByDate(
        date: String,
        cookies: String
    ): HeadmanGetOmissionsModel = repositoryFor(cookies).headmanGetOmissionsByDate(date, cookies)

    override suspend fun headmanSaveOmissions(
        omissions: HeadmanCreateOmissionsDto,
        cookies: String
    ): Call<ResponseBody?> = repositoryFor(cookies).headmanSaveOmissions(omissions, cookies)
}
