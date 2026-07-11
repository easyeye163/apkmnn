package com.apk.claw.android

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apk.claw.android.local.diffusion.DiffusionEngine
import com.apk.claw.android.local.diffusion.DiffusionState
import com.apkmnn.diffusion.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MNNTest"
    }

    private lateinit var tvStatus: TextView
    private lateinit var engine: DiffusionEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tv_status)
        val btnCheck = findViewById<Button>(R.id.btn_check)
        val btnLoad = findViewById<Button>(R.id.btn_load)

        engine = DiffusionEngine.getInstance(this)

        // Observe state
        lifecycleScope.launch {
            engine.state.collect { state ->
                when (state) {
                    is DiffusionState.Uninitialized -> tvStatus.text = "State: Uninitialized"
                    is DiffusionState.Initializing -> tvStatus.text = "State: Initializing..."
                    is DiffusionState.NativeLoaded -> {
                        tvStatus.text = "State: Native lib loaded OK"
                        Log.i(TAG, "MNN native library loaded successfully")
                    }
                    is DiffusionState.NativeNotAvailable -> {
                        tvStatus.text = "State: Native NOT available\n${state.message}"
                        Log.w(TAG, "Native not available: ${state.message}")
                    }
                    is DiffusionState.LoadingModel -> tvStatus.text = "State: Loading model..."
                    is DiffusionState.Ready -> tvStatus.text = "State: Ready to generate"
                    is DiffusionState.Generating -> tvStatus.text = "State: Generating ${state.progress}%"
                    is DiffusionState.Error -> {
                        tvStatus.text = "State: Error\n${state.exception.message}"
                        Log.e(TAG, "Diffusion error", state.exception)
                    }
                }
            }
        }

        btnCheck.setOnClickListener {
            val info = buildString {
                append("App: apkmnn v0.0.1\n")
                append("ABI: ${android.os.Build.SUPPORTED_ABIS.joinToString()}\n")
                append("Device: ${android.os.Build.MODEL}\n")
                append("Native loaded: ${engine.isReady || engine.state.value is DiffusionState.NativeLoaded}")
            }
            tvStatus.text = info
            Log.i(TAG, info)
        }

        btnLoad.setOnClickListener {
            Toast.makeText(this, "Set model path in code first", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Load model button pressed - configure model path in source")
        }
    }
}