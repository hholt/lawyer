package com.wl.lawyer.mvp.model

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import javax.inject.Inject

import com.wl.lawyer.mvp.contract.GraphicConsultationContract
import com.wl.lawyer.mvp.model.api.BaseResponse
import com.wl.lawyer.mvp.model.api.service.CommonService
import com.wl.lawyer.mvp.model.bean.GraphicConsultationBean
import io.reactivex.Observable


@ActivityScope
class GraphicConsultationModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    GraphicConsultationContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getPTCList() = mRepositoryManager.obtainRetrofitService(CommonService::class.java).getPTCList()
}
