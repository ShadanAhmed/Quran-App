package com.example.quran.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quran.domain.model.Chapter
import com.example.quran.domain.model.TranslatedName
import com.example.quran.ui.theme.QuranTheme

@Composable
fun ChapterItem(
    chapter: Chapter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth(),
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .aspectRatio(1f)
                        .background(
                            color = if (isSystemInDarkTheme()) MaterialTheme.colors.background else LightGray
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chapter.id.toString(),
                        style = MaterialTheme.typography.body2,
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = chapter.name_simple,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = chapter.translated_name.name,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
            Text(text = chapter.name_arabic, style = MaterialTheme.typography.h5)
        }
    }
}

@Preview
@Composable
fun ChapterItemPreview() {
    QuranTheme {
        Surface(color = MaterialTheme.colors.background) {
            ChapterItem(chapter = Chapter(
                id = 1,
                "الفاتحة",
                "Al-Fatihah",
                TranslatedName(
                    "The Opener"
                )
            ))
        }
    }
}