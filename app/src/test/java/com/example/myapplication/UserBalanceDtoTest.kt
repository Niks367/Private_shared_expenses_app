package com.example.myapplication.model

import com.example.myapplication.interfaces.UserBalance
import org.junit.Assert.*
import org.junit.Test

// --- Mock Domain and Nested Data Classes (You must define these) ---

// Assuming your domain interface/class for the final model
// Using a data class for easy assertion in the test
data class UserBalanceDomain(
    override val id: String,
    override val balance: BalanceDto,
    override val transactions: List<Transaction>
) : UserBalance // Assuming UserBalance is an interface or base class

// Define the BalanceDto used in the DTO (assuming it's a data class)
data class BalanceDto(
    val total: Double,
    val income: Double,
    val expense: Double
)

// Define the TransactionDto used in the DTO
// We need a simple toDomain() for the nested list mapping
data class TransactionDto(
    val title: String,
    val amount: Double,
    val type: String
) {
    // Define a simple placeholder toDomain for the test to succeed
    fun toDomain(): Transaction = Transaction(
        title = this.title,
        amount = this.amount,
        isIncome = this.type.equals("income", ignoreCase = true)
    )
}

// Define the Transaction domain model used in the final UserBalance
data class Transaction(
    val title: String,
    val amount: Double,
    val isIncome: Boolean
)
// -----------------------------------------------------------------


class UserBalanceDtoTest {

    @Test
    fun testToDomain_withTransactions() {
        // Arrange: Create a DTO with sample nested data
        val balanceDto = BalanceDto(total = 50.0, income = 150.0, expense = 100.0)
        val transactionDto1 = TransactionDto(title = "Salary", amount = 1000.0, type = "income")
        val transactionDto2 = TransactionDto(title = "Rent", amount = 500.0, type = "expense")
        
        val userBalanceDto = UserBalanceDto(
            id = "user123",
            balance = balanceDto,
            transactions = listOf(transactionDto1, transactionDto2)
        )

        // Act
        val userBalanceDomain = userBalanceDto.toDomain()

        // Assert: Check top-level properties and nested object
        assertEquals("user123", userBalanceDomain.id)
        assertEquals(balanceDto, userBalanceDomain.balance) // BalanceDto is copied by value
        assertEquals(50.0, userBalanceDomain.balance.total, 0.0)

        // Assert: Check transactions list size and mapping
        assertNotNull(userBalanceDomain.transactions)
        assertEquals(2, userBalanceDomain.transactions.size)
        
        // Assert: Check that nested mapping (toDomain) was called correctly
        val domainTransaction1 = userBalanceDomain.transactions[0]
        assertEquals("Salary", domainTransaction1.title)
        assertTrue(domainTransaction1.isIncome)

        val domainTransaction2 = userBalanceDomain.transactions[1]
        assertEquals("Rent", domainTransaction2.title)
        assertFalse(domainTransaction2.isIncome)
    }

    @Test
    fun testToDomain_withNullTransactions() {
        // Arrange: Create a DTO with transactions set to null
        val balanceDto = BalanceDto(total = 0.0, income = 0.0, expense = 0.0)
        val userBalanceDto = UserBalanceDto(
            id = "user456",
            balance = balanceDto,
            transactions = null // This is the case we are testing
        )

        // Act
        val userBalanceDomain = userBalanceDto.toDomain()

        // Assert
        assertEquals("user456", userBalanceDomain.id)
        
        // Assert: Check that null transactions results in an empty list
        assertNotNull(userBalanceDomain.transactions)
        assertTrue(userBalanceDomain.transactions.isEmpty())
    }
}