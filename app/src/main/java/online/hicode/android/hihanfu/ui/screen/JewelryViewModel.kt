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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.Jewelry
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.data.entity.JewelryImage
import online.hicode.android.hihanfu.data.repository.HanfuRepository
import online.hicode.android.hihanfu.ui.components.UiState
import java.io.File
import javax.inject.Inject
import kotlin.collections.forEach

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class JewelryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hanfuRepository: HanfuRepository
)  : ViewModel() {

    // 1. 从 SavedStateHandle 中读取字符串路由传递过来的 "cabinetId"
    // 如果读取不到，抛出异常或设定默认值
    // 1. 将导航传过来的 "itemId" 转化为一个状态 Flow。
    // 如果读取不到参数，直接抛出异常阻止初始化。
    private val cabinetIdFlow: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = "cabinetId",
        initialValue = savedStateHandle["cabinetId"] ?: 0
    )

    val uiState: StateFlow<UiState<List<Jewelry>>> = cabinetIdFlow
        .flatMapLatest { id ->
            // 自动传入 id，并调用 repository 获取该 id 对应的本地/网络数据流
            hanfuRepository.listJewelry(id)
        }
        .map { entityList ->
            // 将原始数据列表包装为 UI 层的状态泛型
            if (entityList.isEmpty()) UiState.Success(emptyList()) else UiState.Success(entityList)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = UiState.Loading                  // 骨架屏/加载中的初始值
        )

    val jewelryCabinet: StateFlow<JewelryCabinet?> = cabinetIdFlow
        .flatMapLatest { id ->
            // 自动传入 id，并调用 repository 获取该 id 对应的本地/网络数据流
            hanfuRepository.getJewelryCabinetById(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = null                  // 骨架屏/加载中的初始值
        )

    private val _jewelryId = MutableStateFlow(0L)
    val hanfuId: StateFlow<Long> = _jewelryId.asStateFlow()

    val jewelryImages: StateFlow<List<JewelryImage>> = _jewelryId
        .flatMapLatest { jewelryId ->
            hanfuRepository.listJewelryImageByJewelry(jewelryId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // 初始先给空列表防报错
        )

    fun loadJewelryImg(newJewelryId: Long) {
        _jewelryId.value = newJewelryId
    }

    // 删除汉服
    fun deleteJewelry(fileDir: File, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // 删除关联的image
            hanfuRepository.deleteJewelryImageByJewelry(id)
            // 删除hanfu
            hanfuRepository.deleteJewelryById(id)
            // 删除图片文件（当前删除逻辑在弹出时，已经加载了图片列表，不需要重新加载）
            jewelryImages.value.forEach { it
                // 物理删除文件
                withContext(Dispatchers.IO) {
                    val file = File(fileDir, it.path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
        }
    }

    fun updateJewelry(jewelry: Jewelry) {
        viewModelScope.launch {
            hanfuRepository.updateJewelry(jewelry)
        }
    }

    suspend fun insertJewelry(jewelry: Jewelry): Long {
        return hanfuRepository.insertJewelry(jewelry)
    }

    fun insertJewelryImage(jewelryImage: JewelryImage) {
        viewModelScope.launch {
            hanfuRepository.insertJewelryImage(jewelryImage)
        }
    }

    fun deleteJewelryImage(jewelryImage: JewelryImage) {
        viewModelScope.launch {
            // 删除图片文件
            // 物理删除文件
            withContext(Dispatchers.IO) {
                val file = File(jewelryImage.path)
                if (file.exists()) {
                    file.delete()
                }
            }
            // 删除image
            hanfuRepository.deleteJewelryImageById(jewelryImage.id)
        }
    }

    // 当 ViewModel 即将被销毁时调用（用于清理资源）
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        // 取消协程或关闭连接等
    }

}
