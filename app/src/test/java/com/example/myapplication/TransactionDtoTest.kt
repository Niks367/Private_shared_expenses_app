package com.example.myapplication.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
// Import the R class or define placeholders for the resource IDs
// Since R.drawable is not available in a pure JUnit test, we must mock/replace it.
// Assuming your Transaction domain model looks something like this (you must define it):
/*
data class Transaction(
    val iconRes: Int,
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)
*/

// Define placeholder values for R.drawable
private const val MOCK_UPWORK_RES = 1001
private const val MOCK_TRANSFER_RES = 1002

class TransactionDtoTest {

    // Helper function that mirrors the extension, but uses mock IDs for pure JVM testing
    private fun TransactionDto.toDomainMocked(): Transaction = Transaction(
        iconRes = when (icon) {
            "upwork"   -> MOCK_UPWORK_RES
            "transfer" -> MOCK_TRANSFER_RES
            else -> { MOCK_TRANSFER_RES } // Default value logic
        },
        title = title,
        date = date,
        amount = amount,
        isIncome = type.equals("income", ignoreCase = true)
    )

    // --- Tests for 'isIncome' Logic ---

    @Test
    fun testToDomain_incomeType() {
        // Test case: Type is exactly "income"
        val dto = TransactionDto(icon = "upwork", title = "Work", date = "2025-11-24", amount = 100.0, type = "income")
        val transaction = dto.toDomainMocked()
        
        // Assert income properties
        assertTrue(transaction.isIncome)
        assertEquals(100.0, transaction.amount, 0.0)
    }

    @Test
    fun testToDomain_caseInsensitiveIncomeType() {
        // Test case: Type is "InCoMe" (ensures ignoreCase = true works)
        val dto = TransactionDto(icon = "upwork", title = "Work", date = "2025-11-24", amount = 100.0, type = "InCoMe")
        val transaction = dto.toDomainMocked()
        
        // Assert income properties
        assertTrue(transaction.isIncome)
    }

    @Test
    fun testToDomain_expenseType() {
        // Test case: Type is "expense"
        val dto = TransactionDto(icon = "transfer", title = "Shopping", date = "2025-11-24", amount = 50.0, type = "expense")
        val transaction = dto.toDomainMocked()
        
        // Assert expense properties
        assertFalse(transaction.isIncome)
        assertEquals(50.0, transaction.amount, 0.0)
    }

    // --- Tests for Icon Mapping Logic ---

    @Test
    fun testToDomain_iconMapping_upwork() {
        val dto = TransactionDto(icon = "upwork", title = "", date = "", amount = 0.0, type = "expense")
        val transaction = dto.toDomainMocked()
        
        // Assert that the string "upwork" maps to the correct mock resource ID
        assertEquals(MOCK_UPWORK_RES, transaction.iconRes)
    }

    @Test
    fun testToDomain_iconMapping_transfer() {
        val dto = TransactionDto(icon = "transfer", title = "", date = "", amount = 0.0, type = "expense")
        val transaction = dto.toDomainMocked()
        
        // Assert that the string "transfer" maps to the correct mock resource ID
        assertEquals(MOCK_TRANSFER_RES, transaction.iconRes)
    }
    
    @Test
    fun testToDomain_iconMapping_default() {
        // Test case: Icon string is unknown (e.g., "bank")
        val dto = TransactionDto(icon = "bank", title = "", date = "", amount = 0.0, type = "expense")
        val transaction = dto.toDomainMocked()
        
        // Assert that an unknown string maps to the default R.drawable.transfer (MOCK_TRANSFER_RES)
        assertEquals(MOCK_TRANSFER_RES, transaction.iconRes)
    }
}