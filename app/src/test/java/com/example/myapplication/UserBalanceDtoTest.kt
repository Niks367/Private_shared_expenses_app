import com.example.myapplication.model.BalanceDto
import com.example.myapplication.model.TransactionDto
import com.example.myapplication.model.UserBalanceDto
import org.junit.Assert.*
import org.junit.Test

class UserBalanceDtoTest {

    @Test
    fun testToDomain_conversion() {
        val transactions = listOf(
            TransactionDto(icon = "upwork", title = "Job", date = "2025-11-24", amount = 200.0, type = "income")
        )
        val dto = UserBalanceDto(
            id = "user1",
            balance = BalanceDto(total = 200.0, income = 200.0, expense = 0.0),
            transactions = transactions
        )
        val domain = dto.toDomain()
        assertEquals("user1", domain.id)
        assertEquals(200.0, domain.balance.total, 0.01)
        assertEquals(1, domain.transactions.size)
        assertTrue(domain.transactions[0].isIncome)
    }
}
