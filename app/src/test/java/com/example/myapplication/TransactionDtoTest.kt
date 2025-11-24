import com.example.myapplication.model.TransactionDto
import org.junit.Assert.*
import org.junit.Test

class TransactionDtoTest {

    @Test
    fun testToDomain_incomeType() {
        val dto = TransactionDto(icon = "upwork", title = "Work", date = "2025-11-24", amount = 100.0, type = "income")
        val transaction = dto.toDomain()
        assertTrue(transaction.isIncome)
        assertEquals(100.0, transaction.amount, 0.01)
    }

    @Test
    fun testToDomain_expenseType() {
        val dto = TransactionDto(icon = "transfer", title = "Shopping", date = "2025-11-24", amount = 50.0, type = "expense")
        val transaction = dto.toDomain()
        assertFalse(transaction.isIncome)
        assertEquals(50.0, transaction.amount, 0.01)
    }
}
