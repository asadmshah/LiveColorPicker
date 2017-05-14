package com.asadmshah.livecolorpicker.storage

import android.content.Context
import com.asadmshah.livecolorpicker.R
import io.reactivex.Observable

internal class StorageImpl(val context: Context) : Storage {

    override fun colorsFileStream(): Observable<String> {
        return Observable
                .create<String> { emitter ->
                    context.resources.openRawResource(R.raw.colors).use {
                        it.bufferedReader().useLines { sequence ->
                            sequence.filterIndexed { i, _ -> i > 0 }
                                    .forEach {
                                        emitter.onNext(it)
                                    }
                        }
                    }
                    emitter.onComplete()
                }
    }

}