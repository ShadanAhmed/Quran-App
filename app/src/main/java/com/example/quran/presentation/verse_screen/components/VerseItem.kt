package com.example.quran.presentation.verse_screen.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.example.quran.domain.model.Verse
import com.example.quran.presentation.util.Constants
import com.example.quran.ui.theme.Blue
import com.example.quran.ui.theme.BlueDark
import com.example.quran.ui.theme.BlueLight

@Composable
fun VerseItem(
    verse: Verse,
    modifier: Modifier = Modifier,
    currentPlayingItemIndex: Int,
    onPlayButtonClicked: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .padding(4.dp)
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth(),
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.background),
        elevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colors.background)
                            .padding(vertical = 1.dp, horizontal = 3.dp)
                    ) {
                        Text(text = verse.verse_key, style = MaterialTheme.typography.body1)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSystemInDarkTheme()) BlueLight else Blue)
                            .padding(1.dp)
                            .clickable {
                                onPlayButtonClicked()
                            }
                    ) {
                        Icon(
                            imageVector = if (currentPlayingItemIndex + 1 == verse.id) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Play Verse",
                            tint = MaterialTheme.colors.background,
                        )
                    }
                }

                Text(
                    text = verse.text_uthmani,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxWidth(.9f),
                    textAlign = TextAlign.End,
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Column(modifier = Modifier.fillMaxWidth(.99f)) {
                for (translation in verse.translations) {
                    val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        android.text.Html.fromHtml(
                            translation.text,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString()
                    } else {
                        android.text.Html.fromHtml(
                            translation.text
                        ).toString()
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        style = MaterialTheme.typography.h6,
                        text = text,
                        color = MaterialTheme.colors.secondary
                    )
                    val translatedBy = if (translation.resource_id == 131) {
                        Constants.firstTranslatorName
                    } else {
                        Constants.secondTranslatorName
                    }
                    Text(
                        text = "- $translatedBy",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSystemInDarkTheme()) BlueLight else BlueDark
                    )
                }
            }
        }
    }
}