package com.example.antique.model.remote.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.Uploader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

object CloudinaryManager {

    private var cloudinary: Cloudinary? = null

    fun init(context: Context) {
        if (cloudinary == null) {
            val config = mapOf(
                "cloud_name" to "dvl4fsgce",
                "api_key" to "792343568726829",
                "api_secret" to "4wO5KzVOEDcN4HxQpWk78YQWEYw",
                "secure" to true
            )
            cloudinary = Cloudinary(config)
        }
    }

    fun uploadImage(
        context: Context,
        fileUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        init(context)
        val uploader: Uploader = cloudinary!!.uploader()
        val publicId = "antique_${UUID.randomUUID()}"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val result: Map<*, *> = uploader.upload(inputStream, mapOf("public_id" to publicId))
                val url = result["secure_url"] as? String
                withContext(Dispatchers.Main) {
                    if (url != null) {
                        onSuccess(url)
                    } else {
                        onError("Không thể lấy URL ảnh từ Cloudinary.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Lỗi khi upload: ${e.localizedMessage}")
                }
            }
        }
    }
}
