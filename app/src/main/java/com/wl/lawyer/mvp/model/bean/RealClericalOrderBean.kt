package com.wl.lawyer.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RealClericalOrderBean(
    @SerializedName("id") val id: Int,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("order_type") val orderType: Int,
    @SerializedName("pay_order_no") val payrderNo: String,
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("user_id") val uId: Int,
    @SerializedName("user_memo") val memo: String,
    @SerializedName("specs") val specs: String,
    @SerializedName("refer_price") val referPrice: String,
    @SerializedName("earnest_price") val earnestPrice: String,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("real_pay_price") val realPayPrice: String,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("lawyer_id") val lawyerId: Int,
    @SerializedName("lawyer_status") val lawyerStatus: Int,
    @SerializedName("pay_way") val payWay: String,
    @SerializedName("pay_status") val payStatus: Int,
    @SerializedName("pay_time") val payTime: Long,
    @SerializedName("createtime") val createtime: Long,
    @SerializedName("updatetime") val updatetime: Long,
    @SerializedName("deletetime") val deletetime: Any,
    @SerializedName("service_lawyer_receipt_time") val receiptTime: Any,
    @SerializedName("service_lawyer_end_time") val endTime: Any,
    @SerializedName("order_status") val orderStatus: Int,
    @SerializedName("lawyer_status_text") val lawyerStatusText: String,
    @SerializedName("pay_status_text") val payStatusText: String,
    @SerializedName("pay_time_text") val payTimeText: String,
    @SerializedName("order_status_text") val orderStatusText: String
) : Serializable
/*
*     "id": 30,
      "order_no": "EW20200513210458261378",
      "order_type": 2,
      "pay_order_no": "",
      "transaction_id": "",
      "user_id": 27,
      "user_memo": "",
      "specs": "服务类型:文书处理,文书类型:文书A,协助方式:审查",
      "refer_price": "2.00",
      "earnest_price": "30.00",
      "total_price": "30.00",
      "real_pay_price": "30.00",
      "file_path": "",
      "lawyer_id": 13,
      "lawyer_status": 0,
      "pay_way": "money",
      "pay_status": 1,
      "pay_time": 1589375117,
      "createtime": 1589375098,
      "updatetime": 1589375117,
      "deletetime": null,
      "service_lawyer_receipt_time": null,
      "service_lawyer_end_time": 0,
      "order_status": 0,
      "lawyer_status_text": "Lawyer_status 0",
      "pay_status_text": "Pay_status 1",
      "pay_time_text": "2020-05-13 21:05:17",
      "order_status_text": "待接单"
*
* */