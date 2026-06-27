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
        
        // CORREÇÃO LIMPA: Acessa o contexto diretamente através do applicationLooper do player
        val context = player.applicationLooper.thread.run { 
            androidx.media3.common.util.Util.getApplicationContext() 
        }
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
