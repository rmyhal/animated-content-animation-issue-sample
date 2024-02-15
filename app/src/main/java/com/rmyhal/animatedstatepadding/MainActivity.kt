package com.rmyhal.animatedstatepadding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmyhal.animatedstatepadding.ui.theme.AnimatedStatePaddingTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			AnimatedStatePaddingTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(innerPadding),
					) {
						Content(
							modifier = Modifier
								.align(Alignment.Center)
						)
					}
				}
			}
		}
	}
}

@Composable
fun Content(modifier: Modifier = Modifier) {
	var expanded by remember { mutableStateOf(false) }
	val transition = updateTransition(targetState = expanded, label = "expanding")

	val padding by transition.animateDp(
		label = "padding",
	) { exp ->
		if (exp) 16.dp else 0.dp
	}
	transition.AnimatedContent(
		modifier = modifier
			// comment out to see correct animation
			.padding(padding)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) {
				expanded = !expanded
			}
			.drawBehind { drawRect(Color.Gray) },
		transitionSpec = {
			(fadeIn(animationSpec = tween(220, delayMillis = 90)) +
				scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
				.togetherWith(fadeOut(animationSpec = tween(90)))
				.using(
					SizeTransform(
						clip = false,
						sizeAnimationSpec = { _, _ ->
							tween(
								durationMillis = 1500, // setting it long to make animation difference more visible
								easing = FastOutSlowInEasing
							)
						}
					)
				)
		}
	) { exp ->
		if (exp) {
			Box(
				modifier = Modifier
					.height(300.dp)
					.fillMaxWidth()
			)
		} else {
			Box(
				modifier = Modifier
					.size(150.dp)
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	AnimatedStatePaddingTheme {
		Box(modifier = Modifier.fillMaxSize()) {
			Content(
				modifier = Modifier.align(Alignment.Center)
			)
		}
	}
}