package online.hicode.android.hihanfu.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import online.hicode.android.hihanfu.R
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.neumorphism.LightSource
import online.hicode.android.hihanfu.neumorphism.neu
import online.hicode.android.hihanfu.neumorphism.shape.Flat
import online.hicode.android.hihanfu.neumorphism.shape.Pressed
import online.hicode.android.hihanfu.neumorphism.shape.RoundedCorner
import online.hicode.android.hihanfu.ui.components.DeleteConfirmDialog
import online.hicode.android.hihanfu.ui.components.UiState
import online.hicode.android.hihanfu.ui.navigation.BottomNavigationBar
import online.hicode.android.hihanfu.ui.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JewelryCabinetScreen(
    navController: NavHostController,
    jewelryCabinetViewModel: JewelryCabinetViewModel
) {
    // cabinet 列表数据
    val jewelryCabinetListUiState by jewelryCabinetViewModel.jewelryCabinetListUiState.collectAsState()
    val jewelryCabinetStatisticsUiState by jewelryCabinetViewModel.jewelryCabinetStatisticsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    // 新增编辑汉服柜抽屉
    val showSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        // locked
        skipPartiallyExpanded = true
    )

    var editableCabinet by remember {
        mutableStateOf(JewelryCabinet(name = ""))
    }

    val showDeleteDialog = remember { mutableStateOf(false) }

    val statisticsMap = remember(jewelryCabinetStatisticsUiState) {
        when (val uiState = jewelryCabinetStatisticsUiState) {
            is UiState.Success -> {
                val jewelryCabinetStatisticsList = uiState.content // 自动识别为 List<HanfuEntity>
                jewelryCabinetStatisticsList.associateBy { it.cabinetId }
            }
            else -> emptyMap()
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
                title = { Text(stringResource(Screen.JewelryCabinetNav.label)) },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,     // 🔥 整个顶栏的背景颜色
                    titleContentColor = MaterialTheme.colorScheme.primary,        // 🔥 标题文字的颜色
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,// 🔥 左侧返回键等图标的颜色
                    actionIconContentColor = MaterialTheme.colorScheme.primary     // 🔥 右侧菜单等图标的颜色
                ),
                actions = {
                    // 新增汉服柜按钮
                    IconButton(onClick = {
                        editableCabinet = JewelryCabinet(
                            name = ""
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
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        when (val uiState = jewelryCabinetListUiState) {
            is UiState.Loading -> {
                // 展示加载
            }
            is UiState.Success -> {
                val jewelryCabinetList = uiState.content // 自动识别为 List<HanfuEntity>
                // 🎯 1. 判断列表是否为空
                if (jewelryCabinetList.isEmpty()) {
                    // 💡 空状态：直接在 item 作用域内放占位组件
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        JewelryCabinetEmptyPlaceholder()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                        ,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        // 💡 不为空：使用 items 自动进行高效循环渲染
                        items(jewelryCabinetList) { itemData ->
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp)
                            ) {
                                JewelryCabinetCard(
                                    navController = navController,
                                    data = itemData,
                                    count = statisticsMap[itemData.id]?.count ?: 0,
                                    onEdit = { cabinet ->
                                        editableCabinet = cabinet
                                        showSheet.value = true
                                    },
                                    onDelete = { cabinet ->
                                        editableCabinet = cabinet
                                        showDeleteDialog.value = true
                                    }
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Text(
                        text = uiState.message,
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
        ) {
            // Sheet 表单内容
            JewelryCabinetForm(
                editableCabinet,
                onSave = { cabinet ->
                    coroutineScope.launch {
                        if (cabinet.id > 0) {
                            jewelryCabinetViewModel.updateJewelryCabinet(cabinet)
                        } else {
                            jewelryCabinetViewModel.insertJewelryCabinet(cabinet)
                        }
                        showSheet.value = false
                    }
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
                jewelryCabinetViewModel.deleteJewelryCabinet(editableCabinet.id)
                showDeleteDialog.value = false
            }
        }
    )

}


@Composable
fun JewelryCabinetEmptyPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // 水平方向居中（交叉轴）
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            painter = painterResource(id = Screen.JewelryCabinetNav.icon),
            contentDescription = stringResource(Screen.JewelryCabinetNav.label),
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
fun JewelryCabinetCard(
    navController: NavHostController,
    data: JewelryCabinet,
    count: Int,
    onEdit: (cabinet: JewelryCabinet) -> Unit,
    onDelete: (cabinet: JewelryCabinet) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
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
                        navController.navigate(Screen.JewelryNav.route + "/" + data.id)
                    }
            )
        ,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = data.name,
                fontSize = 32.sp,
                modifier = Modifier
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                // 🎯 核心点：横向的所有子组件自动实现 Space Between 效果
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom // 顺便让它们在纵向上居中对齐
            ) {
                // 展示汉服数量
                Text(
                    text = count.toString()
                )

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
                            onEdit(data)
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
                            onDelete(data)
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JewelryCabinetForm(
    cabinet: JewelryCabinet,
    onSave: (cabinet: JewelryCabinet) -> Unit
) {
    val name = rememberTextFieldState(cabinet.name)
    val interactionSource = remember { MutableInteractionSource() }
    val isNameError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end= 16.dp)
            .then(
                if (isNameError.value) Modifier.height(150.dp) else Modifier.height(130.dp)
            )
    ) {
        // 输入框
        // 🎯 2. 使用全套新版参数
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
            interactionSource = interactionSource,
            // ✨ 新版参数名叫 decorator，内部使用专门匹配 TextFieldState 的重载
            decorator = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = name.text.toString(), // 桥接读取文本
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = { Text(stringResource(R.string.placeholder_jewelry_cabinet_name)) },
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp // 👈 调大这个值变高，调小这个值变矮
                    ),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
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
                            shape = RoundedCornerShape(8.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

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
                    if (cabinet.id == 0L) {
                        onSave(JewelryCabinet(
                            name = name.text.toString()
                        ))
                    } else {
                        onSave(JewelryCabinet(
                            id = cabinet.id,
                            name = name.text.toString()
                        ))
                    }
                }
            }
        ) {
            Text(stringResource(if (cabinet.id == 0L) R.string.button_save else R.string.button_update))
        }
    }
}