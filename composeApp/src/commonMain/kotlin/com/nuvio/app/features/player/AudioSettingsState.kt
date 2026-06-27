package com.nuviomedia.nuviomobile.features.player // Ajuste o pacote se necessário

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AudioSettingsStep {
    CLOSED,      // Menu fechado
    TRACKS,      // Lista original de áudios (Imagem 2)
    OPTIONS,     // Opções: Abrir áudio externo / Atraso (Imagem 3)
    PICK_SOURCE, // Origem: Pick File / Abrir URL / Open document (Imagem 4)
    URL_INPUT    // Input da URL (Imagem 5)
}

// Objeto simples para controlar o estado global da navegação do menu
object AudioMenuController {
    var currentStep by mutableStateOf(AudioSettingsStep.CLOSED)
}
