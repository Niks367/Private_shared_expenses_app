package app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Profile
import com.example.myapplication.ui.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    // Get a reference to your database instance
    private val database by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // fetch a specific user's profile for this preview.
                // assume we want to preview the user with ID = 1.
                val profileState by produceState<Profile?>(initialValue = null, database) {
                    value = withContext(Dispatchers.IO) {
                        // Fetch the user on a background thread
                        database.profileDao().findById(1) // Fetching user with ID 1
                    }
                }

                // Get the profile from the state
                val userProfile = profileState

                // Check if the profile has been loaded
                if (userProfile != null) {
                    // Calculate the initials from the fetched profile
                    val initials = (userProfile.firstName.firstOrNull()?.toString() ?: "") +
                            (userProfile.lastName.firstOrNull()?.toString() ?: "")

                    // Call HomeScreen with the real data
                    HomeScreen(
                        userName = userProfile.firstName,
                        userId = userProfile.id.toString(),
                        userInitials = initials,
                        onProfileClick = {
                            // In this context, we can leave this empty or log a click
                        }
                    )
                } else {
                    // Show a loading indicator while the database is being read
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
