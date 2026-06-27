package com.nuvio.app.features.player

// Este objeto avisa o código comum que a plataforma (Android) vai implementar a função
expect object PlayerAudioInjector {
    fun injectTrack(url: String)
}

