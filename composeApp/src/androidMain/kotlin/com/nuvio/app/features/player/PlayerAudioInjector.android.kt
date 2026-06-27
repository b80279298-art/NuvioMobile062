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
        
        // CORREÇÃO: Garante o contexto do Android isolando a chamada numa variável local tipada
        val androidContext: android.content.Context = player.applicationLooper.thread.let { 
            player.currentMediaItem?.localConfiguration?.uri?.let { null }
            // O próprio ExoPlayer estende a interface Player, e a instância de execução sempre expõe o contexto
            // Forçamos o cast seguro usando o contexto interno do Media3/ExoPlayer de forma direta
            (player as? androidx.media3.exoplayer.ExoPlayer)?.applicationLooper?.thread?.let { null }
            // Usamos a referência limpa do próprio ecossistema do player ativo
            androidx.media3.common.util.Util.getApplicationContext()
        }
        
        val dataSourceFactory = DefaultDataSource.Factory(androidContext)

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

