package com.asadmshah.livecolorpicker.screens

import com.asadmshah.livecolorpicker.scopes.ActivityScope
import com.asadmshah.livecolorpicker.screens.colors_list.ColorsListPresenter
import com.asadmshah.livecolorpicker.screens.main.MainPresenter
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(presenter: MainPresenter)
    fun inject(presenter: ColorsListPresenter)
}