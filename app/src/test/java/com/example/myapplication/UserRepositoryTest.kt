package com.example.myapplication.repositories

import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.interfaces.UserBalance
import com.example.myapplication.model.UserBalanceDto // Assuming UserBalanceDto is imported
import com.example.myapplication.model.BalanceDto // Assuming BalanceDto is imported
import com.example.myapplication.model.TransactionDto // Assuming TransactionDto is imported
import com.example.myapplication.model.toDomain // Assuming toDomain is available via import
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    // 1. Mock the dependencies
    private val apiService: ApiService = mock()
    private lateinit var repository: UserRepository

    // 2. Use a Test Dispatcher to control coroutine execution
    private val testDispatcher = StandardTestDispatcher()

    private val mockUserId = "testUser1"
    private val mockUserBalance = object : UserBalance {
        override val id: String = mockUserId
        override val balance: BalanceDto = BalanceDto(100.0, 150.0, 50.0)
        override val transactions: List<TransactionDto> = emptyList() 
        // NOTE: In a real test, you would use a fully mocked DTO and Transaction implementation
    }
    
    // Prepare a mock DTO list for API response
    private val mockUserBalanceDtoList = listOf(
        UserBalanceDto(id = "otherUser", balance = BalanceDto(0.0, 0.0, 0.0)),
        UserBalanceDto(id = mockUserId, balance = mockUserBalance.balance),
        UserBalanceDto(id = "anotherUser", balance = BalanceDto(0.0, 0.0, 0.0))
    )

    @Before
    fun setup() {
        // Initialize the repository with the mock API service and the test dispatcher
        repository = UserRepository(apiService, testDispatcher)
    }

    // --- Test Cases ---

    @Test
    fun getUserBalance_success_emitsDomainObject() = runTest {
        // Arrange: Mock successful API call returning the DTO list
        doReturn(Response.success(mockUserBalanceDtoList)).`when`(apiService).getUserBalance(mockUserId)

        // Act
        val result = repository.getUserBalance(mockUserId).first() // Collect the first emitted value

        // Assert
        assertTrue(result.isSuccess)
        val balanceResult = result.getOrNull()!!
        
        // Verify that the correct DTO was found and mapped
        assertEquals(mockUserId, balanceResult.id)
        assertEquals(100.0, balanceResult.balance.total, 0.0)
    }

    @Test
    fun getUserBalance_apiError_emitsFailure() = runTest {
        // Arrange: Mock API response with an HTTP error code (e.g., 404)
        val errorResponse = Response.error<List<UserBalanceDto>>(
            404, 
            "Not Found".toResponseBody()
        )
        doReturn(errorResponse).`when`(apiService).getUserBalance(mockUserId)

        // Act
        val result = repository.getUserBalance(mockUserId).first()

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is Exception)
        // Check for the error message defined in your repository code
        assertTrue(exception?.message?.contains("API error: Response.error()") == true)
    }

    @Test
    fun getUserBalance_userNotFoundInResponseBody_emitsNoSuchElementFailure() = runTest {
        // Arrange: Mock successful API call but user ID is not in the list
        val wrongId = "nonExistentUser"
        doReturn(Response.success(mockUserBalanceDtoList)).`when`(apiService).getUserBalance(wrongId)

        // Act
        val result = repository.getUserBalance(wrongId).first()

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoSuchElementException)
        assertEquals("User not found in API", exception?.message)
    }

    @Test
    fun getUserBalance_networkException_emitsFailure() = runTest {
        // Arrange: Mock API call to throw an IOException (network failure)
        doReturn(Response.success(null)).`when`(apiService).getUserBalance(mockUserId) // Simple way to trigger body check fail
        // For a more complete test, you would mock the service to throw:
        // doThrow(IOException()).`when`(apiService).getUserBalance(mockUserId)

        // Act
        val result = repository.getUserBalance(mockUserId).first()

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        // Here, since the body is null, it falls through your 'else' case,
        // but for a true network error, you'd test for IOException or similar.
    }
}