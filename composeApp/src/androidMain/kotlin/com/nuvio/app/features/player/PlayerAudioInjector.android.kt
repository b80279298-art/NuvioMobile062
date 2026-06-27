package com.nuvio.app.features.player

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.datasource.DefaultDataSource

actual object PlayerAudioInjector {
    
    // Guarda a instância ativa do ExoPlayer que o Nuvio está a usar
    var activeExoPlayer: ExoPlayer? = null

    actual fun injectTrack(url: String) {
        val player = activeExoPlayer ?: return
        val currentMediaItem = player.currentMediaItem ?: return
        
        // Cria a fábrica de dados usando o contexto do próprio player
        val dataSourceFactory = DefaultDataSource.Factory(player.context)

        // 1. Cria a nova fonte de áudio externo a partir da URL digitada pelo utilizador
        val audioMediaItem = MediaItem.Builder().setUri(Uri.parse(url)).build()
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(audioMediaItem)

        // 2. Cria a fonte do vídeo que já estava em reprodução
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(currentMediaItem)

        // 3. Junta o vídeo original com o áudio externo novo
        val mergedSource = MergingMediaSource(videoSource, audioSource)

        // 4. Recarrega o player mantendo exatamente a mesma posição (tempo) onde o utilizador estava
        val currentPosition = player.currentPosition
        player.setMediaSource(mergedSource)
        player.seekTo(currentPosition)
        player.prepare()
        player.play()
    }
}
