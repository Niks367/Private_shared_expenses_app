package com.example.myapplication

import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.interfaces.UserBalance
import com.example.myapplication.model.toDomain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

// Mock DTO class (replace with your actual DTO if needed)
data class MockUserDto(val id: String) {
    fun toDomain(): UserBalance = object : UserBalance {
        override val id: String = this@MockUserDto.id
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private val api = mockk<ApiService>()
    private val repository = UserRepository(api)

    @Test
    fun `getUserBalance returns success when user exists`() = runTest {
        val userId = "123"
        val mockResponse = listOf(MockUserDto(id = userId))
        coEvery { api.getUserBalance(userId) } returns Response.success(mockResponse)

        val result = repository.getUserBalance(userId).first()

        assertTrue(result.isSuccess)
        assertEquals(userId, result.getOrNull()?.id)
    }

    @Test
    fun `getUserBalance returns failure when user not found`() = runTest {
        val userId = "123"
        val mockResponse = listOf(MockUserDto(id = "456")) // different id
        coEvery { api.getUserBalance(userId) } returns Response.success(mockResponse)

        val result = repository.getUserBalance(userId).first()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun `getUserBalance returns failure on API error`() = runTest {
        val userId = "123"
        coEvery { api.getUserBalance(userId) } returns Response.error(500, okhttp3.ResponseBody.create(null, ""))

        val result = repository.getUserBalance(userId).first()

        assertTrue(result.isFailure)
        assertEquals("API error: Response.error()", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getUserBalance returns failure on exception`() = runTest {
        val userId = "123"
        coEvery { api.getUserBalance(userId) } throws RuntimeException("Network error")

        val result = repository.getUserBalance(userId).first()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
