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
import java.io.IOException
import okhttp3.Headers
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MockIisAPIRepository : IisAPIRepository {

    override suspend fun getListOfGroups(): List<ListOfGroupsModel> = MockFixtures.groups

    override suspend fun getListOfEmployees(): List<ListOfEmployeesModel> =
        MockFixtures.scheduleEmployees

    override suspend fun getSchedule(id: String): ScheduleModel = MockFixtures.schedule.copy(
        id = id.ifBlank { MockFixtures.schedule.id },
        title = id.ifBlank { MockFixtures.schedule.title }
    )

    override suspend fun getCurrentWeek(): Int = 1

    override suspend fun loginToAccount(
        username: String,
        password: String
    ): Call<UserBasicDataDto?> = successfulCall(
        body = MockFixtures.basicUserDto,
        headers = Headers.headersOf("Set-Cookie", MockFixtures.COOKIE)
    )

    override suspend fun getAccountProfile(cookies: String): AccountProfileModel =
        MockFixtures.profile

    override suspend fun getNotifications(cookies: String): List<NotificationModel> =
        MockFixtures.notifications

    override suspend fun getUnreadNotificationsCount(cookies: String): Int =
        MockFixtures.notifications.count { !it.isViewed }

    override suspend fun readNotifications(cookies: String, data: List<Int>) = Unit

    override suspend fun getDormitory(cookies: String): List<DormitoryModel> =
        MockFixtures.dormitory

    override suspend fun getPrivileges(cookies: String): List<PrivilegesModel> =
        MockFixtures.privileges

    override suspend fun getUserGroup(cookies: String): GroupModel = MockFixtures.group

    override suspend fun getMarkBook(cookies: String): MarkBookModel = MockFixtures.markBook

    override suspend fun getOmissions(cookies: String): List<OmissionsModel> =
        MockFixtures.omissions

    override suspend fun getPenalty(cookies: String): List<PenaltyModel> = MockFixtures.penalties

    override suspend fun getRating(cookies: String): RatingModel = MockFixtures.rating

    override suspend fun getCertificates(cookies: String): List<CertificateModel> =
        MockFixtures.certificates

    override suspend fun getNewCertificatePlaces(cookies: String): List<NewCertificatePlacesModel> =
        MockFixtures.certificatePlaces

    override suspend fun createCertificate(
        request: CreateCertificateModel,
        cookies: String
    ): Call<Any> = successfulCall(Any())

    override suspend fun closeCertificate(id: Int, cookies: String) = Unit

    override suspend fun getMarkSheets(cookies: String): List<MarkSheetModel> =
        MockFixtures.markSheets

    override suspend fun closeMarkSheet(id: Int, cookies: String) = Unit

    override suspend fun createMarkSheet(
        request: CreateMarkSheetModel,
        cookies: String
    ): Call<Any> = successfulCall(Any())

    override suspend fun getMarkSheetTypes(cookies: String): List<MarkSheetTypeModel> =
        MockFixtures.markSheetTypes

    override suspend fun getMarkSheetSubjects(cookies: String): List<MarkSheetSubjectsModel> =
        MockFixtures.markSheetSubjects

    override suspend fun searchEmployeeById(
        thId: Int?,
        focsId: Int?,
        cookies: String
    ): List<SearchEmployeeMarkSheetModel> = MockFixtures.employees

    override suspend fun searchEmployeeByName(name: String): List<SearchEmployeeMarkSheetModel> =
        MockFixtures.employees.filter { name.isBlank() || it.fio.contains(name, ignoreCase = true) }

    override suspend fun settingsUpdateBio(profile: AccountProfileModel, cookies: String) = Unit

    override suspend fun settingsUpdateSkills(
        skills: List<AccountProfileModel.SkillModel>,
        cookies: String
    ): Call<ResponseBody?> = successfulCall(null)

    override suspend fun settingsUpdateLinks(
        links: List<AccountProfileModel.ReferenceModel>,
        cookies: String
    ): Any = Unit

    override suspend fun settingsUpdateViewProfile(
        profile: AccountProfileModel,
        cookies: String
    ) = Unit

    override suspend fun settingsUpdateViewRating(
        profile: AccountProfileModel,
        cookies: String
    ) = Unit

    override suspend fun settingsUpdateViewJob(
        profile: AccountProfileModel,
        cookies: String
    ) = Unit

    override suspend fun settingsUpdatePassword(
        password: ChangePasswordDto,
        cookies: String
    ): Call<ResponseBody?> = successfulCall(null)

    override suspend fun settingsEmailGetContacts(cookies: String): ContactsModel =
        MockFixtures.contacts

    override suspend fun settingsEmailUpdate(
        mail: ContactsUpdateRequestModel,
        cookies: String
    ): Call<ResponseBody?> = successfulCall(null)

    override suspend fun settingsEmailGetConfirmCode(
        id: Int,
        cookies: String
    ): Call<SendConfirmMessageResponseDto> = successfulCall(
        SendConfirmMessageResponseDto("2026-09-06T12:10:00.000Z")
    )

    override suspend fun settingsEmailConfirmMessage(
        code: SendConfirmCodeRequestModel,
        cookies: String
    ): Call<ResponseBody?> = successfulCall(null)

    override suspend fun settingsUpdatePhoto(
        base64: String,
        cookies: String
    ): Call<String?> = successfulCall(null)

    override suspend fun headmanGetOmissionsByDate(
        date: String,
        cookies: String
    ): HeadmanGetOmissionsModel = MockFixtures.headman(date)

    override suspend fun headmanSaveOmissions(
        omissions: HeadmanCreateOmissionsDto,
        cookies: String
    ): Call<ResponseBody?> = successfulCall(null)
}

private fun <T> successfulCall(
    body: T,
    headers: Headers = Headers.Builder().build()
): Call<T> = MockCall(Response.success(body, headers))

private class MockCall<T>(private val response: Response<T>) : Call<T> {
    private var executed = false
    private var canceled = false

    override fun execute(): Response<T> {
        check(!executed) { "Already executed" }
        executed = true
        if (canceled) throw IOException("Canceled")
        return response
    }

    override fun enqueue(callback: Callback<T>) {
        check(!executed) { "Already executed" }
        executed = true
        if (canceled) {
            callback.onFailure(this, IOException("Canceled"))
        } else {
            callback.onResponse(this, response)
        }
    }

    override fun isExecuted(): Boolean = executed

    override fun cancel() {
        canceled = true
    }

    override fun isCanceled(): Boolean = canceled

    override fun clone(): Call<T> = MockCall(response)

    override fun request(): Request = Request.Builder()
        .url("https://mock.local/")
        .build()

    override fun timeout(): Timeout = Timeout.NONE
}
