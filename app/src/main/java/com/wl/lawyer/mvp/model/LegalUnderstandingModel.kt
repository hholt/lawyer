package com.wl.lawyer.mvp.model

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.FragmentScope
import javax.inject.Inject

import com.wl.lawyer.mvp.contract.LegalUnderstandingContract
import com.wl.lawyer.mvp.model.api.service.CommonService


@FragmentScope
class LegalUnderstandingModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    LegalUnderstandingContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application
    override fun getArticles() =
        mRepositoryManager.obtainRetrofitService(CommonService::class.java).getArticles()

}
