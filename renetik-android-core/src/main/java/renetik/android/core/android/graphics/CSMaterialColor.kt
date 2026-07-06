package renetik.android.core.android.graphics

import android.graphics.Color.parseColor

data class CSMaterialColor(val title: String, val hex: String) {
    val colorInt: Int = parseColor(hex)

    companion object {
        val material50 = listOf(
            CSMaterialColor("Red 50", "#FFEBEE"),
            CSMaterialColor("Pink 50", "#FCE4EC"),
            CSMaterialColor("Purple 50", "#F3E5F5"),
            CSMaterialColor("Deep Purple 50", "#EDE7F6"),
            CSMaterialColor("Indigo 50", "#E8EAF6"),
            CSMaterialColor("Blue 50", "#E3F2FD"),
            CSMaterialColor("Light Blue 50", "#E1F5FE"),
            CSMaterialColor("Cyan 50", "#E0F7FA"),
            CSMaterialColor("Teal 50", "#E0F2F1"),
            CSMaterialColor("Green 50", "#E8F5E9"),
            CSMaterialColor("Light Green 50", "#F1F8E9"),
            CSMaterialColor("Lime 50", "#F9FBE7"),
            CSMaterialColor("Yellow 50", "#FFFDE7"),
            CSMaterialColor("Amber 50", "#FFF8E1"),
            CSMaterialColor("Orange 50", "#FFF3E0"),
            CSMaterialColor("Deep Orange 50", "#FBE9E7"),
            CSMaterialColor("Brown 50", "#EFEBE9"),
            CSMaterialColor("Grey 50", "#FAFAFA"),
            CSMaterialColor("Blue Grey 50", "#ECEFF1")
        )

        val material100 = listOf(
            CSMaterialColor("Red 100", "#FF8A80"),
            CSMaterialColor("Pink 100", "#FF80AB"),
            CSMaterialColor("Purple 100", "#E1BEE7"),
            CSMaterialColor("Deep Purple 100", "#D1C4E9"),
            CSMaterialColor("Indigo 100", "#C5CAE9"),
            CSMaterialColor("Blue 100", "#BBDEFB"),
            CSMaterialColor("Light Blue 100", "#B3E5FC"),
            CSMaterialColor("Cyan 100", "#B2EBF2"),
            CSMaterialColor("Teal 100", "#B9FBC0"),
            CSMaterialColor("Green 100", "#C5E1A5"),
            CSMaterialColor("Light Green 100", "#DCE775"),
            CSMaterialColor("Lime 100", "#F4FF81"),
            CSMaterialColor("Yellow 100", "#FFFF8D"),
            CSMaterialColor("Amber 100", "#FFE57F"),
            CSMaterialColor("Orange 100", "#FFD180"),
            CSMaterialColor("Deep Orange 100", "#FFAB91"),
            CSMaterialColor("Brown 100", "#D7CCC8"),
            CSMaterialColor("Grey 100", "#F5F5F5"),
            CSMaterialColor("Blue Grey 100", "#CFD8DC")
        )

        val material200 = listOf(
            CSMaterialColor("Red 200", "#FF5252"),
            CSMaterialColor("Pink 200", "#FF4081"),
            CSMaterialColor("Purple 200", "#CE93D8"),
            CSMaterialColor("Deep Purple 200", "#B39DDB"),
            CSMaterialColor("Indigo 200", "#9FA8DA"),
            CSMaterialColor("Blue 200", "#90CAF9"),
            CSMaterialColor("Light Blue 200", "#81D4FA"),
            CSMaterialColor("Cyan 200", "#80DEEA"),
            CSMaterialColor("Teal 200", "#80E2B1"),
            CSMaterialColor("Green 200", "#AED581"),
            CSMaterialColor("Light Green 200", "#C0CA33"),
            CSMaterialColor("Lime 200", "#E1F5FE"),
            CSMaterialColor("Yellow 200", "#E2F5B5"),
            CSMaterialColor("Amber 200", "#FFEB3B"),
            CSMaterialColor("Orange 200", "#FFAB40"),
            CSMaterialColor("Deep Orange 200", "#FF6E40"),
            CSMaterialColor("Brown 200", "#BCAAA4"),
            CSMaterialColor("Grey 200", "#EEEEEE"),
            CSMaterialColor("Blue Grey 200", "#B0BEC5")
        )

        val material300 = listOf(
            CSMaterialColor("Red 300", "#E57373"),
            CSMaterialColor("Pink 300", "#F06292"),
            CSMaterialColor("Purple 300", "#BA68C8"),
            CSMaterialColor("Deep Purple 300", "#9575CD"),
            CSMaterialColor("Indigo 300", "#7986CB"),
            CSMaterialColor("Blue 300", "#64B5F6"),
            CSMaterialColor("Light Blue 300", "#4FC3F7"),
            CSMaterialColor("Cyan 300", "#26C6DA"),
            CSMaterialColor("Teal 300", "#4DB6AC"),
            CSMaterialColor("Green 300", "#81C784"),
            CSMaterialColor("Light Green 300", "#9CCC65"),
            CSMaterialColor("Lime 300", "#DCE775"),
            CSMaterialColor("Yellow 300", "#F0F4C3"),
            CSMaterialColor("Amber 300", "#FFE082"),
            CSMaterialColor("Orange 300", "#FFB74D"),
            CSMaterialColor("Deep Orange 300", "#FF8A65"),
            CSMaterialColor("Brown 300", "#A1887F"),
            CSMaterialColor("Grey 300", "#E0E0E0"),
            CSMaterialColor("Blue Grey 300", "#90A4AE")
        )

        val material400 = listOf(
            CSMaterialColor("Red 400", "#F44336"),
            CSMaterialColor("Pink 400", "#E91E63"),
            CSMaterialColor("Purple 400", "#9C27B0"),
            CSMaterialColor("Deep Purple 400", "#673AB7"),
            CSMaterialColor("Indigo 400", "#3F51B5"),
            CSMaterialColor("Blue 400", "#2196F3"),
            CSMaterialColor("Light Blue 400", "#03A9F4"),
            CSMaterialColor("Cyan 400", "#00BCD4"),
            CSMaterialColor("Teal 400", "#009688"),
            CSMaterialColor("Green 400", "#4CAF50"),
            CSMaterialColor("Light Green 400", "#8BC34A"),
            CSMaterialColor("Lime 400", "#CDDC39"),
            CSMaterialColor("Yellow 400", "#FFEB3B"),
            CSMaterialColor("Amber 400", "#FFC107"),
            CSMaterialColor("Orange 400", "#FF9800"),
            CSMaterialColor("Deep Orange 400", "#FF5722"),
            CSMaterialColor("Brown 400", "#6D4C41"),
            CSMaterialColor("Grey 400", "#BDBDBD"),
            CSMaterialColor("Blue Grey 400", "#607D8B")
        )

        val material500 = listOf(
            CSMaterialColor("Red 500", "#F44336"),
            CSMaterialColor("Pink 500", "#E91E63"),
            CSMaterialColor("Purple 500", "#9C27B0"),
            CSMaterialColor("Deep Purple 500", "#673AB7"),
            CSMaterialColor("Indigo 500", "#3F51B5"),
            CSMaterialColor("Blue 500", "#2196F3"),
            CSMaterialColor("Light Blue 500", "#03A9F4"),
            CSMaterialColor("Cyan 500", "#00BCD4"),
            CSMaterialColor("Teal 500", "#009688"),
            CSMaterialColor("Green 500", "#4CAF50"),
            CSMaterialColor("Light Green 500", "#8BC34A"),
            CSMaterialColor("Lime 500", "#CDDC39"),
            CSMaterialColor("Yellow 500", "#FFEB3B"),
            CSMaterialColor("Amber 500", "#FFC107"),
            CSMaterialColor("Orange 500", "#FF9800"),
            CSMaterialColor("Deep Orange 500", "#FF5722"),
            CSMaterialColor("Brown 500", "#795548"),
            CSMaterialColor("Grey 500", "#9E9E9E"),
            CSMaterialColor("Blue Grey 500", "#607D8B")
        )

        val material600 = listOf(
            CSMaterialColor("Red 600", "#E53935"),
            CSMaterialColor("Pink 600", "#D81B60"),
            CSMaterialColor("Purple 600", "#8E24AA"),
            CSMaterialColor("Deep Purple 600", "#5E35B1"),
            CSMaterialColor("Indigo 600", "#3949AB"),
            CSMaterialColor("Blue 600", "#1E88E5"),
            CSMaterialColor("Light Blue 600", "#039BE5"),
            CSMaterialColor("Cyan 600", "#00ACC1"),
            CSMaterialColor("Teal 600", "#00897B"),
            CSMaterialColor("Green 600", "#43A047"),
            CSMaterialColor("Light Green 600", "#7CB342"),
            CSMaterialColor("Lime 600", "#C0CA33"),
            CSMaterialColor("Yellow 600", "#FBC02D"),
            CSMaterialColor("Amber 600", "#FFB300"),
            CSMaterialColor("Orange 600", "#FB8C00"),
            CSMaterialColor("Deep Orange 600", "#F57C00"),
            CSMaterialColor("Brown 600", "#6D4C41"),
            CSMaterialColor("Grey 600", "#757575"),
            CSMaterialColor("Blue Grey 600", "#455A64")
        )

        val material700 = listOf(
            CSMaterialColor("Red 700", "#D32F2F"),
            CSMaterialColor("Pink 700", "#C2185B"),
            CSMaterialColor("Purple 700", "#7B1FA2"),
            CSMaterialColor("Deep Purple 700", "#512DA8"),
            CSMaterialColor("Indigo 700", "#303F9F"),
            CSMaterialColor("Blue 700", "#1976D2"),
            CSMaterialColor("Light Blue 700", "#0288D1"),
            CSMaterialColor("Cyan 700", "#0097A7"),
            CSMaterialColor("Teal 700", "#00796B"),
            CSMaterialColor("Green 700", "#388E3C"),
            CSMaterialColor("Light Green 700", "#689F38"),
            CSMaterialColor("Lime 700", "#AFB42B"),
            CSMaterialColor("Yellow 700", "#F9A825"),
            CSMaterialColor("Amber 700", "#FF8F00"),
            CSMaterialColor("Orange 700", "#F57C00"),
            CSMaterialColor("Deep Orange 700", "#E64A19"),
            CSMaterialColor("Brown 700", "#5D4037"),
            CSMaterialColor("Grey 700", "#616161"),
            CSMaterialColor("Blue Grey 700", "#37474F")
        )

        val material800 = listOf(
            CSMaterialColor("Red 800", "#C62828"),
            CSMaterialColor("Pink 800", "#AD1457"),
            CSMaterialColor("Purple 800", "#6A1B9A"),
            CSMaterialColor("Deep Purple 800", "#4527A0"),
            CSMaterialColor("Indigo 800", "#283593"),
            CSMaterialColor("Blue 800", "#1565C0"),
            CSMaterialColor("Light Blue 800", "#0277BD"),
            CSMaterialColor("Cyan 800", "#00838F"),
            CSMaterialColor("Teal 800", "#00695C"),
            CSMaterialColor("Green 800", "#2C6B2F"),
            CSMaterialColor("Light Green 800", "#558B2F"),
            CSMaterialColor("Lime 800", "#9E9D24"),
            CSMaterialColor("Yellow 800", "#F57F17"),
            CSMaterialColor("Amber 800", "#FF6F00"),
            CSMaterialColor("Orange 800", "#EF6C00"),
            CSMaterialColor("Deep Orange 800", "#DD5600"),
            CSMaterialColor("Brown 800", "#4E342E"),
            CSMaterialColor("Grey 800", "#424242"),
            CSMaterialColor("Blue Grey 800", "#263238")
        )

        val material900 = listOf(
            CSMaterialColor("Red 900", "#B71C1C"),
            CSMaterialColor("Pink 900", "#880E4F"),
            CSMaterialColor("Purple 900", "#4A148C"),
            CSMaterialColor("Deep Purple 900", "#311B92"),
            CSMaterialColor("Indigo 900", "#1A237E"),
            CSMaterialColor("Blue 900", "#0D47A1"),
            CSMaterialColor("Light Blue 900", "#01579B"),
            CSMaterialColor("Cyan 900", "#006064"),
            CSMaterialColor("Teal 900", "#004D40"),
            CSMaterialColor("Green 900", "#003D33"),
            CSMaterialColor("Light Green 900", "#33691E"),
            CSMaterialColor("Lime 900", "#827717"),
            CSMaterialColor("Yellow 900", "#F57F17"),
            CSMaterialColor("Amber 900", "#FF6F00"),
            CSMaterialColor("Orange 900", "#E65100"),
            CSMaterialColor("Deep Orange 900", "#BF360C"),
            CSMaterialColor("Brown 900", "#3E2723"),
            CSMaterialColor("Grey 900", "#212121"),
            CSMaterialColor("Blue Grey 900", "#102027")
        )

        val all = material100 + material200 + material300 + material400 +
                material500 + material600 + material700 + material800 + material900
    }
}