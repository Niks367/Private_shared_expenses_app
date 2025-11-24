import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Profile
import com.example.myapplication.utils.DebtCalculator
import org.junit.Assert.*
import org.junit.Test

class DebtCalculatorTest {

    @Test
    fun testCalculatePersonBalance_positiveBalance() {
        val expenses = listOf(
            Expense(groupId = 1, paidBy = 1, description = "Dinner", amount = 50.0, date = "2025-11-24"),
            Expense(groupId = 1, paidBy = 2, description = "Drinks", amount = 30.0, date = "2025-11-24")
        )
        val balance = DebtCalculator.calculatePersonBalance(profileId = 1, expenses = expenses, memberCount = 2)
        assertEquals(25.0, balance, 0.01)
    }

    @Test
    fun testCalculatePersonBalance_negativeBalance() {
        val expenses = listOf(
            Expense(groupId = 1, paidBy = 1, description = "Dinner", amount = 50.0, date = "2025-11-24"),
            Expense(groupId = 1, paidBy = 2, description = "Drinks", amount = 30.0, date = "2025-11-24")
        )
        val balance = DebtCalculator.calculatePersonBalance(profileId = 2, expenses = expenses, memberCount = 2)
        assertEquals(-25.0, balance, 0.01)
    }
}
