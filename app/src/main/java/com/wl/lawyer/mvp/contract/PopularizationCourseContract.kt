package com.wl.lawyer.mvp.contract

import com.jess.arms.mvp.IView
import com.jess.arms.mvp.IModel
import com.wl.lawyer.mvp.model.api.BaseResponse
import com.wl.lawyer.mvp.model.bean.LiveListBean
import io.reactivex.Observable


interface PopularizationCourseContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onDataLoad(liveListBean: LiveListBean)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun loadLiveData(): Observable<BaseResponse<LiveListBean>>
    }

}
