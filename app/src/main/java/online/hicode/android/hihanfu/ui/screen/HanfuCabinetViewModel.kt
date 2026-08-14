package online.hicode.android.hihanfu.ui.screen

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.hicode.android.hihanfu.data.dao.HanfuDao
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.data.repository.HanfuRepository
import online.hicode.android.hihanfu.data.vo.StatisticsVO
import online.hicode.android.hihanfu.ui.components.UiState
import java.io.File
import javax.inject.Inject

/**
 * 汉服柜ViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HanfuCabinetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hanfuRepository: HanfuRepository
)  : ViewModel() {
    val hanfuCabinetListUiState: StateFlow<UiState<List<HanfuCabinet>>> = hanfuRepository.listHanfuCabinet()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            if (list.isEmpty()) UiState.Success(emptyList()) else UiState.Success(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 界面销毁 5 秒后自动断开，省电
            initialValue = UiState.Loading // Compose 界面一打开时看到的初始状态
        )

    val hanfuCabinetStatisticsUiState: StateFlow<UiState<List<StatisticsVO>>> = hanfuRepository.countHanfuCabinet()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            if (list.isEmpty()) UiState.Success(emptyList()) else UiState.Success(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = UiState.Loading                  // 骨架屏/加载中的初始值
        )

    fun deleteHanfuCabinet(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // 删除图片文件
            val imgs:List<HanfuImage> = hanfuRepository.listHanfuImageByCabinet(id)
            imgs.forEach { it
                // 物理删除文件
                withContext(Dispatchers.IO) {
                    val file = File(it.path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
            // 删除关联的image
            hanfuRepository.deleteHanfuImageByCabinet(id)
            // 删除关联的hanfu
            hanfuRepository.deleteHanfuByCabinet(id)
            // 删除hanfu cabinet
            hanfuRepository.deleteHanfuCabinetById(id)
        }
    }

    fun updateHanfuCabinet(hanfuCabinet: HanfuCabinet) {
        viewModelScope.launch {
            hanfuRepository.updateHanfuCabinet(hanfuCabinet)
        }
    }

    fun insertHanfuCabinet(hanfuCabinet: HanfuCabinet) {
        viewModelScope.launch {
            hanfuRepository.insertHanfuCabinet(hanfuCabinet)
        }
    }

    // 当 ViewModel 即将被销毁时调用（用于清理资源）
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        // 取消协程或关闭连接等
    }
}