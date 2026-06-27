package com.nuvio.app.features.player

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.datasource.DefaultDataSource

actual object PlayerAudioInjector {
    
    var activeExoPlayer: ExoPlayer? = null

    actual fun injectTrack(url: String) {
        val player = activeExoPlayer ?: return
        val currentMediaItem = player.currentMediaItem ?: return
        
        // CORREÇÃO DEFINITIVA: Pega o contexto global do aplicativo usando a thread principal do próprio player,
        // evitando chamadas estáticas do Media3 que o compilador KMP não consegue resolver.
        val context = android.app.ActivityThread.currentApplication()?.applicationContext 
            ?: throw IllegalStateException("Não foi possível obter o Contexto do Android")
            
        val dataSourceFactory = DefaultDataSource.Factory(context)

        val audioMediaItem = MediaItem.Builder().setUri(Uri.parse(url)).build()
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(audioMediaItem)

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(currentMediaItem)

        val mergedSource = MergingMediaSource(videoSource, audioSource)

        val currentPosition = player.currentPosition
        player.setMediaSource(mergedSource)
        player.seekTo(currentPosition)
        player.prepare()
        player.play()
    }
}
