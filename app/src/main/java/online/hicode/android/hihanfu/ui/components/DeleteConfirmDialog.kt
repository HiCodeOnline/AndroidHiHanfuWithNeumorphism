package online.hicode.android.hihanfu.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.hicode.android.hihanfu.R
import online.hicode.android.hihanfu.neumorphism.LightSource
import online.hicode.android.hihanfu.neumorphism.neu
import online.hicode.android.hihanfu.neumorphism.shape.Flat
import online.hicode.android.hihanfu.neumorphism.shape.RoundedCorner

@Composable
fun DeleteConfirmDialog(
    title: String,
    message: String,
    showDialog: Boolean = false,
    onDismiss: () -> Unit, // 点击取消或外部
    onConfirm: () -> Unit  // 点击确定删除
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            // 🎯 1. 顶部的警告图标
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.delete_bin_5_fill),
                    contentDescription = stringResource(R.string.description_delete),
                    tint = MaterialTheme.colorScheme.error // 红色警告色
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(16.dp),
            // 🎯 2. 简短明确的标题
            title = {
                Text(text = title)
            },
            // 🎯 3. 详细的风险说明
            text = {
                Text(text = message)
            },
            // 🎯 4. 确认按钮（根据 M3 规范，高风险按钮通常放在右侧，并使用 error 红色）
            confirmButton = {
                TextButton(onClick = {
                    onConfirm() // 执行删除逻辑
                    onDismiss()      // 关闭弹窗
                }) {
                    Text("确定删除", color = MaterialTheme.colorScheme.error)
                }
            },
            // 🎯 5. 取消按钮
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}