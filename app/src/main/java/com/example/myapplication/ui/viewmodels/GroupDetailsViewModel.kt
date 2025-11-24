package com.example.myapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.GroupBalanceSummary
import com.example.myapplication.utils.DebtCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupDetailsViewModel(private val database: AppDatabase, private val groupId: Long) : ViewModel() {

    data class GroupDetailsUiState(
        val group: Group? = null,
        val members: List<Profile> = emptyList(),
        val expenses: List<Expense> = emptyList(),
        val balanceSummary: GroupBalanceSummary? = null
    )

    val uiState: StateFlow<GroupDetailsUiState> = combine(
        database.groupDao().getGroupById(groupId),
        database.groupMemberDao().getGroupMembers(groupId),
        database.expenseDao().getExpensesForGroup(groupId)
    ) { group, members, expenses ->
        // Calculate balance summary
        val balanceSummary = if (group != null && members.isNotEmpty()) {
            DebtCalculator.calculateGroupBalance(
                groupId = group.id,
                groupName = group.name,
                expenses = expenses,
                members = members
            )
        } else {
            null
        }
        
        GroupDetailsUiState(group, members, expenses, balanceSummary)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GroupDetailsUiState()
    )

    class Factory(private val database: AppDatabase, private val groupId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GroupDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GroupDetailsViewModel(database, groupId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}