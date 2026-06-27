package com.nuvio.app.features.player

// Define os estados que o menu de áudio pode ter
enum class AudioSettingsStep {
    OPTIONS,
    DEFAULT
}

// Cria o objeto que gerencia qual passo do menu está ativo
object AudioMenuController {
    var currentStep: AudioSettingsStep = AudioSettingsStep.DEFAULT
}
