package com.asadmshah.livecolorpicker.database

internal object ColorsContract {

    val TableName = "colors"

    val ColumnDate = "date"
    val ColumnData = "data"

    val QueryCreateTable = """CREATE TABLE $TableName ($ColumnDate INTEGER PRIMARY KEY, $ColumnData BLOB NOT NULL);"""
    val QueryDropTable = "DROP TABLE $TableName IF EXISTS"

}