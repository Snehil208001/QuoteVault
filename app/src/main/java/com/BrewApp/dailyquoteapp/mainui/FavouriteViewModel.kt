import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.model.FavouriteItem
import com.BrewApp.dailyquoteapp.data.model.FavouriteUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<FavouriteUiState>(FavouriteUiState.Loading)
    val uiState: StateFlow<FavouriteUiState> = _uiState.asStateFlow()

    init {
        loadFavourites()
    }

    // Simulate fetching from a database/API
    fun loadFavourites() {
        viewModelScope.launch {
            _uiState.value = FavouriteUiState.Loading
            try {
                // TODO: Replace with actual Repository call, e.g., repository.getAllFavourites()
                delay(1000) // Simulating network/database delay

                // simulating fetching data
                val fetchedItems = listOf(
                    FavouriteItem(
                        "1",
                        "Home",
                        "123, Green Street, Bihar",
                        System.currentTimeMillis()
                    ),
                    FavouriteItem("2", "Office", "Invyu Tech Park, Sector 5", System.currentTimeMillis()),
                    FavouriteItem("3", "Gym", "Gold's Gym, Main Road", System.currentTimeMillis())
                )

                if (fetchedItems.isEmpty()) {
                    _uiState.value = FavouriteUiState.Empty
                } else {
                    _uiState.value = FavouriteUiState.Success(fetchedItems)
                }
            } catch (e: Exception) {
                _uiState.value = FavouriteUiState.Error("Failed to load favourites")
            }
        }
    }

    fun removeFavourite(item: FavouriteItem) {
        viewModelScope.launch {
            // TODO: Call Repository to delete from DB: repository.delete(item.id)

            // Optimistically update UI
            val currentState = _uiState.value
            if (currentState is FavouriteUiState.Success) {
                val updatedList = currentState.items.filter { it.id != item.id }
                if (updatedList.isEmpty()) {
                    _uiState.value = FavouriteUiState.Empty
                } else {
                    _uiState.value = FavouriteUiState.Success(updatedList)
                }
            }
        }
    }
}