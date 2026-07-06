package renetik.android.preset

//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(RobolectricTestRunner::class)
//class LoadChildChildTest {
//    @Before fun setUp() {
//        CSJson.isJsonPretty = true
//    }
//
//    val barItemJson = """{
//    "childChildPreset preset current": "second childChildPreset item"
//  }"""
//
//    val keyItemJson = """{
//      "key property": 10
//    }"""
//
//    val initialJson = """{
//  "bar1Preset preset current": "clear bar item",
//  "bar1Preset preset store": {
//    "key preset current": "clear key item",
//    "key preset store": {}
//  },
//  "bar2Preset preset current": "clear bar item",
//  "bar2Preset preset store": {
//    "key preset current": "clear key item",
//    "key preset store": {}
//  }
//}"""
//    val clearChildSecondChildChildStoreJson = """{
//  "childPreset1 preset current": "clear childPreset item",
//  "childPreset1 preset store": {
//    "childChildPreset1 preset current": "second childChildPreset item",
//    "childChildPreset1 preset store": {
//      "childChildPresetProperty": 10
//    }
//  },
//  "childPreset2 preset current": "clear childPreset item",
//  "childPreset2 preset store": {
//    "childChildPreset2 preset current": "second childChildPreset item",
//    "childChildPreset2 preset store": {
//      "childChildPresetProperty": 10
//    }
//  }
//}"""
//
//    val secondChildStoreJson = """{
//  "childPreset preset current": "second childPreset item",
//  "childPreset1 preset store": {
//    "childChildPreset1 preset current": "second childChildPreset item",
//    "childChildPreset1 preset store": {
//      "childChildPresetProperty": 10
//    }
//  },
//  "childPreset2 preset store": {
//    "childChildPreset2 preset current": "second childChildPreset item",
//    "childChildPreset2 preset store": {
//      "childChildPresetProperty": 10
//    }
//  }
//}"""
//
//    val preset = CSPreset(
//        CSModel(), CSJsonObjectStore(), "preset",
//        TestCSPresetItemList(firstItemId = "clear preset item"),
//        ::NotFoundPresetItem
//    ).manageItems().init()
//
//    val childPresetList = TestCSPresetItemList(firstItemId = "clear childPreset item")
//    val secondChildPresetItem = childPresetList.createItem(
//        "second childPreset item", category = defaultCategory)
//        .apply { store.reload(secondChildPresetItemJson) }
//    val childPreset1 = CSPreset(
//        CSModel(), preset, "childPreset1",
//        childPresetList, ::NotFoundPresetItem,
//    ).manageItems().init()
//    val childPreset2 = CSPreset(
//        CSModel(), preset, "childPreset2",
//        childPresetList, ::NotFoundPresetItem,
//    ).manageItems().init()
//
//    val childChildPresetList = TestCSPresetItemList(
//        firstItemId = "clear childChildPreset item")
//    val secondChildChildPresetItem = childChildPresetList.createItem(
//        "second childChildPreset item", category = defaultCategory)
//        .apply { store.reload(secondChildChildPresetItemJson) }
//    val childChildPreset1 = CSPreset(
//        CSModel(), childPreset1, "childChildPreset1",
//        childChildPresetList, ::NotFoundPresetItem,
//    ).manageItems().init()
//    val childChildPreset1Property = childChildPreset1.property(CSModel(),
//        "childChildPresetProperty", 5)
//    val childChildPreset2 = CSPreset(
//        CSModel(), childPreset2, "childChildPreset2",
//        childChildPresetList, ::NotFoundPresetItem,
//    ).manageItems().init()
//    val childChildPreset2Property = childChildPreset2.property(CSModel(),
//        "childChildPresetProperty", 5)
//
//    @Before fun setUp2() {
//        assertEquals(5, childChildPreset1Property.value)
//        assertEquals(5, childChildPreset2Property.value)
//        assertEquals(preset.store.data.toJson(), initialStoreJson)
//    }
//
//    @Test
//    fun loadSecondChildChildPresetListItem() {
//        childChildPreset1.listItem assign secondChildChildPresetItem
//        childChildPreset2.listItem assign secondChildChildPresetItem
//        assertEquals(10, childChildPreset1Property.value)
//        assertEquals(10, childChildPreset2Property.value)
//        assertEquals(preset.store.data.toJson(), clearChildSecondChildChildStoreJson)
//        childPreset1.reload()
//        childPreset2.reload()
//        assertEquals(preset.store.data.toJson(), initialStoreJson)
//        assertEquals(5, childChildPreset1Property.value)
//        childChildPreset1.listItem assign secondChildChildPresetItem
//        childChildPreset2.listItem assign secondChildChildPresetItem
//        assertEquals(preset.store.data.toJson(), clearChildSecondChildChildStoreJson)
//        assertEquals(10, childChildPreset1Property.value)
//        assertEquals(10, childChildPreset2Property.value)
//        preset.reload()
//        assertEquals(preset.store.data.toJson(), initialStoreJson)
//        assertEquals(5, childChildPreset1Property.value)
//        assertEquals(5, childChildPreset2Property.value)
//    }
//
////    @Test
////    fun loadSecondChildPresetListItem() {
////        childPreset1.listItem assign secondChildPresetItem
////        childPreset2.listItem assign secondChildPresetItem
////        assertEquals(preset.store.data.toJson(), secondChildStoreJson)
////        assertEquals(10, childChildPreset1Property.value)
////    }
//}
