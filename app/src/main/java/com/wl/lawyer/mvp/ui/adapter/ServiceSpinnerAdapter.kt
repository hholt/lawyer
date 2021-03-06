package com.wl.lawyer.mvp.ui.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView
import com.wl.lawyer.R
import com.wl.lawyer.mvp.model.bean.SpecBean

class ServiceSpinnerAdapter(powerSpinnerView: PowerSpinnerView, data: List<SpecBean>) :
    BaseQuickAdapter<SpecBean, BaseViewHolder>(
        R.layout.adapter_popview_text,
        data
    ),
    PowerSpinnerInterface<SpecBean> {

    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<SpecBean>? =  null
    override val spinnerView: PowerSpinnerView = powerSpinnerView

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun convert(helper: BaseViewHolder, item: SpecBean?) {
        item?.apply {
            helper.getView<AppCompatTextView>(R.id.tv_simple_desc).text = name
        }
    }

    override fun notifyItemSelected(index: Int) {
        spinnerView.notifyItemSelected(index, mData.get(index).name)
        onSpinnerItemSelectedListener?.onItemSelected(index, mData.get(index))
    }

    override fun setItems(itemList: List<SpecBean>) = setNewData(itemList)

}