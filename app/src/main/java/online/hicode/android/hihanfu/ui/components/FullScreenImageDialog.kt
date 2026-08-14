package online.hicode.android.hihanfu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import java.io.File

/**
 * 全屏查看图片
 *
 */
@Composable
fun FullScreenImageDialog(
    imagePath: File,     // 支持本地沙盒 data 目录路径或网络 URL
    onDismiss: () -> Unit  // 点击关闭的回调
) {
    // 💡 1. 全屏配置参数：破开系统默认的边距限制，强行撑满整块屏幕
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // 👈 关键：禁用平台默认宽度限制（实现真正全屏）
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        // 💡 2. 手势缩放状态记录
        var scale by remember { mutableFloatStateOf(1f) }
        val state = rememberTransformableState { zoomChange, _, _ ->
            // 限制最小缩放为 1 倍，最大为 3 倍
            scale = (scale * zoomChange).coerceIn(1f, 3f)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // 沉浸式纯黑底色
                // 💡 3. 单击大图任意地方或黑色背景，直接关闭全屏退出
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "查看大图",
                contentScale = ContentScale.Fit, // 保证图片完整展示
                modifier = Modifier
                    .fillMaxSize()
                    // 💡 4. 挂载缩放手势监听器
                    .transformable(state = state)
                    // 💡 5. 图层物理缩放效果应用
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            )
        }
    }
}