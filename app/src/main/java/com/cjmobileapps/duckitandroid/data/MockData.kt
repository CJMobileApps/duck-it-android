package com.cjmobileapps.duckitandroid.data

import com.cjmobileapps.duckitandroid.data.model.AccountState
import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.Error
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
import com.cjmobileapps.duckitandroid.data.model.Post
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.ResponseWrapper
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.data.model.Upvotes
import kotlinx.coroutines.CompletableDeferred
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.UUID

object MockData {

    val mockNewPostRequest = NewPostRequest(
        headline = "Daffy Duck",
        image = "https://upload.wikimedia.org/wikipedia/en/thumb/f/f4/Daffy_Duck.svg/1200px-Daffy_Duck.svg.png"
    )

    val mockEmailPasswordRequest = EmailPasswordRequest(
        email = "johnsmith@duckit.com",
        password = "password"
    )

    const val mockToken =
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huc21pdGhAZHVja2l0LmNvbSJ9.F0v_iQya0HIxUroq6Z-CsIqePbLvHCUoyvVHSfpO1vsXSpDdFa9OfXE6vtrVgcnOykVIB8A0yJoGZuB83mmfOQ"

    private val mockTokenResponse = TokenResponse(
        token = mockToken
    )

    val mockTokenResponseErrorHttpForbidden: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_FORBIDDEN,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpBadRequest: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_BAD_REQUEST,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpNotFound: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_NOT_FOUND,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockUnitErrorHttpNotFound: Response<Unit> = Response.error(
        HttpURLConnection.HTTP_NOT_FOUND,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpConflict: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_CONFLICT,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseSuccess: Response<TokenResponse> = Response.success(mockTokenResponse)

    val mockDeferredTokenResponseSuccess = CompletableDeferred(mockTokenResponseSuccess)

    val mockUnitResponseSuccess: Response<Unit> = Response.success(200, Unit!!)

    val mockDeferredUnitResponseSuccess = CompletableDeferred(mockUnitResponseSuccess)

    val mockAccountStateAccountSignedInResponseWrapper = ResponseWrapper(
        data = AccountState.AccountSignedIn
    )

    val mockAccountStateAccountCreatedResponseWrapper = ResponseWrapper(
        data = AccountState.AccountCreated
    )

    val mockAccountStateErrorPasswordIncorrectResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "Password Incorrect"
        )
    )

    val mockAccountStateGenericErrorResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "There was a problem"
        )
    )

    val mockAccountStateGenericErrorNoMessageResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = ""
        )
    )

    val mockAccountStateAccountAlreadyExistsResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "Account Already Exists"
        )
    )

    val mockAccountNotLoggedInResponseWrapper = ResponseWrapper<Boolean>(
        error = Error(
            isError = true,
            message = "Account Not Logged In"
        )
    )

    val mockAccountBooleanStateGenericErrorResponseWrapper = ResponseWrapper<Boolean>(
        error = Error(
            isError = true,
            message = "There was a problem"
        )
    )

    private val mockPostList = listOf(
        Post(
            id = "Eg8KBFBvc3QQgICAmJSxkgo",
            headline = "L",
            image = "https://www.peta.org/wp-content/uploads/2016/06/iStock_000015316532_thierry-vialard.jpg",
            upvotes = 70,
            author = "co@gmail.com"
        ),
        Post(
            id = "Eg8KBFBvc3QQgICAmKbXgAo",
            headline = "Big ducky",
            image = "https://media.npr.org/assets/img/2013/05/06/ducky062way-23e257dfd081032928ffbd73768a7ddd8615f1f3-s1600-c85.webp",
            upvotes = 20,
            author = "co@gmail.com"
        ),
        Post(
            id = "Eg8KBFBvc3QQgICAuIiRgwo",
            headline = "ginj",
            image = "https://www.desktopbackground.org/p/2011/12/31/321050_donald-duck-cartoon-wallpapers-image-for-android-cartoons-wallpapers_1600x2200_h.jpg",
            upvotes = 18,
            author = "abc@gmail.coma"
        ),
        Post(
            id = "Eg8KBFBvc3QQgICAmMX2ggo",
            headline = "t",
            image = "https://www.desktopbackground.org/p/2011/12/31/321050_donald-duck-cartoon-wallpapers-image-for-android-cartoons-wallpapers_1600x2200_h.jpg",
            upvotes = 6,
            author = "abc@gmail.coma"
        )
    )

    private val mockPosts = Posts(posts = mockPostList)

    val mockPostResponseSuccess: Response<Posts> = Response.success(mockPosts)

    val mockDeferredPostResponseSuccess = CompletableDeferred(mockPostResponseSuccess)

    val mockPostId = mockPostList.first().id!!

    private val mockUpVotes = Upvotes(upvotes = 70)

    val mockUpVotesResponseSuccess: Response<Upvotes> = Response.success(mockUpVotes)

    val mockDeferredUpVotesResponseSuccess = CompletableDeferred(mockUpVotesResponseSuccess)

    val mockTrueResponseWrapper = ResponseWrapper(
        data = true
    )
}
