package renetik.android.framework.consumer.smoke

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import renetik.android.core.android.content.dpToPixel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val padding = dpToPixel(24)
        setContentView(LinearLayout(this).apply {
            orientation = VERTICAL
            gravity = CENTER
            setPadding(padding, padding, padding, padding)
            setBackgroundColor(Color.BLUE)
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            addView(TextView(context).apply {
                text = "Renetik Framework Smoke Test"
                textSize = 28f
                setTextColor(Color.WHITE)
                gravity = CENTER
            })

            addView(TextView(context).apply {
                text = "Opened successfully"
                textSize = 16f
                setTextColor(Color.rgb(190, 230, 255))
                gravity = CENTER
            })
        })
    }
}
