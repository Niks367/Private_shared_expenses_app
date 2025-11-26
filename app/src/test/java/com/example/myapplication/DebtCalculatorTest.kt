package com.example.myapplication.utils

import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.DebtInfo
import com.example.myapplication.model.GroupBalanceSummary
import com.example.myapplication.model.Settlement
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.abs

// --- Mock Data Classes (You MUST define these in your project) ---
// Assuming these are defined in com.example.myapplication.entities and com.example.myapplication.model
/*
data class Expense(val id: Long, val paidBy: Long, val amount: Double, val description: String)
data class Profile(val id: Long, val firstName: String, val lastName: String)
data class DebtInfo(...)
data class Settlement(...)
data class GroupBalanceSummary(...)
*/
// -----------------------------------------------------------------

class DebtCalculatorTest {

    // Mock data for profiles and expenses
    private val memberA = Profile(id = 1, firstName = "Alice", lastName = "A")
    private val memberB = Profile(id = 2, firstName = "Bob", lastName = "B")
    private val memberC = Profile(id = 3, firstName = "Charlie", lastName = "C")
    private val allMembers = listOf(memberA, memberB, memberC)

    @Test
    fun testCalculatePersonBalance_basicScenario() {
        // Alice pays 60.0, Bob pays 0, Charlie pays 0. Total: 60.0. Share: 20.0.
        val expenses = listOf(
            Expense(id = 101, paidBy = memberA.id, amount = 60.0, description = "Dinner")
        )
        val memberCount = 3

        // Alice's balance: Paid (60.0) - Share (20.0) = +40.0 (owed to her)
        val balanceA = DebtCalculator.calculatePersonBalance(memberA.id, expenses, memberCount)
        assertEquals(40.0, balanceA, 0.001)

        // Bob's balance: Paid (0.0) - Share (20.0) = -20.0 (owes)
        val balanceB = DebtCalculator.calculatePersonBalance(memberB.id, expenses, memberCount)
        assertEquals(-20.0, balanceB, 0.001)
    }
    
    // --- Test for calculateGroupBalance ---

    @Test
    fun testCalculateGroupBalance_fullCycle() {
        // Group: Alice, Bob, Charlie. Total Expenses: 90.0. Share per person: 30.0
        val expenses = listOf(
            // Alice pays 60.0
            Expense(id = 101, paidBy = memberA.id, amount = 45.0, description = "Hotel"),
            Expense(id = 102, paidBy = memberA.id, amount = 15.0, description = "Snacks"),
            // Bob pays 30.0
            Expense(id = 103, paidBy = memberB.id, amount = 30.0, description = "Drinks")
        )

        val summary = DebtCalculator.calculateGroupBalance(1, "Trip", expenses, allMembers)

        // Assert Total Expenses
        assertEquals(90.0, summary.totalExpenses, 0.001)

        // Assert Member Debts
        val debtA = summary.memberDebts.find { it.profileId == memberA.id }!! // Paid 60 - Share 30 = +30
        val debtB = summary.memberDebts.find { it.profileId == memberB.id }!! // Paid 30 - Share 30 = +0
        val debtC = summary.memberDebts.find { it.profileId == memberC.id }!! // Paid 0 - Share 30 = -30

        assertEquals(30.0, debtA.balance, 0.001)
        assertEquals(0.0, debtB.balance, 0.001)
        assertEquals(-30.0, debtC.balance, 0.001)

        // Assert Settlements (Optimal settlement: Charlie pays Alice 30)
        assertEquals(1, summary.settlements.size)
        val settlement = summary.settlements.first()

        assertEquals(memberC.id, settlement.fromProfileId) // Charlie pays
        assertEquals(memberA.id, settlement.toProfileId)   // Alice receives
        assertEquals(30.0, settlement.amount, 0.001)
    }

    @Test
    fun testCalculateGroupBalance_emptyInput() {
        val summary = DebtCalculator.calculateGroupBalance(1, "Empty", emptyList(), emptyList())
        assertEquals(0.0, summary.totalExpenses, 0.001)
        assertTrue(summary.memberDebts.isEmpty())
        assertTrue(summary.settlements.isEmpty())
    }

    // --- Test for getPersonSettlements ---
    
    @Test
    fun testGetPersonSettlements_personInvolved() {
        val summary = GroupBalanceSummary(
            groupId = 1, groupName = "Test", totalExpenses = 100.0, memberDebts = emptyList(),
            settlements = listOf(
                Settlement(fromProfileId = 1, toProfileId = 2, amount = 20.0, fromProfileName = "A", toProfileName = "B"), // A -> B (Involved)
                Settlement(fromProfileId = 3, toProfileId = 4, amount = 5.0, fromProfileName = "C", toProfileName = "D"),  // C -> D (Not Involved)
                Settlement(fromProfileId = 5, toProfileId = 1, amount = 10.0, fromProfileName = "E", toProfileName = "A") // E -> A (Involved)
            )
        )
        
        val settlements = DebtCalculator.getPersonSettlements(1, summary)
        
        assertEquals(2, settlements.size)
        assertTrue(settlements.any { it.fromProfileId == 1 && it.toProfileId == 2 })
        assertTrue(settlements.any { it.fromProfileId == 5 && it.toProfileId == 1 })
        
    }
}