package online.hicode.android.hihanfu.ui.screen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.hicode.android.hihanfu.R
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.neumorphism.LightSource
import online.hicode.android.hihanfu.neumorphism.neu
import online.hicode.android.hihanfu.neumorphism.shape.Flat
import online.hicode.android.hihanfu.neumorphism.shape.Pressed
import online.hicode.android.hihanfu.neumorphism.shape.RoundedCorner
import online.hicode.android.hihanfu.ui.components.DeleteConfirmDialog
import online.hicode.android.hihanfu.ui.components.FloatingPriceTag
import online.hicode.android.hihanfu.ui.components.FullScreenImageDialog
import online.hicode.android.hihanfu.ui.components.SubmitState
import online.hicode.android.hihanfu.ui.components.UiState
import online.hicode.android.hihanfu.ui.navigation.Screen
import online.hicode.android.hihanfu.utils.saveUriToAppDataDir
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HanfuScreen(
    cabinetId: Long,
    navController: NavHostController,
    hanfuViewModel: HanfuViewModel
) {
    // 列表数据
    val uiState by hanfuViewModel.uiState.collectAsStateWithLifecycle()
    val hanfuCabinet by hanfuViewModel.hanfuCabinet.collectAsState()
    val hanfuImages by hanfuViewModel.hanfuImages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    // 新增编辑汉服柜抽屉
    val showSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        // locked
        skipPartiallyExpanded = true
    )
    var submitState: SubmitState by remember { mutableStateOf(SubmitState.IDLE) }
    var editableHanfu by remember {
        mutableStateOf(Hanfu(
            cabinetId = cabinetId,
            name = "",
            price = "",
            date = "",
            cover = ""
        ))
    }

    // 查看汉服抽屉
    val showHanfuDetailSheet = remember { mutableStateOf(false) }
    val hanfuDetailSheetState = rememberModalBottomSheetState(
        // locked
        skipPartiallyExpanded = true
    )

    val showDeleteDialog = remember { mutableStateOf(false) }

    // ✨ 核心逻辑：利用 LaunchedEffect 与 showSheet 监听抽屉展开状态
    LaunchedEffect(showSheet) {
        snapshotFlow { showSheet.value }
            .collect { value ->
                if (value) {
                    hanfuViewModel.loadHanfuImg(editableHanfu.id)
                }
            }
    }

    //
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            // 自定义各个页面的topBar
            TopAppBar(
                title = {
                    Text(
                        text = hanfuCabinet?.name ?: stringResource(Screen.HanfuNav.label)
                    )
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,     // 🔥 整个顶栏的背景颜色
                    titleContentColor = MaterialTheme.colorScheme.primary,        // 🔥 标题文字的颜色
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,// 🔥 左侧返回键等图标的颜色
                    actionIconContentColor = MaterialTheme.colorScheme.primary     // 🔥 右侧菜单等图标的颜色
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) { // 🔥 点击时触发返回
                        Icon(
                            painter = painterResource(R.drawable.arrow_left_line),
                            contentDescription = stringResource(R.string.button_back)
                        )
                    }
                },
                actions = {
                    // 新增汉服按钮
                    IconButton(onClick = {
                        hanfuViewModel.loadHanfuImg(0L)
                        editableHanfu = Hanfu(
                            cabinetId = cabinetId,
                            name = "",
                            price = "",
                            date = "",
                            cover = ""
                        )
                        showSheet.value = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_fill),
                            contentDescription = stringResource(R.string.description_add)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                // 1. 加载中状态（骨架屏或进度条）

            }
            is UiState.Success -> {
                // 2. 成功状态：此时 state.data 会自动推导为 List<ProductEntity>
                val hanfuList = state.content
                if (hanfuList.isEmpty()) {
                    // 💡 空状态：直接在 item 作用域内放占位组件
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        HanfuEmptyPlaceholder()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            // 💡 核心：必须使用 Scaffold 吐出来的 paddingValues，瀑布流滑动才不会被遮挡
                            .padding(innerPadding)
                    ) {
                        // 瀑布流组件
                        LazyVerticalStaggeredGrid(
                            // 💡 核心配置 A：定义列数。Fixed(2) 代表固定为 2 列（最经典的电商/社区排版）
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            // 💡 核心配置 B：直接复用从上一节学到的 Scaffold 传出来的安全边距
                            contentPadding = PaddingValues(12.dp),
                            // 💡 核心配置 C：设置多列之间的横向间距和纵向错落间距
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalItemSpacing = 12.dp
                        ) {
                            // 💡 不为空：使用 items 自动进行高效循环渲染
                            itemsIndexed(hanfuList) { index, itemData ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    HanfuCard(
                                        context = context,
                                        data = itemData,
                                        onDetail = { hanfu ->
                                            // 触发更新图片列表
                                            hanfuViewModel.loadHanfuImg(hanfu.id)
                                            editableHanfu = hanfu
                                            showHanfuDetailSheet.value = true
                                        },
                                    )
                                }
                            }
                        }
                    }

                }
            }
            is UiState.Error -> {
                // 3. 失败状态
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

    }


    if (showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            // 这样当键盘弹起时，整个抽屉会像刚体一样，被整体丝滑地推上去，而不是把内容压扁
            contentWindowInsets = { BottomSheetDefaults.windowInsets }
        ) {
            // Sheet 表单内容
            HanfuForm(
                submitState,
                editableHanfu,
                hanfuImages,
                onSave = { hanfu, coverIndex, newImages, deleteImages ->
                    submitState = SubmitState.Loading
                    // 💡 交互核心 2：开启异步协程，在后台线程处理文件 IO
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            var hanfuId : Long = hanfu.id
                            if (hanfu.id > 0) {
                                hanfuViewModel.updateHanfu(hanfu)
                            } else {
                                hanfuId = hanfuViewModel.insertHanfu(hanfu)
                            }

                            newImages.forEachIndexed  { index, it ->
                                val path: String =  saveUriToAppDataDir(context, "images", it)
                                // 保存图片文件到数据库
                                hanfuViewModel.insertHanfuImage(HanfuImage(
                                    hanfuId = hanfuId,
                                    path = path
                                ))
                                if (hanfu.cover.isBlank() && coverIndex == index) {
                                    // 更新封面
                                    hanfuViewModel.updateHanfu(Hanfu(
                                        id = hanfuId,
                                        cabinetId = hanfu.cabinetId,
                                        name = hanfu.name,
                                        price = hanfu.price,
                                        date = hanfu.date,
                                        material = hanfu.material,
                                        size = hanfu.size,
                                        cover = path
                                    ))
                                }
                            }

                            deleteImages.forEach { it
                                // 删除图片
                                hanfuViewModel.deleteHanfuImage(it)
                            }

                            // 处理成功：切回主线程更新 UI
                            withContext(Dispatchers.Main) {
                                submitState = SubmitState.IDLE
                                showSheet.value = false
                                editableHanfu = hanfu
                            }
                        } catch (e: Exception) {
                            // 处理失败：切回主线程提示错误
                            withContext(Dispatchers.Main) {
                                submitState = SubmitState.IDLE
                            }
                        }
                    }
                }
            )
        }
    }

    if (showHanfuDetailSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showHanfuDetailSheet.value = false },
            sheetState = hanfuDetailSheetState,
            containerColor = MaterialTheme.colorScheme.background,
            // 这样当键盘弹起时，整个抽屉会像刚体一样，被整体丝滑地推上去，而不是把内容压扁
            contentWindowInsets = { BottomSheetDefaults.windowInsets }
        ) {
            // Sheet 表单内容
            HanfuDetail(
                context = context,
                editableHanfu,
                hanfuImages,
                onEdit = { hanfu ->
                    hanfuViewModel.loadHanfuImg(hanfu.id)
                    editableHanfu = hanfu
                    showSheet.value = true
                },
                onDelete = { hanfu ->
                    editableHanfu = hanfu
                    showDeleteDialog.value = true
                }
            )
        }
    }

    DeleteConfirmDialog(
        title = stringResource(R.string.delete_confirm_title),
        message = stringResource(R.string.delete_confirm_message_hanfu_cabinet),
        showDialog = showDeleteDialog.value,
        onDismiss = {
            showDeleteDialog.value = false
        },
        onConfirm = {
            coroutineScope.launch {
                hanfuViewModel.deleteHanfu(context.filesDir, editableHanfu.id)
                showDeleteDialog.value = false
                // 隐藏详情弹窗
                showHanfuDetailSheet.value = false
            }
        }
    )

}


@Composable
fun HanfuEmptyPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // 水平方向居中（交叉轴）
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            painter = painterResource(id = Screen.HanfuCabinetNav.icon),
            contentDescription = stringResource(Screen.HanfuNav.label),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(stringResource(R.string.no_date))
    }
}

/**
 * 汉服柜卡片
 */
@Composable
fun HanfuCard(
    context: Context,
    data: Hanfu,
    onDetail: (hanfu: Hanfu) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .neu(
                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                darkShadowColor = MaterialTheme.colorScheme.surface,
                shadowElevation = 6.dp,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(12.dp)),
            )
            .then(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onDetail(data)
                    }
            )
        ,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = File(context.filesDir, data.cover), // Coil 可以直接接收 File 对象作为数据源
                contentDescription = "加载自私有相对路径的图片",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(),
                contentScale = ContentScale.FillWidth,
                // 建议加上错误占位图，防范文件未下载完、被删除或路径拼错的情况
//                        error = painterResource(id = R.drawable.image_error_placeholder)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.dp, end = 2.dp, top = 2.dp, bottom = 2.dp)
                ,
                // 🎯 核心点：横向的所有子组件自动实现 Space Between 效果
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // 顺便让它们在纵向上居中对齐
            ) {
                // 展示汉服名称
                Text(
                    text = data.name,
                    modifier = Modifier.weight(1f),
                    // 💡 核心配置 2：限制必须是单行展示
                    maxLines = 1,
                    // 💡 核心配置 3：超出长度时自动优雅地切为“...”省略号
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
                if (data.price.isNotBlank()) {
                    Spacer(modifier = Modifier.width(2.dp))
                    FloatingPriceTag("￥" + data.price)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HanfuDetail(
    context: Context,
    hanfu: Hanfu,
    hanfuImages: List<HanfuImage>,
    onEdit: (hanfu: Hanfu) -> Unit,
    onDelete: (hanfu: Hanfu) -> Unit
) {
    var activeImageUrl by remember { mutableStateOf<File?>(null) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
            ,
            // 🎯 核心点：横向的所有子组件自动实现 Space Between 效果
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // 顺便让它们在纵向上居中对齐
        ) {
            // 展示汉服数量
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hanfu.price.isNotBlank()) {
                    FloatingPriceTag("￥" + hanfu.price)
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = hanfu.name,
                    modifier = Modifier.weight(1f),
                    // 💡 核心配置 2：限制必须是单行展示
                    maxLines = 1,
                    // 💡 核心配置 3：超出长度时自动优雅地切为“...”省略号
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // 展示编辑、删除按钮
            Row() {
                IconButton(
                    modifier = Modifier

                    ,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = Color.Gray
                    ),
                    onClick = {
                        onEdit(hanfu)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.pencil_ai_fill),
                        contentDescription = stringResource(R.string.description_edit),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    modifier = Modifier

                    ,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = Color.Gray
                    ),
                    onClick = {
                        onDelete(hanfu)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_bin_5_fill),
                        contentDescription = stringResource(R.string.description_delete),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 6. 整体水平布局容器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 【左侧区域】：图片列表（占据剩余的所有空间）
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (hanfuImages.isEmpty()) {
                    // 空的，未上传图片和选择图片
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // 水平方向居中（交叉轴）
                    ) {

                        Icon(
                            painter = painterResource(id = Screen.HanfuCabinetNav.icon),
                            contentDescription = stringResource(Screen.HanfuNav.label),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(stringResource(R.string.no_img))
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth().height(240.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp), // 图片之间的间距
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        itemsIndexed(hanfuImages) { index, img ->
                            Box(
                                modifier = Modifier
                                    .height(240.dp).clip(RoundedCornerShape(12.dp)) // 💡 1. 规定整个图片容器的大小
                            ) {
                                AsyncImage(
                                    model = File(context.filesDir, img.path), // Coil 可以直接接收 File 对象作为数据源
                                    contentDescription = "加载自私有相对路径的图片",
                                    modifier = Modifier
                                        .height(240.dp)
                                        .wrapContentWidth()
                                        .clickable {
                                            // 全屏查看
                                            activeImageUrl = File(context.filesDir, img.path)
                                        },
                                    contentScale = ContentScale.FillHeight,
                                    // 建议加上错误占位图，防范文件未下载完、被删除或路径拼错的情况
//                        error = painterResource(id = R.drawable.image_error_placeholder)
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
            ,
        ) {
            // 展示汉服数量
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (hanfu.material.isNotBlank()) {
                    FloatingPriceTag(hanfu.material)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (hanfu.size.isNotBlank()) {
                    FloatingPriceTag(hanfu.size)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (hanfu.date.isNotBlank()) {
                    FloatingPriceTag(hanfu.date)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    activeImageUrl?.let { file ->
        FullScreenImageDialog(
            imagePath = file,
            onDismiss = {
                activeImageUrl = null // 👈 关闭时清空状态即可
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HanfuForm(
    submitState: SubmitState,
    hanfu: Hanfu,
    hanfuImages: List<HanfuImage>,
    onSave: (hanfu: Hanfu, coverIndex: Int, newImages: List<Uri>, deleteImages: List<HanfuImage>) -> Unit
) {
    val name = rememberTextFieldState(hanfu.name ?: "")
    val price = rememberTextFieldState(hanfu.price ?: "")
    val material = rememberTextFieldState(hanfu.material ?: "")
    val size = rememberTextFieldState(hanfu.size ?: "")
    val date = rememberTextFieldState(hanfu.date ?: "")
    var cover by remember { mutableStateOf(hanfu.cover ?: "") }
    val nameInteractionSource = remember { MutableInteractionSource() }
    val priceInteractionSource = remember { MutableInteractionSource() }
    val dateInteractionSource = remember { MutableInteractionSource() }
    val sizeInteractionSource = remember { MutableInteractionSource() }
    val materialInteractionSource = remember { MutableInteractionSource() }
    // 当没有图片封面时，根据选中的图片设置封面
    val coverIndexState = remember { mutableIntStateOf(0) }
    val isNameError = remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 1. 存储右侧已上传/选择的图片 Uri 列表（核心状态）
    val mutableHanfuImages = remember(hanfuImages) {
        mutableStateListOf<HanfuImage>().apply { addAll(hanfuImages) }
    }
    val newImageList = remember { mutableStateListOf<Uri>() }
    val deleteImageList = remember { mutableStateListOf<HanfuImage>() }

    // 2. 临时存储拍照的 Uri
    var cameraPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // 3. 控制选择弹窗的显示
    var showDialog by remember { mutableStateOf(false) }

    // 4. 图库选择 Launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            newImageList.add(it)
        }
    }

    // 5. 拍照 Launcher
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraPhotoUri?.let {
                newImageList.add(it)
            }
        }
    }

    // 监听名称输入框发生变化就把名称错误提示隐藏
    LaunchedEffect(name) {
        snapshotFlow { name.text.toString() } // 1. 将 TextField 的变化转化为 Flow 流
            // .debounce(300) // 进阶：如果你在做搜索，可以加个 300 毫秒防抖，防止频繁请求
            .collect { currentText ->
                isNameError.value = false
            }
    }

    // 弹窗逻辑
    if (showDialog) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.title_upload_image))
            },
            onDismissRequest = { showDialog = false },
            containerColor = MaterialTheme.colorScheme.background,
            text = {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .neu(
                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                shadowElevation = 6.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(12.dp)),
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledContentColor = Color.Gray
                        ),
                        onClick = {
                            showDialog = false
                            // 创建临时图片文件用于拍照存放
                            val photoFile = File(
                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "camera_hihanfu_${System.currentTimeMillis()}.jpg"
                            )
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
                            cameraPhotoUri = uri
                            takePictureLauncher.launch(uri)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.take_photo)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .neu(
                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                shadowElevation = 6.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(12.dp)),
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledContentColor = Color.Gray
                        ),
                        onClick = {
                            showDialog = false
                            pickImageLauncher.launch("image/*")
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.select_from_gallery)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
    ) {
        // 6. 整体水平布局容器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(186.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 【左侧区域】：图片列表（占据剩余的所有空间）
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (hanfuImages.isEmpty() && newImageList.isEmpty()) {
                    // 空的，未上传图片和选择图片
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // 水平方向居中（交叉轴）
                    ) {

                        Icon(
                            painter = painterResource(id = Screen.HanfuCabinetNav.icon),
                            contentDescription = stringResource(Screen.HanfuNav.label),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(stringResource(R.string.no_img))
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth().height(170.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // 图片之间的间距
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        itemsIndexed(mutableHanfuImages) { index, img ->
                            Box(
                                modifier = Modifier
                                    .height(170.dp).clip(RoundedCornerShape(8.dp)) // 💡 1. 规定整个图片容器的大小
                            ) {
                                AsyncImage(
                                    model = File(context.filesDir, img.path), // Coil 可以直接接收 File 对象作为数据源
                                    contentDescription = "加载自私有相对路径的图片",
                                    modifier = Modifier
                                        .height(170.dp)
                                        .wrapContentWidth()
                                        .then(
                                            if (!cover.isBlank() && img.path == cover) Modifier.neu(
                                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                                shadowElevation = 6.dp,
                                                lightSource = LightSource.LEFT_TOP,
                                                shape = Flat(RoundedCorner(12.dp)),
                                            ) else Modifier
                                        )
                                        .clickable {
                                            // 设置当前封面
                                            cover = img.path
                                        },
                                    contentScale = ContentScale.FillHeight,
                                    // 建议加上错误占位图，防范文件未下载完、被删除或路径拼错的情况
//                        error = painterResource(id = R.drawable.image_error_placeholder)
                                )

                                // 删除按钮：层层叠加在图片上方，并对齐到右上角 (TopEnd)
                                IconButton(
                                    onClick = {
                                        // 如果删除的是当前封面的数据
                                        if (cover.isNotBlank() && cover == img.path) {
                                            if (mutableHanfuImages.size > 1) {
                                                if (index == 0) {
                                                    // 删除的是第一个，则设置第二个为封面
                                                    cover = mutableHanfuImages[1].path
                                                } else {
                                                    cover = mutableHanfuImages[0].path
                                                }
                                            } else {
                                                cover = ""
                                            }
                                        }
                                        deleteImageList.add(img)
                                        mutableHanfuImages.removeAt(index)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd) // 💡 2. 核心：精准定位到右上方
                                        .offset(x = 4.dp, y = (-4.dp)) // 💡 3. 进阶：微调位置，让按钮稍微往外飘出一点（根据视觉微调）
                                        .size(24.dp) // 控制删除按钮的整体大小
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.close_circle_fill), // 或者使用你的 R.drawable.delete 图标
                                        contentDescription = stringResource(R.string.delete_img),
                                        tint = Color.Red, // 设置为显眼的红色或白色
                                        modifier = Modifier.size(24.dp) // 图标实际大小
                                    )
                                }
                            }
                        }

                        itemsIndexed(newImageList) { index, uri ->
                            Box(
                                modifier = Modifier
                                    .height(170.dp).clip(RoundedCornerShape(8.dp)) // 💡 1. 规定整个图片容器的大小
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = uri),
                                    contentDescription = "Uploaded Image",
                                    modifier = Modifier
                                        .height(170.dp)
                                        // 不管图片好没好，先以高度为基准撑开一个 9:16 的空间，打破 0 宽度的死锁
                                        .aspectRatio(9f / 16f, matchHeightConstraintsFirst = true)
                                        .wrapContentWidth()
                                        .then(
                                            if (hanfu.cover.isBlank() && index == coverIndexState.intValue) Modifier.neu(
                                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                                shadowElevation = 6.dp,
                                                lightSource = LightSource.LEFT_TOP,
                                                shape = Flat(RoundedCorner(12.dp)),
                                            ) else Modifier
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            // 设置当前封面
                                            cover = ""
                                            coverIndexState.intValue = index
                                        },
                                    contentScale = ContentScale.FillHeight
                                )

                                // 删除按钮：层层叠加在图片上方，并对齐到右上角 (TopEnd)
                                IconButton(
                                    onClick = {
                                        // 删除选择未上传的图片，如果删除的索引时当前封面，则将封面索引设置为0
                                        newImageList.removeAt(index)
                                        if (hanfu.cover.isBlank() && coverIndexState.intValue == index) {
                                            coverIndexState.intValue = 0
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd) // 💡 2. 核心：精准定位到右上方
                                        .offset(x = 4.dp, y = (-4.dp)) // 💡 3. 进阶：微调位置，让按钮稍微往外飘出一点（根据视觉微调）
                                        .size(24.dp) // 控制删除按钮的整体大小
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.close_circle_fill), // 或者使用你的 R.drawable.delete 图标
                                        contentDescription = stringResource(R.string.delete_img),
                                        tint = Color.Red, // 设置为显眼的红色或白色
                                        modifier = Modifier.size(24.dp) // 图标实际大小
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box (
                modifier = Modifier.padding(start = 8.dp)
            ){
                Button(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(70.dp)
                        .height(170.dp)
                        .neu(
                            lightShadowColor = MaterialTheme.colorScheme.tertiary,
                            darkShadowColor = MaterialTheme.colorScheme.surface,
                            shadowElevation = 6.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(12.dp)),
                        )
                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = Color.Gray
                    ),
                    onClick = {
                        showDialog = true
                    }
                ) {
                    // 在这里放置你右侧需要的按钮、图标或文字（例如：">" 箭头或 "更多"）
                    Icon(
                        painter = painterResource(id = R.drawable.add_fill),
                        contentDescription = stringResource(R.string.description_add)
                    )
                }
            }
            Spacer(modifier = Modifier.height(186.dp).width(16.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            // 输入框
            BasicTextField(
                state = name, // 使用 state 替代 value/onValueChange
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                lineLimits = TextFieldLineLimits.SingleLine, // 替代 singleLine = true
                interactionSource = nameInteractionSource,
                // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
                decorator = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = name.text.toString(), // 桥接读取文本
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = nameInteractionSource,
                        placeholder = { Text(stringResource(R.string.placeholder_hanfu_name)) },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = nameInteractionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    // 1. 未聚焦时的背景色（例如浅灰色）
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 2. 聚焦打字时的背景色（例如纯白色）
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 3. 禁用状态下的背景色
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )

            // 💡 如果出错，在输入框正下方就地抛出提示
            if (isNameError.value) {
                Text(
                    text = stringResource(R.string.error_name_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 输入框
            BasicTextField(
                state = price, // 使用 state 替代 value/onValueChange
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                lineLimits = TextFieldLineLimits.SingleLine, // 替代 singleLine = true
                interactionSource = priceInteractionSource,
                // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
                decorator = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = price.text.toString(), // 桥接读取文本
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = priceInteractionSource,
                        placeholder = { Text(stringResource(R.string.placeholder_hanfu_price)) },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = priceInteractionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    // 1. 未聚焦时的背景色（例如浅灰色）
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 2. 聚焦打字时的背景色（例如纯白色）
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 3. 禁用状态下的背景色
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 输入框
            BasicTextField(
                state = material, // 使用 state 替代 value/onValueChange
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                lineLimits = TextFieldLineLimits.SingleLine, // 替代 singleLine = true
                interactionSource = materialInteractionSource,
                // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
                decorator = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = material.text.toString(), // 桥接读取文本
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = materialInteractionSource,
                        placeholder = { Text(stringResource(R.string.placeholder_hanfu_material)) },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = materialInteractionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    // 1. 未聚焦时的背景色（例如浅灰色）
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 2. 聚焦打字时的背景色（例如纯白色）
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 3. 禁用状态下的背景色
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 输入框
            BasicTextField(
                state = size, // 使用 state 替代 value/onValueChange
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                lineLimits = TextFieldLineLimits.SingleLine, // 替代 singleLine = true
                interactionSource = sizeInteractionSource,
                // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
                decorator = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = size.text.toString(), // 桥接读取文本
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = sizeInteractionSource,
                        placeholder = { Text(stringResource(R.string.placeholder_hanfu_size)) },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = sizeInteractionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    // 1. 未聚焦时的背景色（例如浅灰色）
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 2. 聚焦打字时的背景色（例如纯白色）
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 3. 禁用状态下的背景色
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 输入框
            BasicTextField(
                state = date, // 使用 state 替代 value/onValueChange
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                lineLimits = TextFieldLineLimits.SingleLine, // 替代 singleLine = true
                interactionSource = dateInteractionSource,
                // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
                decorator = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = date.text.toString(), // 桥接读取文本
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = dateInteractionSource,
                        placeholder = { Text(stringResource(R.string.placeholder_hanfu_date)) },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = dateInteractionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    // 1. 未聚焦时的背景色（例如浅灰色）
                                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 2. 聚焦打字时的背景色（例如纯白色）
                                    focusedContainerColor = MaterialTheme.colorScheme.background,
                                    // 3. 禁用状态下的背景色
                                    disabledContainerColor = MaterialTheme.colorScheme.background,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .neu(
                        lightShadowColor = MaterialTheme.colorScheme.tertiary,
                        darkShadowColor = MaterialTheme.colorScheme.surface,
                        shadowElevation = 6.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp)),
                    )
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = Color.Gray
                ),
                onClick = {
                    if (name.text.toString().isBlank()) {
                        isNameError.value = true
                    } else {
                        if (hanfu.id == 0L) {
                            onSave(Hanfu(
                                cabinetId = hanfu.cabinetId,
                                name = name.text.toString(),
                                price = price.text.toString(),
                                date = date.text.toString(),
                                material = material.text.toString(),
                                size = size.text.toString(),
                                cover = cover
                            ), coverIndexState.intValue, newImageList, deleteImageList)
                        } else {
                            onSave(Hanfu(
                                id = hanfu.id,
                                cabinetId = hanfu.cabinetId,
                                name = name.text.toString(),
                                price = price.text.toString(),
                                date = date.text.toString(),
                                material = material.text.toString(),
                                size = size.text.toString(),
                                cover = cover
                            ), coverIndexState.intValue,newImageList, deleteImageList)
                        }
                    }
                },
                // 💡 交互核心 3：正在加载时，禁用按钮，防止用户连续狂点导致重复上传
                enabled = submitState != SubmitState.Loading
            ) {
                if (submitState == SubmitState.Loading) {
                    // 正在加载时的内部小菊花动画
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 1.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(stringResource(if (hanfu.id == 0L) R.string.button_save else R.string.button_update))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}