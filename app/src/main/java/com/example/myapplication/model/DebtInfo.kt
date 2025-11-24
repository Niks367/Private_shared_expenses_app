package com.example.myapplication.model

/**
 * Represents debt information for a person in a group
 * @param profileId The ID of the person
 * @param profileName The name of the person
 * @param balance Positive if they are owed money, negative if they owe money
 * @param totalPaid Total amount they have paid for group expenses
 * @param totalShare Total amount they should pay (their share)
 */
data class DebtInfo(
    val profileId: Long,
    val profileName: String,
    val balance: Double, // positive = owed to them, negative = they owe
    val totalPaid: Double,
    val totalShare: Double
)

/**
 * Represents a settlement transaction between two people
 * @param fromProfileId The person who needs to pay
 * @param fromProfileName Name of the person who needs to pay
 * @param toProfileId The person who will receive payment
 * @param toProfileName Name of the person who will receive payment
 * @param amount The amount to be paid
 */
data class Settlement(
    val fromProfileId: Long,
    val fromProfileName: String,
    val toProfileId: Long,
    val toProfileName: String,
    val amount: Double
)

/**
 * Summary of group balance calculations
 * @param groupId The group ID
 * @param groupName The group name
 * @param totalExpenses Total expenses in the group
 * @param memberDebts List of debt information for each member
 * @param settlements Optimized list of settlements to balance all debts
 */
data class GroupBalanceSummary(
    val groupId: Long,
    val groupName: String,
    val totalExpenses: Double,
    val memberDebts: List<DebtInfo>,
    val settlements: List<Settlement>
)

