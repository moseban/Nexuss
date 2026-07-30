package com.nexus.ui.scanner

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScannedData(
    val name: String = "",
    val amount: String = "",
    val date: String = "",
    val time: String = "",
    val products: List<String> = emptyList(),
    val category: String = ""
)

sealed class ScannerState {
    object Idle : ScannerState()
    object Loading : ScannerState()
    data class Success(val data: ScannedData) : ScannerState()
    data class Error(val message: String) : ScannerState()
}

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerState>(ScannerState.Idle)
    val uiState: StateFlow<ScannerState> = _uiState.asStateFlow()

    // Nota: Reemplazar con tu propia API KEY
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "TU_API_KEY_AQUI" 
    )

    fun processImage(bitmap: Bitmap) {
        _uiState.value = ScannerState.Loading
        viewModelScope.launch {
            try {
                val prompt = """
                    Analiza esta imagen de una factura o ticket.
                    Extrae la siguiente información en formato JSON:
                    - nombre (del establecimiento)
                    - monto (total gastado, solo el número)
                    - fecha (en formato dd/mm/aaaa)
                    - hora (en formato hh:mm)
                    - productos (lista de nombres de productos)
                    - categoria (asigna una categoría general como 'Comida', 'Transporte', 'Salud', 'Hogar' basándote en los productos)
                    
                    Si ves productos como leche, arroz, pan, etc., la categoría debe ser 'Comida'.
                """.trimIndent()

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)
                val text = response.text ?: ""
                
                // Aquí se debería parsear el JSON de la respuesta.
                // Por ahora, simulamos un éxito con datos extraídos (ejemplo).
                _uiState.value = ScannerState.Success(
                    ScannedData(
                        name = "Supermercado",
                        amount = "45.50",
                        date = "29/07/2026",
                        time = "14:30",
                        products = listOf("Leche", "Arroz"),
                        category = "Comida"
                    )
                )
            } catch (e: Exception) {
                _uiState.value = ScannerState.Error("Error al procesar la imagen: ${e.message}")
            }
        }
    }
}
