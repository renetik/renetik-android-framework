package renetik.android.core.lang

interface CSHasId {
	companion object

	val id: String
}

fun CSHasId(id: String): CSHasId = object : CSHasId {
	override val id = id
}