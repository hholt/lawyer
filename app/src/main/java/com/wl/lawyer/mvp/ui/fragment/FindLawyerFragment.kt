package com.wl.lawyer.mvp.ui.fragment

import android.hardware.input.InputManager
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.jess.arms.di.component.AppComponent
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.isGone
import com.lxj.androidktx.core.visible
import com.wl.lawyer.R
import com.wl.lawyer.app.AppConstant
import com.wl.lawyer.app.RouterArgs
import com.wl.lawyer.app.RouterPath
import com.wl.lawyer.app.base.BaseSupportFragment
import com.wl.lawyer.app.mlog
import com.wl.lawyer.app.utils.RVUtils
import com.wl.lawyer.di.component.DaggerFindLawyerComponent
import com.wl.lawyer.di.module.FindLawyerModule
import com.wl.lawyer.mvp.contract.FindLawyerContract
import com.wl.lawyer.mvp.model.bean.*
import com.wl.lawyer.mvp.presenter.FindLawyerPresenter
import com.wl.lawyer.mvp.ui.adapter.AreaAdapter
import com.wl.lawyer.mvp.ui.adapter.CategoryAdapter
import com.wl.lawyer.mvp.ui.adapter.LawAdapter
import com.wl.lawyer.mvp.ui.adapter.SortAdapter
import com.wl.lawyer.mvp.ui.callback.LawyerQuickDiff
import kotlinx.android.synthetic.main.fragment_find_lawyer.*
import kotlinx.android.synthetic.main.include_find_law.*

/**
 * 找律师
 */
class FindLawyerFragment : BaseSupportFragment<FindLawyerPresenter>(), FindLawyerContract.View {
    companion object {
        fun newInstance(): FindLawyerFragment {
            val fragment = FindLawyerFragment()
            return fragment
        }
    }

    var lastData: FindLawyerBean? = null
    var dataList: MutableList<LawyerBean> = arrayListOf<LawyerBean>()
    var allLoad = false
    var keyword = ""
    var province: SearchBean.AreaBean? = null
    var city: SearchBean.AreaBean? = null
    var block: SearchBean.AreaBean? = null
    var category: CategoryBean? = null
    var service: LawyerServiceBean? = null
    var sortBy: SearchBean.SortBean? = null
    var selectorVisible = false
    val mFooterView: View by lazy {
        RVUtils.myFooterView(mContext, null)
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerFindLawyerComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .findLawyerModule(FindLawyerModule(this))
            .build()
            .inject(this)
    }

    private val lawAdapter by lazy {
        LawAdapter(
            arrayListOf()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    ARouter.getInstance().build(RouterPath.LAWYER_ACTIVITY)
                        .withSerializable(RouterArgs.LAWYER, this)
                        .navigation()
                }
            }

            /*setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
                mPresenter?.loadMore(1)
            }, rv_law)*/
        }
    }

    private val provinceAdapter by lazy {
        AreaAdapter(
            arrayListOf()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    if (position != mSelectItemPosition) {
                        blockAdapter.reset()
                        blockAdapter.setNewData(null)
                        cityAdapter.reset()
                        cityAdapter.setNewData(null)
                        notifyItemChanged(position)
                        notifyItemChanged(mSelectItemPosition)
                        mSelectItemPosition = position
                        mPresenter?.loadCityData(id)
                    }
                }
            }
        }
    }

    private val cityAdapter by lazy {
        AreaAdapter(
            arrayListOf()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    if (position != mSelectItemPosition) {
                        blockAdapter.reset()
                        blockAdapter.setNewData(null)
                        notifyItemChanged(position)
                        notifyItemChanged(mSelectItemPosition)
                        mSelectItemPosition = position
                        mPresenter?.loadBlockData(id)
                    }
                }
            }
        }
    }

    private val blockAdapter by lazy {
        AreaAdapter(
            arrayListOf()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    if (position != mSelectItemPosition) {
                        notifyItemChanged(position)
                        notifyItemChanged(mSelectItemPosition)
                        mSelectItemPosition = position
                    }
                }
            }
        }
    }


    private val categoryAdapter by lazy {
        CategoryAdapter(arrayListOf()).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    if (position != mSelectItemPosition) {
                        notifyItemChanged(position)
                        notifyItemChanged(mSelectItemPosition)
                        mSelectItemPosition = position
                    }
                }
            }
        }
    }

    private val sortAdapter by lazy {
        SortAdapter(
            arrayListOf()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                getItem(position)?.apply {
                    notifyItemChanged(position)
                    notifyItemChanged(mSelectItemPosition)
                    mSelectItemPosition = position
                }
            }
        }
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_find_lawyer, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mlog(activity?.window?.attributes?.softInputMode!!.toString())
        mPresenter?.loadData()
        initSelector()
        initSelectorAdapter()
        initAdapter()

        iv_clear.click {
            et_search.setText("")
            hideSoftInput()
        }

        et_search.maxLines = 1
        et_search.setOnEditorActionListener { _, actionId, event ->
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                keyword = et_search.text.toString()
                mPresenter?.search()
                hideSoftInput()
            }
            event.keyCode == KeyEvent.KEYCODE_ENTER

        }


        btn_reset.click {
            provinceAdapter.reset()
            provinceAdapter.notifyDataSetChanged()
            cityAdapter.reset()
            cityAdapter.notifyDataSetChanged()
            blockAdapter.reset()
            blockAdapter.notifyDataSetChanged()
            categoryAdapter.reset()
            categoryAdapter.notifyDataSetChanged()
            sortAdapter.reset()
            sortAdapter.notifyDataSetChanged()
            province = null
            city = null
            block = null
            category = null
            sortBy = null
            mPresenter?.search()
            dismisssSelector()
        }
        btn_confirm.click {
            province = provinceAdapter.getSelectItem()
            city = cityAdapter.getSelectItem()
            block = blockAdapter.getSelectItem()
            category = categoryAdapter.getSelectItem()
            sortBy = sortAdapter.getSelectItem()
            mPresenter?.search()
            dismisssSelector()
        }
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        ImmersionBar.with(this)
            .titleBar(ll_find_lawyer)
            .init()
    }

    private fun initSelector() {
        tv_area.click {
            showSelector(layout_area_selector)

        }
        tv_category.click {
            showSelector(layout_category_selector)

        }
        tv_sort.click {
            showSelector(layout_sort_selector)
        }
    }

    private fun showSelector(v: View) {
        layout_area_selector.gone()
        layout_category_selector.gone()
        layout_sort_selector.gone()
        layout_menu_detail.visible()
        v.visible()
        selectorVisible = true
    }

    private fun dismisssSelector() {
        layout_area_selector.gone()
        layout_category_selector.gone()
        layout_sort_selector.gone()
        layout_menu_detail.gone()
        selectorVisible = false
    }

    private fun initSelectorAdapter() {
        rv_province_selector.layoutManager = LinearLayoutManager(mContext)
        rv_province_selector.adapter = provinceAdapter
        rv_city_selector.layoutManager = LinearLayoutManager(mContext)
        rv_city_selector.adapter = cityAdapter
        rv_block_selector.layoutManager = LinearLayoutManager(mContext)
        rv_block_selector.adapter = blockAdapter
        rv_catagory_selector.layoutManager = LinearLayoutManager(mContext)
        rv_catagory_selector.adapter = categoryAdapter
        rv_sort_selector.layoutManager = LinearLayoutManager(mContext)
        rv_sort_selector.adapter = sortAdapter
        RVUtils.myDivider(mContext, rv_province_selector)
        RVUtils.myDivider(mContext, rv_city_selector)
        RVUtils.myDivider(mContext, rv_block_selector)
        RVUtils.myDivider(mContext, rv_catagory_selector)
        RVUtils.myDivider(mContext, rv_sort_selector)
    }

    private fun initAdapter() {
        rv_law.layoutManager = LinearLayoutManager(mContext)
        rv_law.adapter = lawAdapter
        rv_law.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (selectorVisible) dismisssSelector()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!allLoad && !mPresenter!!.isLoadingMore) {
                    var manager = recyclerView.layoutManager
                    manager?.getChildAt(0)?.let {
                        var position = recyclerView.getChildViewHolder(it).adapterPosition
                        lastData?.let {
                            if (position + manager.childCount == dataList.size) {
                                mPresenter?.loadMore(it.currentPage + 1)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        lawAdapter.emptyView = getMEmptyView()
    }

    override fun setData(data: Any?) {

    }

    private fun getFooterView(): View {
        return layoutInflater.inflate(R.layout.include_law_footer, rv_law, false)
    }

    private fun getMEmptyView(): View {
        return layoutInflater.inflate(R.layout.layout_no_data, rv_law, false)
    }

    override fun updateAdapter(findLawyerBean: FindLawyerBean) {
        lawAdapter.setNewData(findLawyerBean.lawyerList)
        lastData = findLawyerBean
        dataList.clear()
        dataList.addAll(findLawyerBean.lawyerList)
        handleFooterView(findLawyerBean)
    }

    override fun onMoreData(findLawyerBean: FindLawyerBean) {
        lastData = findLawyerBean
        dataList.addAll(findLawyerBean.lawyerList)
        lawAdapter.setNewDiffData(LawyerQuickDiff(dataList))
        handleFooterView(findLawyerBean)
    }

    fun handleFooterView(findLawyerBean: FindLawyerBean) {
        allLoad = findLawyerBean.currentPage == findLawyerBean.lastPage
        mlog(allLoad.toString())
        if (allLoad) {
            if (lawAdapter.footerLayoutCount==0) lawAdapter.addFooterView(mFooterView)
        } else {
            if (lawAdapter.footerLayoutCount==1) lawAdapter.removeFooterView(mFooterView)
        }
    }

    override fun onSearchFieldGet(searchBean: SearchBean) {
    }

    override fun initProvinceAdapter(data: List<SearchBean.AreaBean>) {
        provinceAdapter.setNewData(data)
    }

    override fun initCategoryAdapter(data: List<CategoryBean>) {
        categoryAdapter.setNewData(data)
    }

    override fun initSortAdapter(data: List<SearchBean.SortBean>) {
        sortAdapter.setNewData(data)
    }

    override fun updateCityAdapter(data: List<SearchBean.AreaBean>) {
        cityAdapter.setNewData(data)
    }

    override fun updateBlockAdapter(data: List<SearchBean.AreaBean>) {
        blockAdapter.setNewData(data)
    }

    override fun onSearch(findLawyerBean: FindLawyerBean) {
        lawAdapter.setNewData(findLawyerBean.lawyerList)
    }

    override fun getKeyWord() = keyword


    override fun getProvinceId() = province?.id ?: 0

    override fun getCityId() = city?.id ?: 0

    override fun getBlockId() = block?.id ?: 0

    override fun getCategoryId() = category?.id?.toString() ?: ""

    override fun getServiceId() = service?.id?.toString() ?: ""

    override fun getSortBy() = sortBy?.field ?: ""
}
