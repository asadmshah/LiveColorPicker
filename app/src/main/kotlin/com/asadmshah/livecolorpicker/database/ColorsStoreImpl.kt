package com.asadmshah.livecolorpicker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.asadmshah.livecolorpicker.android.marshall
import com.asadmshah.livecolorpicker.android.unmarshall
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.models.ColorPalette
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

internal class ColorsStoreImpl(context: Context) : SQLiteOpenHelper(context, "Colors.db", null, 1), ColorsStore {

    private val writer: SQLiteDatabase by lazy { writableDatabase }
    private val reader: SQLiteDatabase by lazy { readableDatabase }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ColorsContract.QueryCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ColorsContract.QueryDropTable)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun insert(colorPalette: ColorPalette): Single<ColorPalette> {
        val values = ContentValues()
        values.put(ColorsContract.ColumnDate, colorPalette.date.time)
        values.put(ColorsContract.ColumnData, colorPalette.colors.marshall())
        writer.insert(ColorsContract.TableName, null, values)

        return read(colorPalette.date).toSingle()
    }

    override fun read(): Observable<ColorPalette> {
        return Observable
                .create<ColorPalette> { emitter ->
                    val projection = arrayOf(ColorsContract.ColumnDate, ColorsContract.ColumnData)
                    val sortOrder = "${ColorsContract.ColumnDate} DESC"
                    reader.query(ColorsContract.TableName, projection, null, null, null, null, sortOrder).use {
                        while (it.moveToNext() && !emitter.isDisposed) {
                            val colorPalette = ColorPalette(Date(it.getLong(0)), ColorList.CREATOR.unmarshall(it.getBlob(1)))
                            emitter.onNext(colorPalette)
                        }
                    }
                    if (!emitter.isDisposed) emitter.onComplete()
                }
    }

    override fun read(date: Date): Maybe<ColorPalette> {
        return Maybe
                .fromCallable {
                    val projection = arrayOf(ColorsContract.ColumnDate, ColorsContract.ColumnData)
                    val selection = "${ColorsContract.ColumnDate} = ?"
                    val selectionArgs = arrayOf(date.time.toString())
                    reader.query(ColorsContract.TableName, projection, selection, selectionArgs, null, null, null).use {
                        if (!it.moveToNext()) null else {
                            ColorPalette(Date(it.getLong(0)), ColorList.CREATOR.unmarshall(it.getBlob(1)))
                        }
                    }
                }
    }
}