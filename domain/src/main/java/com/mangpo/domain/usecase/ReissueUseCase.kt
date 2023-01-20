package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.reissue.ReissueReqEntity
import com.mangpo.domain.model.reissue.ReissueResEntity
import com.mangpo.domain.repository.AuthRepository

class ReissueUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(reissueReqEntity: ReissueReqEntity): BaseResEntity<ReissueResEntity?> {
        return repository.reissue(reissueReqEntity)
    }
}