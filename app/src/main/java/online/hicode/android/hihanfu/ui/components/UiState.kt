package online.hicode.android.hihanfu.ui.components

sealed interface UiState<out T> {

    object Loading : UiState<Nothing>

    data class Success<out T>(val content: T) : UiState<T>

    data class Error(val message: String) : UiState<Nothing>

}