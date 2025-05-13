package com.example.antique.viewmodel.admin

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.remote.entity.Category
import com.example.antique.model.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ManageCategoryVM(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val categoryCollection = db.collection("category")
    private val categoryRepository = CategoryRepository

    private val placeholderCategory = Category(
        cid = 0,
        name = "",
        description = "",
        image = "",
    )

    private val currentCategory = mutableStateOf(placeholderCategory)
    var cid by mutableStateOf(0)
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var imageUrl by mutableStateOf("")

    suspend fun isCidExists(cid: Int): Boolean {
        return try {
            val result = categoryCollection.whereEqualTo("cid", cid).get().await()
            result.size() > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun setCurrentCategory(category: Category) {
        currentCategory.value = category
        cid = category.cid
        name = category.name
        description = category.description
        imageUrl = category.image
    }

    fun resetCurrentCategory() {
        currentCategory.value = placeholderCategory
        cid = 0
        name = ""
        description = ""
        imageUrl = ""
    }

    fun addCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isCidExists(cid)) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(getApplication(), "Mã danh mục đã tồn tại!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val newCategory = Category(
                    cid = cid,
                    name = name,
                    description = description,
                    image = imageUrl
                )

                categoryRepository.insertCategory(newCategory)

                launch(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Thêm danh mục thành công", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val originalCid = currentCategory.value.cid
                if (cid != originalCid && isCidExists(cid)) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(getApplication(), "Mã danh mục đã tồn tại!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val updatedCategory = currentCategory.value.copy(
                    cid = cid,
                    name = name,
                    description = description,
                    image = imageUrl
                )

                categoryRepository.updateCategory(updatedCategory)

                launch(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
