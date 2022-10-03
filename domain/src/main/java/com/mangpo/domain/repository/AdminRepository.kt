package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity

interface AdminRepository {
    suspend fun deleteUser(userId: Int): BaseResEntity<Nothing>
}