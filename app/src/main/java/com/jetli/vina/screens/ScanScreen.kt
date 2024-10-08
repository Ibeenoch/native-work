package com.jetli.vina.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.jetli.vina.viewmodel.PaymentViewModel
import com.jetli.vina.viewmodel.QrCodedDecoded
//import com.journeyapps.barcodescanner.CameraPreview


fun parseQrCode(result: String): QrCodedDecoded {
    // Example of parsing the result string.
    // This will depend on how the result is structured (e.g., CSV, JSON, etc.)

    // For demonstration, assume result is a comma-separated string like:
    // "Merchant X,1234567890,Store Y,₦2,300"
    val parts = result.split(",")
    return QrCodedDecoded(
        Merchant_Name = parts[0],
        Account_Number = parts[1],
        Store_Name = parts[2],
        Amount = parts[3]
    )
}


@Composable
fun ScanScreen(
    navController: NavController,
    paymentViewModel: PaymentViewModel
){
    // State to store scanned QR code result
    var scannedResult by remember { mutableStateOf("") }

    // Get the current context
    val context = LocalContext.current

    // Permission state to track if the camera permission is granted
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Request permission launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Request permission if not granted
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }


    // If permission is granted, show the camera preview, otherwise show a message
    if (hasCameraPermission) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onQRCodeScanned = { result ->
                scannedResult = result // Update the scanned result
//
                // Assuming result is a raw string and needs to be parsed
                val qrCodeData: QrCodedDecoded = parseQrCode(result)

                // Update ViewModel with the decoded QR code details
                paymentViewModel.setQrCodeDetails(qrCodeData)

                // Navigate to transaction details screen
                navController.navigate("transactiondetails")
            }
        )
    } else {
        Text(text = "Camera permission is required to scan QR codes")
    }

    // Display the scanned QR code result QR Code Result: $scannedResult
    Text(text = "")

}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit // Define the type explicitly as a lambda that accepts a String
) {
    // Get the context and lifecycle owner (for managing CameraX lifecycle)
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner

    // Create the PreviewView (a camera preview UI element)
    val previewView = remember { PreviewView(context) }

    // Remember a coroutine scope
    val coroutineScope = rememberCoroutineScope()

    // LaunchedEffect to set up and bind CameraX lifecycle
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()

        // Preview use case for displaying the camera feeds
        val preview = Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        // Camera selector (we use the back camera)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Image analysis use case for QR code scanning
        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720)) // Set the resolution
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysisUseCase ->
                analysisUseCase.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    { imageProxy ->
                        processQRCodeImage(imageProxy, onQRCodeScanned) // Pass the lambda for QR code scanning
                    }
                )
            }

        // Bind the use cases to the lifecycle (camera lifecycle)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        } catch (exc: Exception) {
            Log.e("CameraPreview", "Camera binding failed", exc)
        }
    }

    // AndroidView for integrating PreviewView into Compose
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

// Function to process QR code from the camera feed
@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processQRCodeImage(
    imageProxy: ImageProxy,
    onQRCodeScanned: (String) -> Unit // Explicitly declare that the lambda takes a String
) {
    // Get the image from the image proxy
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Set barcode scanner options for QR codes only
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        // Get an instance of BarcodeScanner
        val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

        // Process the image for QR codes
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    // If a QR code is found, invoke the callback
                    barcode.rawValue?.let { qrCode ->
                        onQRCodeScanned(qrCode) // Pass the scanned QR code as a string
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QRCodeScanner", "QR Code scan failed", it)
            }
            .addOnCompleteListener {
                // Close the image proxy after processing
                imageProxy.close()
            }
    }
}
