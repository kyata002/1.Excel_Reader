package com.documentmaster.documentscan

interface OnActionCallback {
    fun callback(key: String?, vararg data: Any?)
}