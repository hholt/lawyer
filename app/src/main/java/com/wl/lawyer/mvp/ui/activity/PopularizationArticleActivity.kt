package com.wl.lawyer.mvp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jess.arms.di.component.AppComponent
import com.vondear.rxtool.RxKeyboardTool
import com.wl.lawyer.R
import com.wl.lawyer.app.*
import com.wl.lawyer.app.base.BaseSupportActivity
import com.wl.lawyer.app.utils.RVUtils
import com.wl.lawyer.di.component.DaggerPopularizationArticleComponent
import com.wl.lawyer.di.module.PopularizationArticleModule
import com.wl.lawyer.mvp.contract.PopularizationArticleContract
import com.wl.lawyer.mvp.model.bean.BaseListBean
import com.wl.lawyer.mvp.model.bean.LawPopularizationDataBean
import com.wl.lawyer.mvp.model.bean.LawyerArticleBean
import com.wl.lawyer.mvp.presenter.PopularizationArticlePresenter
import com.wl.lawyer.mvp.ui.adapter.LawPopularizationAdapter
import com.wl.lawyer.mvp.ui.callback.ArticleQuickDiff
import kotlinx.android.synthetic.main.activity_popularization_article.*
import kotlinx.android.synthetic.main.include.*

@Route(path = RouterPath.POPULARIZATION_ARTICLE_LIST)
class PopularizationArticleActivity: BaseSupportActivity<PopularizationArticlePresenter>(),
PopularizationArticleContract.View {

    var lastData: BaseListBean<LawyerArticleBean>? = null
    var articleList: MutableList<LawyerArticleBean> = arrayListOf<LawyerArticleBean>()
    var allLoad = false

    private val lawPopularizationAdapter by lazy {
        LawPopularizationAdapter(
            arrayListOf(
            )
        ).apply {
            setOnItemClickListener { _, _, position ->
                getItem(position)?.lawyerArticle?.let {
                    ARouter.getInstance()
                        .build(RouterPath.LAWYER_ARTICLE)
                        .withSerializable(RouterArgs.ARTICLE, it)
                        .navigation()
                }
            }
        }
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerPopularizationArticleComponent
            .builder()
            .appComponent(appComponent)
            .popularizationArticleModule(PopularizationArticleModule(this))
            .build()
            .inject(this)

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_popularization_article
    }

    override fun initData(savedInstanceState: Bundle?) {

        tv_title.text = "普法动态"
        dividing_line.visibility = View.GONE
        iv_right.visibility = View.VISIBLE
        iv_back.setOnClickListener { mPresenter?.mAppManager?.onBack() }
        iv_right.image(R.drawable.ic_share_it)
        iv_right.setOnClickListener { }

        RxKeyboardTool.hideSoftInput(this)

        initArticleList()

    }


    //普法动态
    private fun initArticleList() {
        rv_article_list.isNestedScrollingEnabled = false
        rv_article_list.isFocusable = false
        rv_article_list.layoutManager = LinearLayoutManager(mContext)
        rv_article_list.adapter = lawPopularizationAdapter
        rv_article_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!allLoad && !mPresenter!!.isLoadingMore) {
                    val manager = recyclerView.layoutManager
                    manager?.getChildAt(0)?.let {
                        val position = recyclerView.getChildViewHolder(it).adapterPosition
                        if (position + manager.childCount == articleList.size) {
                            mPresenter?.loadMore(articleList.size / AppConstant.PAGE_SIZE + 1)
                        }
                    }
                }
            }
        })
        mPresenter?.loadData()
    }

    override fun onDataLoad(articles: BaseListBean<LawyerArticleBean>) {
        lastData = articles
        articleList.clear()
        articleList.addAll(articles.list)
        updateAdapter()
    }

    override fun onDataMore(articles: BaseListBean<LawyerArticleBean>) {
        lastData = articles
        articleList.addAll(articles.list)
        updateAdapter()
    }

    fun updateAdapter() {
        var data = arrayListOf<LawPopularizationDataBean>()
        for (article in articleList) {
            data.add(LawPopularizationDataBean(article))
        }
        lawPopularizationAdapter.setNewDiffData(ArticleQuickDiff(data))
        articleList?.let {
            if (it.size == lastData?.totalCount) {
                allLoad = true
                lawPopularizationAdapter.addFooterView(RVUtils.myFooterView(mContext, null))
            }
        }
    }

}