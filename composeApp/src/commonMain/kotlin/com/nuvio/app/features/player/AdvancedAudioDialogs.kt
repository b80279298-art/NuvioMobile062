package com.nuviomedia.nuviomobile.features.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AdvancedAudioSettingsManager(
    onUrlSelected: (String) -> Unit
) {
    val currentStep = AudioMenuController.currentStep

    if (currentStep == AudioSettingsStep.CLOSED || currentStep == AudioSettingsStep.TRACKS) return

    Dialog(onDismissRequest = { AudioMenuController.currentStep = AudioSettingsStep.CLOSED }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            when (currentStep) {
                AudioSettingsStep.OPTIONS -> {
                    // --- IMAGEM 3 ---
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { AudioMenuController.currentStep = AudioSettingsStep.PICK_SOURCE },
                            modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444))
                        ) {
                            Text("Abrir áudio externo...", color = Color.White)
                        }
                        Button(
                            onClick = { /* Lógica de delay será conectada aqui */ },
                            modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444))
                        ) {
                            Text("Atraso de áudio", color = Color.White)
                        }
                    }
                }

                AudioSettingsStep.PICK_SOURCE -> {
                    // --- IMAGEM 4 ---
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("MEDIA LIBRARY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("Abrir áudio externo...", style = MaterialTheme.typography.titleMedium, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
                        
                        listOf("Pick file (legacy)", "Abrir URL", "Open document").forEach { opcao ->
                            Button(
                                onClick = { 
                                    if (opcao == "Abrir URL") AudioMenuController.currentStep = AudioSettingsStep.URL_INPUT
                                    // As outras opções abrem o seletor nativo (opcional)
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp).padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444))
                            ) {
                                Text(opcao, color = Color.White)
                            }
                        }
                    }
                }

                AudioSettingsStep.URL_INPUT -> {
                    // --- IMAGEM 5 ---
                    var urlText by remember { mutableStateOf("") }
                    Column {
                        Text("Abrir URL", style = MaterialTheme.typography.titleMedium, color = Color.White, modifier = Modifier.padding(bottom = 12.dp))
                        OutlinedTextField(
                            value = urlText,
                            onValueChange = { urlText = it },
                            label = { Text("Abrir URL") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { AudioMenuController.currentStep = AudioSettingsStep.PICK_SOURCE }) {
                                Text("Cancelar", color = Color.LightGray)
                            }
                            TextButton(onClick = {
                                if (urlText.isNotBlank()) {
                                    onUrlSelected(urlText)
                                    AudioMenuController.currentStep = AudioSettingsStep.CLOSED
                                }
                            }) {
                                Text("OK", color = Color.White)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
