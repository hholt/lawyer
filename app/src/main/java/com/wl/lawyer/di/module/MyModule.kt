package com.wl.lawyer.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.wl.lawyer.mvp.contract.MyContract
import com.wl.lawyer.mvp.model.MyModel


@Module
//构建MyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MyModule(private val view: MyContract.View) {
    @FragmentScope
    @Provides
    fun provideMyView(): MyContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideMyModel(model: MyModel): MyContract.Model {
        return model
    }
}
