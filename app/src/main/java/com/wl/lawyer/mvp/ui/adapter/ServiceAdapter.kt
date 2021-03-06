package com.wl.lawyer.mvp.ui.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.androidktx.core.logd
import com.wl.lawyer.R
import com.wl.lawyer.app.circleImage
import com.wl.lawyer.app.mlog
import com.wl.lawyer.mvp.model.api.Api
import com.wl.lawyer.mvp.model.bean.ServiceBean

/**
 * 服务项目
 */
class ServiceAdapter(data: MutableList<ServiceBean>?) :
    BaseSectionQuickAdapter<ServiceBean, BaseViewHolder>(
        R.layout.adapter_service,
        R.layout.adapter_recommended_lawyer_header,
        data
    ) {
    override fun convertHead(helper: BaseViewHolder?, item: ServiceBean?) {
        item?.header?.let {
            helper?.getView<AppCompatTextView>(R.id.tv_recommended)?.text = item?.header
        }
        helper?.getView<AppCompatTextView>(R.id.tv_more)?.visibility = View.GONE
    }

    override fun convert(helper: BaseViewHolder, item: ServiceBean?) {
        item?.serviceBean?.let {
            helper.getView<AppCompatImageView>(R.id.iv_service_img).circleImage(Api.Companion.APP_DOMAIN + it.iconImage)
            helper.getView<AppCompatTextView>(R.id.tv_service_title).text = it.name
            helper.getView<AppCompatTextView>(R.id.tv_service_desc).text = it.desc
            helper.getView<AppCompatTextView>(R.id.tv_service_price).text = "￥${it.price}/起"
        }
    }

}
