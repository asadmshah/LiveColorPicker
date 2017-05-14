package com.asadmshah.livecolorpicker.storage

import io.reactivex.Observable

interface Storage {

    fun colorsFileStream(): Observable<String>

}