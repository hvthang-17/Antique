package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.*
import com.example.antique.model.remote.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(val context: Application) : AndroidViewModel(context) {
    private val categoryRepository = CategoryRepository
    private val placeHolderCategory =
        Category(
            cid = -1,
            name = "",
            description = "",
            image = "",
        )

    val category = mutableStateOf(placeHolderCategory)
    val categories = mutableStateOf<List<Category>>(emptyList())
    val errorMessage = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = categoryRepository.getAllCategories()
                categories.value = categoryList
            } catch (e: Exception) {
                errorMessage.value = "Không thể lấy danh sách danh mục"
                e.printStackTrace()
            }
        }
    }

    fun setCurrentCategory(categoryId: String) {
        if (categoryId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loading.value = true
                    val tempCategory = categoryRepository.getCategoryById(categoryId)
                    if (tempCategory != null) {
                        category.value = tempCategory
                    } else {
                        errorMessage.value = "Danh mục không tồn tại"
                    }
                } catch (e: Exception) {
                    errorMessage.value = "Không thể tải thông tin danh mục"
                    e.printStackTrace()
                } finally {
                    loading.value = false
                }
            }
        } else {
            errorMessage.value = "ID danh mục không hợp lệ"
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(categoryId)
            getCategories()
        }
    }
}
