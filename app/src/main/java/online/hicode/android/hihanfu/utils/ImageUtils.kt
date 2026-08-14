package online.hicode.android.hihanfu.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * 将沙盒外部的 Uri 图片安全复制到 App 内部 data/files/images 目录下
 *
 * @param context 上下文
 * @param uri 选中的图片 Uri
 * @return 保存成功后返回本地沙盒文件的绝对路径 [String]，失败返回 null
 */
suspend fun saveUriToAppDataDir(context: Context, subPath: String, uri: Uri): String {
    try {
        // 1. 在 app 的 files 目录下创建一个名为指定子目录
        val imageFolder = File(context.filesDir, subPath).apply {
            if (!exists()) mkdirs() // 如果文件夹不存在则创建
        }

        // 2. 获取原图片的后缀名（例如 .jpg / .png），如果没有则默认为 .jpg
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        val extension = when (mimeType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            "image/heif" -> "heif"
            "image/heic" -> "heic"
            "image/x-adobe-dng" -> "dng"
            "image/dng" -> "dng"
            "image/gif" -> "gif"
            else -> "jpg"
        }

        // 3. 生成一个唯一的文件名，防止命名冲突覆盖旧图片
        val uniqueFileName = "hanfu_img_${UUID.randomUUID()}.$extension"
        val targetFile = File(imageFolder, uniqueFileName)

        // 4. 核心：通过输入输出流安全复制文件（.use 会自动关闭流，防止内存泄漏）
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(targetFile).use { outputStream ->
                inputStream.copyTo(outputStream) // 💡 管道流式复制，百兆大图也不会引发 OOM
            }
        }

        // 5. 返回保存成功后的绝对路径，后续可用于直接加载或存入数据库
        return "$subPath/$uniqueFileName"
    } catch (e: Exception) {
        e.printStackTrace()
        return "" // 发生异常（如权限失效、空间不足）返回 空字符串
    }
}