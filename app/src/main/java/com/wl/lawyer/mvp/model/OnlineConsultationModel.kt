package com.wl.lawyer.mvp.model

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import javax.inject.Inject

import com.wl.lawyer.mvp.contract.OnlineConsultationContract
import com.wl.lawyer.mvp.model.api.BaseResponse
import com.wl.lawyer.mvp.model.api.service.CommonService
import com.wl.lawyer.mvp.model.bean.ConsultOrderBean
import io.reactivex.Observable


@ActivityScope
class OnlineConsultationModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    OnlineConsultationContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getServiceType() = mRepositoryManager.obtainRetrofitService(CommonService::class.java).serviceType()

    override fun createOrder(
        serviceId: Int,
        lawyerId: Int
    ): Observable<BaseResponse<ConsultOrderBean>> =
        mRepositoryManager.obtainRetrofitService(CommonService::class.java).createOrder(serviceId, lawyerId)
}
