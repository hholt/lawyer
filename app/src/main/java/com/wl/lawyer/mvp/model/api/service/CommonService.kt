package com.wl.lawyer.mvp.model.api.service

import com.wl.lawyer.mvp.model.api.BaseResponse
import com.wl.lawyer.mvp.model.bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommonService {
    /**
     * ******************************* 文件 *******************************
     */

    /**
     * 上传文件
     */
    @Multipart
    @POST("/api/common/upload")
    fun uploadFile(@Part file: MultipartBody.Part): Observable<BaseResponse<String>>

    /**
     * ******************************* 首页 *******************************
     */
    @POST("/api/index/getIndexData")
    fun indexData(): Observable<BaseResponse<HomeDataBean>>

    /**
     * ******************************* 详情 *******************************
     */
    @GET("/api/lawyer/getLawyerDetail")
    fun lawyerData(@Query("id") id: Int): Observable<BaseResponse<LawyerDetailBean>>

    /**
     * ******************************* 在线咨询 *******************************
     */
    @GET("/api/consultation/getServiceType")
    fun serviceType(): Observable<BaseResponse<OnlineConsultlationBean>>
    /**
     * ******************************* 创建订单 *******************************
     */
    @GET("/api/consultation/createOrder")
    fun createOrder(@Query("package_id") serviceId: Int,
                    @Query("invite_lawyer_id") lawyerId: Int,
                    @Query("pay_type")payMethod: String): Observable<BaseResponse<CreateOrderBean>>
}