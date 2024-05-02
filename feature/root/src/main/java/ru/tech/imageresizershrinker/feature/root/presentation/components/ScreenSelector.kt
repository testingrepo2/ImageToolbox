/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package ru.tech.imageresizershrinker.feature.root.presentation.components

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.t8rin.dynamic.theme.LocalDynamicThemeState
import com.t8rin.dynamic.theme.rememberAppColorTuple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.tech.imageresizershrinker.core.settings.presentation.provider.LocalSettingsState
import ru.tech.imageresizershrinker.core.ui.utils.animation.FancyTransitionEasing
import ru.tech.imageresizershrinker.core.ui.utils.navigation.LocalNavController
import ru.tech.imageresizershrinker.core.ui.utils.navigation.Screen
import ru.tech.imageresizershrinker.core.ui.utils.navigation.backstack
import ru.tech.imageresizershrinker.core.ui.utils.navigation.pop
import ru.tech.imageresizershrinker.feature.apng_tools.presentation.ApngToolsScreen
import ru.tech.imageresizershrinker.feature.bytes_resize.presentation.BytesResizeScreen
import ru.tech.imageresizershrinker.feature.cipher.presentation.FileCipherScreen
import ru.tech.imageresizershrinker.feature.compare.presentation.CompareScreen
import ru.tech.imageresizershrinker.feature.convert.presentation.ConvertScreen
import ru.tech.imageresizershrinker.feature.crop.presentation.CropScreen
import ru.tech.imageresizershrinker.feature.delete_exif.presentation.DeleteExifScreen
import ru.tech.imageresizershrinker.feature.draw.presentation.DrawScreen
import ru.tech.imageresizershrinker.feature.easter_egg.presentation.EasterEggScreen
import ru.tech.imageresizershrinker.feature.erase_background.presentation.EraseBackgroundScreen
import ru.tech.imageresizershrinker.feature.filters.presentation.FiltersScreen
import ru.tech.imageresizershrinker.feature.generate_palette.presentation.GeneratePaletteScreen
import ru.tech.imageresizershrinker.feature.gif_tools.presentation.GifToolsScreen
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.GradientMakerScreen
import ru.tech.imageresizershrinker.feature.image_preview.presentation.ImagePreviewScreen
import ru.tech.imageresizershrinker.feature.image_stitch.presentation.ImageStitchingScreen
import ru.tech.imageresizershrinker.feature.jxl_tools.presentation.JxlToolsScreen
import ru.tech.imageresizershrinker.feature.limits_resize.presentation.LimitsResizeScreen
import ru.tech.imageresizershrinker.feature.load_net_image.presentation.LoadNetImageScreen
import ru.tech.imageresizershrinker.feature.main.presentation.MainScreen
import ru.tech.imageresizershrinker.feature.pdf_tools.presentation.PdfToolsScreen
import ru.tech.imageresizershrinker.feature.pick_color.presentation.PickColorFromImageScreen
import ru.tech.imageresizershrinker.feature.recognize.text.presentation.RecognizeTextScreen
import ru.tech.imageresizershrinker.feature.resize_convert.presentation.ResizeAndConvertScreen
import ru.tech.imageresizershrinker.feature.root.presentation.viewModel.RootViewModel
import ru.tech.imageresizershrinker.feature.settings.presentation.SettingsScreen
import ru.tech.imageresizershrinker.feature.single_edit.presentation.SingleEditScreen
import ru.tech.imageresizershrinker.feature.svg.presentation.SvgScreen
import ru.tech.imageresizershrinker.feature.watermarking.presentation.WatermarkingScreen
import ru.tech.imageresizershrinker.feature.zip.presentation.ZipScreen
import kotlin.reflect.typeOf

@Composable
internal fun ScreenSelector(
    viewModel: RootViewModel
) {
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val settingsState = LocalSettingsState.current
    val themeState = LocalDynamicThemeState.current
    val appColorTuple = rememberAppColorTuple(
        defaultColorTuple = settingsState.appColorTuple,
        dynamicColor = settingsState.isDynamicColors,
        darkTheme = settingsState.isNightMode
    )
    val backstack = navController.backstack
    val onGoBack: () -> Unit = {
        viewModel.updateUris(null)
        navController.apply {
            if (backstack.size > 1) pop()
        }
        scope.launch {
            delay(350L) //delay for  it.toRoute<Screen.SingleEdit>() anim
            themeState.updateColorTuple(appColorTuple)
        }
    }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    NavHost(
        navController = navController,
        startDestination = Screen.Main,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(600, easing = FancyTransitionEasing),
                initialOffsetX = { screenWidthDp }) + fadeIn(
                tween(300, 100)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(600, easing = FancyTransitionEasing),
                targetOffsetX = { -screenWidthDp }) + fadeOut(
                tween(300, 100)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(600, easing = FancyTransitionEasing),
                initialOffsetX = { -screenWidthDp }) + fadeIn(
                tween(300, 100)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(600, easing = FancyTransitionEasing),
                targetOffsetX = { screenWidthDp }) + fadeOut(
                tween(300, 100)
            )
        }
    ) {
        composable<Screen.Settings> {
            SettingsScreen(
                onTryGetUpdate = viewModel::tryGetUpdate,
                updateAvailable = viewModel.updateAvailable,
                onGoBack = onGoBack
            )
        }
        composable<Screen.EasterEgg> {
            EasterEggScreen(onGoBack = onGoBack)
        }


        composable<Screen.Main> {
            MainScreen(
                onTryGetUpdate = viewModel::tryGetUpdate,
                updateAvailable = viewModel.updateAvailable,
                updateUris = viewModel::updateUris
            )
        }

        composable<Screen.SingleEdit> {
            SingleEditScreen(
                uriState = it.toRoute<Screen.SingleEdit>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.ResizeAndConvert> {
            ResizeAndConvertScreen(
                uriState = it.toRoute<Screen.ResizeAndConvert>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.DeleteExif> {
            DeleteExifScreen(
                uriState = it.toRoute<Screen.DeleteExif>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.ResizeByBytes> {
            BytesResizeScreen(
                uriState = it.toRoute<Screen.ResizeByBytes>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Crop> {
            CropScreen(
                uriState = it.toRoute<Screen.Crop>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.PickColorFromImage> {
            PickColorFromImageScreen(
                uriState = it.toRoute<Screen.PickColorFromImage>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.ImagePreview> {
            ImagePreviewScreen(
                uriState = it.toRoute<Screen.ImagePreview>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.GeneratePalette> {
            GeneratePaletteScreen(
                uriState = it.toRoute<Screen.GeneratePalette>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Compare> { backStackEntry ->
            CompareScreen(
                comparableUris = backStackEntry.toRoute<Screen.Compare>().uris
                    ?.takeIf { it.size == 2 }
                    ?.let { it[0] to it[1] },
                onGoBack = onGoBack
            )
        }

        composable<Screen.LoadNetImage> {
            LoadNetImageScreen(
                url = it.toRoute<Screen.LoadNetImage>().url,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Filter>(
            typeMap = mapOf(typeOf<Screen.Filter.Type?>() to FilterNavType)
        ) {
            FiltersScreen(
                type = it.toRoute<Screen.Filter>().type,
                onGoBack = onGoBack
            )
        }

        composable<Screen.LimitResize> {
            LimitsResizeScreen(
                uriState = it.toRoute<Screen.LimitResize>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Draw> {
            DrawScreen(
                uriState = it.toRoute<Screen.Draw>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Cipher> {
            FileCipherScreen(
                uriState = it.toRoute<Screen.Cipher>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.EraseBackground> {
            EraseBackgroundScreen(
                uriState = it.toRoute<Screen.EraseBackground>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.ImageStitching> {
            ImageStitchingScreen(
                uriState = it.toRoute<Screen.ImageStitching>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.PdfTools>(
            typeMap = mapOf(typeOf<Screen.PdfTools.Type?>() to PdfToolsNavType)
        ) {
            PdfToolsScreen(
                type = it.toRoute<Screen.PdfTools>().type,
                onGoBack = onGoBack
            )
        }

        composable<Screen.RecognizeText> {
            RecognizeTextScreen(
                uriState = it.toRoute<Screen.RecognizeText>().uri,
                onGoBack = onGoBack
            )
        }

        composable<Screen.GradientMaker> {
            GradientMakerScreen(
                uriState = it.toRoute<Screen.GradientMaker>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Watermarking> {
            WatermarkingScreen(
                uriState = it.toRoute<Screen.Watermarking>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.GifTools>(
            typeMap = mapOf(typeOf<Screen.GifTools.Type?>() to GifToolsNavType)
        ) {
            GifToolsScreen(
                typeState = it.toRoute<Screen.GifTools>().type,
                onGoBack = onGoBack
            )
        }

        composable<Screen.ApngTools>(
            typeMap = mapOf(typeOf<Screen.ApngTools.Type?>() to ApngToolsNavType)
        ) {
            ApngToolsScreen(
                typeState = it.toRoute<Screen.ApngTools>().type,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Zip> {
            ZipScreen(
                uriState = it.toRoute<Screen.Zip>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.JxlTools>(
            typeMap = mapOf(typeOf<Screen.JxlTools.Type?>() to JxlToolsNavType)
        ) {
            JxlToolsScreen(
                typeState = it.toRoute<Screen.JxlTools>().type,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Svg> {
            SvgScreen(
                uriState = it.toRoute<Screen.Svg>().uris,
                onGoBack = onGoBack
            )
        }

        composable<Screen.Convert> {
            ConvertScreen(
                uriState = it.toRoute<Screen.Convert>().uris,
                onGoBack = onGoBack
            )
        }
    }

    ScreenBasedMaxBrightnessEnforcement()
}

private val PdfToolsNavType = navType<Screen.PdfTools.Type>()
private val ApngToolsNavType = navType<Screen.ApngTools.Type>()
private val GifToolsNavType = navType<Screen.GifTools.Type>()
private val JxlToolsNavType = navType<Screen.JxlTools.Type>()
private val FilterNavType = navType<Screen.Filter.Type>()

inline fun <reified T : Parcelable> navType() = object : NavType<T?>(true) {

    @Suppress("DEPRECATION")
    override fun get(
        bundle: Bundle,
        key: String
    ): T? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(key, T::class.java)
    } else {
        bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(value)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: T?
    ) = bundle.putParcelable(key, value)

}