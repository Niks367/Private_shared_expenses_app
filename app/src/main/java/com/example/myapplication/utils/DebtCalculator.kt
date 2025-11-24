package com.example.myapplication.utils

import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.DebtInfo
import com.example.myapplication.model.GroupBalanceSummary
import com.example.myapplication.model.Settlement
import kotlin.math.abs
import kotlin.math.min

/**
 * Utility class for calculating debts and settlements in expense groups
 */
object DebtCalculator {
    
    /**
     * Calculate the complete balance summary for a group
     * @param groupId The group ID
     * @param groupName The group name
     * @param expenses List of all expenses in the group
     * @param members List of all members in the group
     * @return GroupBalanceSummary with all debt information and optimal settlements
     */
    fun calculateGroupBalance(
        groupId: Long,
        groupName: String,
        expenses: List<Expense>,
        members: List<Profile>
    ): GroupBalanceSummary {
        // Calculate total expenses
        val totalExpenses = expenses.sumOf { it.amount }
        
        // If no expenses or no members, return empty summary
        if (totalExpenses == 0.0 || members.isEmpty()) {
            return GroupBalanceSummary(
                groupId = groupId,
                groupName = groupName,
                totalExpenses = 0.0,
                memberDebts = emptyList(),
                settlements = emptyList()
            )
        }
        
        // Calculate equal share per person
        val sharePerPerson = totalExpenses / members.size
        
        // Calculate how much each person paid
        val paidByPerson = mutableMapOf<Long, Double>()
        members.forEach { member ->
            paidByPerson[member.id] = 0.0
        }
        
        expenses.forEach { expense ->
            paidByPerson[expense.paidBy] = 
                (paidByPerson[expense.paidBy] ?: 0.0) + expense.amount
        }
        
        // Calculate debt information for each member
        val memberDebts = members.map { member ->
            val totalPaid = paidByPerson[member.id] ?: 0.0
            val balance = totalPaid - sharePerPerson
            
            DebtInfo(
                profileId = member.id,
                profileName = "${member.firstName} ${member.lastName}",
                balance = balance,
                totalPaid = totalPaid,
                totalShare = sharePerPerson
            )
        }.sortedByDescending { it.balance }
        
        // Calculate optimal settlements
        val settlements = calculateOptimalSettlements(memberDebts)
        
        return GroupBalanceSummary(
            groupId = groupId,
            groupName = groupName,
            totalExpenses = totalExpenses,
            memberDebts = memberDebts,
            settlements = settlements
        )
    }
    
    /**
     * Calculate optimal settlements to minimize number of transactions
     * Uses a greedy algorithm to match largest creditor with largest debtor
     * @param debts List of debt information for all members
     * @return List of settlements that balance all debts
     */
    private fun calculateOptimalSettlements(debts: List<DebtInfo>): List<Settlement> {
        val settlements = mutableListOf<Settlement>()
        
        // Create mutable lists of creditors (positive balance) and debtors (negative balance)
        val creditors = debts.filter { it.balance > 0.01 } // Small threshold to avoid floating point issues
            .map { it.profileId to Pair(it.profileName, it.balance) }
            .toMutableList()
            .sortedByDescending { it.second.second }
            .toMutableList()
        
        val debtors = debts.filter { it.balance < -0.01 }
            .map { it.profileId to Pair(it.profileName, abs(it.balance)) }
            .toMutableList()
            .sortedByDescending { it.second.second }
            .toMutableList()
        
        // Greedy algorithm: match largest creditor with largest debtor
        var creditorIndex = 0
        var debtorIndex = 0
        
        while (creditorIndex < creditors.size && debtorIndex < debtors.size) {
            val (creditorId, creditorData) = creditors[creditorIndex]
            val (creditorName, creditorAmount) = creditorData
            
            val (debtorId, debtorData) = debtors[debtorIndex]
            val (debtorName, debtorAmount) = debtorData
            
            // Amount to settle is minimum of what creditor is owed and what debtor owes
            val settlementAmount = min(creditorAmount, debtorAmount)
            
            // Create settlement
            settlements.add(
                Settlement(
                    fromProfileId = debtorId,
                    fromProfileName = debtorName,
                    toProfileId = creditorId,
                    toProfileName = creditorName,
                    amount = settlementAmount
                )
            )
            
            // Update balances
            val newCreditorAmount = creditorAmount - settlementAmount
            val newDebtorAmount = debtorAmount - settlementAmount
            
            // Update or remove creditor
            if (newCreditorAmount > 0.01) {
                creditors[creditorIndex] = creditorId to Pair(creditorName, newCreditorAmount)
            } else {
                creditorIndex++
            }
            
            // Update or remove debtor
            if (newDebtorAmount > 0.01) {
                debtors[debtorIndex] = debtorId to Pair(debtorName, newDebtorAmount)
            } else {
                debtorIndex++
            }
        }
        
        return settlements
    }
    
    /**
     * Calculate how much a specific person owes or is owed
     * @param profileId The person's profile ID
     * @param expenses List of all expenses in the group
     * @param memberCount Number of members in the group
     * @return The balance (positive if owed, negative if owing)
     */
    fun calculatePersonBalance(
        profileId: Long,
        expenses: List<Expense>,
        memberCount: Int
    ): Double {
        if (memberCount == 0) return 0.0
        
        val totalExpenses = expenses.sumOf { it.amount }
        val sharePerPerson = totalExpenses / memberCount
        val totalPaid = expenses.filter { it.paidBy == profileId }.sumOf { it.amount }
        
        return totalPaid - sharePerPerson
    }
    
    /**
     * Get settlements involving a specific person
     * @param profileId The person's profile ID
     * @param summary The group balance summary
     * @return List of settlements where this person is involved
     */
    fun getPersonSettlements(
        profileId: Long,
        summary: GroupBalanceSummary
    ): List<Settlement> {
        return summary.settlements.filter {
            it.fromProfileId == profileId || it.toProfileId == profileId
        }
    }
}

