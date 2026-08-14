package online.hicode.android.hihanfu.ui.components

sealed interface SubmitState {

    object IDLE : SubmitState

    object Loading : SubmitState

    object Success : SubmitState

    object Fail : SubmitState

}