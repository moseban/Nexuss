package com.nexus.ui.scanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.nexus.ui.theme.BackgroundDark
import com.nexus.ui.theme.PrimaryGradient
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        when (uiState) {
            is ScannerState.Idle -> {
                CameraPreview(
                    onImageCaptureCreated = { imageCapture = it }
                )
                
                // Capture Button Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconButton(
                        onClick = {
                            captureImage(imageCapture, context, cameraExecutor) { bitmap ->
                                viewModel.processImage(bitmap)
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(PrimaryGradient)
                    ) {
                        Icon(Icons.Default.Camera, contentDescription = "Capturar", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                }
            }
            is ScannerState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
            is ScannerState.Success -> {
                val data = (uiState as ScannerState.Success).data
                ScannedDataDialog(data, onConfirm = onNavigateBack, onDismiss = onNavigateBack)
            }
            is ScannerState.Error -> {
                Text(
                    text = (uiState as ScannerState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }
        }

        // Close Button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
        }
    }
}

@Composable
fun CameraPreview(
    onImageCaptureCreated: (ImageCapture) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                onImageCaptureCreated(imageCapture)

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Log.e("ScannerScreen", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun captureImage(
    imageCapture: ImageCapture?,
    context: Context,
    executor: Executor,
    onImageCaptured: (Bitmap) -> Unit
) {
    val capture = imageCapture ?: return

    capture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            image.close()
            onImageCaptured(bitmap)
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("ScannerScreen", "Photo capture failed: ${exception.message}", exception)
        }
    })
}

@Composable
fun ScannedDataDialog(
    data: ScannedData,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Datos Extraídos") },
        text = {
            Column {
                Text("Comercio: ${data.name}")
                Text("Monto: €${data.amount}")
                Text("Categoría: ${data.category}")
                Text("Productos: ${data.products.joinToString(", ")}")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirmar") }
        }
    )
}
