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
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.data.entity.Jewelry
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.data.entity.JewelryImage
import online.hicode.android.hihanfu.data.entity.Outfit
import online.hicode.android.hihanfu.data.entity.OutfitHanfuRel
import online.hicode.android.hihanfu.data.entity.OutfitImage
import online.hicode.android.hihanfu.data.repository.HanfuRepository
import online.hicode.android.hihanfu.ui.components.UiState
import java.io.File
import javax.inject.Inject
import kotlin.collections.forEach

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class OutfitViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hanfuRepository: HanfuRepository
)  : ViewModel() {
    val uiState: StateFlow<UiState<List<Outfit>>> = hanfuRepository.listOutfit()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            if (list.isEmpty()) UiState.Success(emptyList()) else UiState.Success(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = UiState.Loading                  // 骨架屏/加载中的初始值
        )

    private val _outfitId = MutableStateFlow(0L)

    val outfitImages: StateFlow<List<OutfitImage>> = _outfitId
        .flatMapLatest { outfitId ->
            hanfuRepository.listOutfitImageByOutfit(outfitId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // 初始先给空列表防报错
        )


    val outfitHanfuList: StateFlow<List<Hanfu>> = _outfitId
        .flatMapLatest { outfitId ->
            hanfuRepository.listHanfuByOutfit(outfitId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // 初始先给空列表防报错
        )

    val outfitJewelryList: StateFlow<List<Jewelry>> = _outfitId
        .flatMapLatest { outfitId ->
            hanfuRepository.listJewelryByOutfit(outfitId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // 初始先给空列表防报错
        )

    val hanfuCabinetList: StateFlow<List<HanfuCabinet>> = hanfuRepository.listHanfuCabinet()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            list.ifEmpty { emptyList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = emptyList()               // 骨架屏/加载中的初始值
        )


    val hanfuList: StateFlow<List<Hanfu>> = hanfuRepository.listAllHanfu()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            list.ifEmpty { emptyList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = emptyList()               // 骨架屏/加载中的初始值
        )

    val jewelryCabinetList: StateFlow<List<JewelryCabinet>> = hanfuRepository.listJewelryCabinet()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            list.ifEmpty { emptyList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = emptyList()               // 骨架屏/加载中的初始值
        )

    val jewelryList: StateFlow<List<Jewelry>> = hanfuRepository.listAllJewelry()
        .map { list ->
            // 如果数据库是空的，可以返回空状态或 Loading，否则返回成功
            list.ifEmpty { emptyList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 当界面销毁 5 秒后自动停止收集，节约内存
            initialValue = emptyList()               // 骨架屏/加载中的初始值
        )

    fun loadOutfitImg(newOutfitId: Long) {
        _outfitId.value = newOutfitId
    }

    private val _jewelryId = MutableStateFlow(0L)

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

    private val _hanfuId = MutableStateFlow(0L)

    val hanfuImages: StateFlow<List<HanfuImage>> = _hanfuId
        .flatMapLatest { hanfuId ->
            hanfuRepository.listHanfuImageByHanfu(hanfuId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // 初始先给空列表防报错
        )

    fun loadHanfuImg(newHanfuId: Long) {
        _hanfuId.value = newHanfuId
    }

    // 删除汉服
    fun deleteOutfit(fileDir: File, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // 删除关联的image
            hanfuRepository.deleteOutfitImageByOutfit(id)
            // 删除汉服关联
            hanfuRepository.deleteOutfitByOutfit(id)
            // 删除hanfu
            hanfuRepository.deleteOutfitById(id)
            // 删除图片文件（当前删除逻辑在弹出时，已经加载了图片列表，不需要重新加载）
            outfitImages.value.forEach { it
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

    fun updateOutfit(outfit: Outfit) {
        viewModelScope.launch {
            hanfuRepository.updateOutfit(outfit)
        }
    }

    suspend fun insertOutfit(outfit: Outfit): Long {
        return hanfuRepository.insertOutfit(outfit)
    }

    fun insertOutfitImage(outfitImage: OutfitImage) {
        viewModelScope.launch {
            hanfuRepository.insertOutfitImage(outfitImage)
        }
    }

    fun deleteOutfitImage(outfitImage: OutfitImage) {
        viewModelScope.launch {
            // 删除图片文件
            // 物理删除文件
            withContext(Dispatchers.IO) {
                val file = File(outfitImage.path)
                if (file.exists()) {
                    file.delete()
                }
            }
            // 删除image
            hanfuRepository.deleteOutfitImageById(outfitImage.id)
        }
    }

    // 当 ViewModel 即将被销毁时调用（用于清理资源）
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        // 取消协程或关闭连接等
    }

    fun insertOutfitHanfuRel(outfitId: Long, hanfuList: List<Hanfu>, jewelryList: List<Jewelry>) {
        viewModelScope.launch(Dispatchers.IO) {
            // 先删除，再插入
            hanfuRepository.deleteOutfitByOutfit(outfitId)
            hanfuList.forEach { it
                hanfuRepository.insertOutfitHanfuRel(OutfitHanfuRel(
                    outfitId = outfitId,
                    type = "hanfu",
                    relId = it.id
                ))
            }
            jewelryList.forEach { it
                hanfuRepository.insertOutfitHanfuRel(OutfitHanfuRel(
                    outfitId = outfitId,
                    type = "jewelry",
                    relId = it.id
                ))
            }
        }
    }

}
