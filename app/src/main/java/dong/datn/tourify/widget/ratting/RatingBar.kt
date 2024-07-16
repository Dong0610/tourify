package dong.datn.tourify.widget.ratting

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.ratingbar.RatingBarImpl
import com.smarttoolfactory.ratingbar.model.GestureStrategy
import com.smarttoolfactory.ratingbar.model.RateChangeStrategy
import com.smarttoolfactory.ratingbar.model.RatingInterval
import com.smarttoolfactory.ratingbar.model.ShimmerData
import com.smarttoolfactory.ratingbar.model.ShimmerEffect
import com.smarttoolfactory.ratingbar.model.getRatingForInterval

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    imageEmpty: ImageBitmap,
    imageFilled: ImageBitmap,
    tintEmpty: Color? = null,
    tintFilled: Color? = null,
    itemSize: Dp = Dp.Unspecified,
    rateChangeStrategy: RateChangeStrategy = RateChangeStrategy.AnimatedChange(),
    gestureStrategy: GestureStrategy = GestureStrategy.DragAndPress,
    shimmerEffect: ShimmerEffect? = null,
    itemCount: Int = 5,
    space: Dp = 0.dp,
    ratingInterval: RatingInterval = RatingInterval.Unconstrained,
    allowZeroRating: Boolean = true,
    onRatingChangeFinished: ((Float) -> Unit)? = null,
    onRatingChange: (Float) -> Unit
) {
    val intrinsicWidth = imageEmpty.width.toFloat()
    val intrinsicHeight = imageEmpty.height.toFloat()

    val colorFilterEmpty: ColorFilter? = remember(tintEmpty) {
        if (tintEmpty != null) {
            ColorFilter.tint(tintEmpty)
        } else null
    }

    val colorFilterFilled: ColorFilter? = remember(tintFilled) {
        if (tintFilled != null) {
            ColorFilter.tint(tintFilled)
        } else null
    }
    var checkInitialZeroWhenNotAllowed by remember {
        mutableStateOf(allowZeroRating.not() && rating == 0f)
    }

    val ratingFraction = if (checkInitialZeroWhenNotAllowed && rating == 0f) 0f else
        rating.getRatingForInterval(
            ratingInterval = ratingInterval,
            allowZero = allowZeroRating,
        )

    LaunchedEffect(key1 = checkInitialZeroWhenNotAllowed) {
        if (checkInitialZeroWhenNotAllowed) {
            checkInitialZeroWhenNotAllowed = false
        }
    }

    val state = rememberLazyGridState()

    state.layoutInfo.visibleItemsInfo
    RatingBarImpl(
        modifier = modifier,
        rating = ratingFraction,
        intrinsicWidth = intrinsicWidth,
        intrinsicHeight = intrinsicHeight,
        itemSize = itemSize,
        rateChangeStrategy = rateChangeStrategy,
        gestureStrategy = gestureStrategy,
        shimmerEffect = shimmerEffect,
        itemCount = itemCount,
        space = space,
        block = {
                updatedRating: Float,
                spaceBetween: Float,
                shimmerData: ShimmerData? ->
            drawRatingImages(
                rating = updatedRating,
                itemCount = itemCount,
                imageEmpty = imageEmpty,
                imageFilled = imageFilled,
                colorFilterEmpty = colorFilterEmpty,
                colorFilterFilled = colorFilterFilled,
                shimmerData = shimmerData,
                space = spaceBetween,
            )
        },
        ratingInterval = ratingInterval,
        allowZeroRating = allowZeroRating,
        onRatingChangeFinished = onRatingChangeFinished,
        onRatingChange = onRatingChange
    )
}
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    painterEmpty: Painter,
    painterFilled: Painter,
    tintEmpty: Color? = null,
    tintFilled: Color? = null,
    itemSize: Dp = Dp.Unspecified,
    rateChangeStrategy: RateChangeStrategy = RateChangeStrategy.AnimatedChange(),
    gestureStrategy: GestureStrategy = GestureStrategy.DragAndPress,
    shimmerEffect: ShimmerEffect? = null,
    itemCount: Int = 5,
    space: Dp = 0.dp,
    ratingInterval: RatingInterval = RatingInterval.Unconstrained,
    allowZeroRating: Boolean = true,
    onRatingChangeFinished: ((Float) -> Unit)? = null,
    onRatingChange: (Float) -> Unit
) {

    val painterWidth = painterEmpty.intrinsicSize.width
    val painterHeight = painterEmpty.intrinsicSize.height

    val colorFilterEmpty: ColorFilter? = remember(tintEmpty) {
        if (tintEmpty != null) {
            ColorFilter.tint(tintEmpty)
        } else null
    }

    val colorFilterFilled: ColorFilter? = remember(tintFilled) {
        if (tintFilled != null) {
            ColorFilter.tint(tintFilled)
        } else null
    }

    var checkInitialZeroWhenNotAllowed by remember {
        mutableStateOf(allowZeroRating.not() && rating == 0f)
    }

    val ratingFraction = if (checkInitialZeroWhenNotAllowed && rating == 0f) 0f else
        rating.getRatingForInterval(
            ratingInterval = ratingInterval,
            allowZero = allowZeroRating,
        )

    LaunchedEffect(key1 = checkInitialZeroWhenNotAllowed) {
        if (checkInitialZeroWhenNotAllowed) {
            checkInitialZeroWhenNotAllowed = false
        }
    }

    RatingBarImpl(
        modifier = modifier,
        rating = ratingFraction,
        intrinsicWidth = painterWidth,
        intrinsicHeight = painterHeight,
        itemSize = itemSize,
        rateChangeStrategy = rateChangeStrategy,
        gestureStrategy = gestureStrategy,
        shimmerEffect = shimmerEffect,
        itemCount = itemCount,
        space = space,
        block = { updatedRating: Float, spaceBetween: Float, shimmerData: ShimmerData? ->
            drawRatingPainters(
                updatedRating,
                itemCount,
                painterEmpty,
                painterFilled,
                colorFilterEmpty,
                colorFilterFilled,
                shimmerData,
                spaceBetween
            )
        },
        ratingInterval = ratingInterval,
        allowZeroRating = allowZeroRating,
        onRatingChangeFinished = onRatingChangeFinished,
        onRatingChange = onRatingChange
    )
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    imageVectorEmpty: ImageVector,
    imageVectorFilled: ImageVector,
    tintEmpty: Color? = null,
    tintFilled: Color? = null,
    itemSize: Dp = Dp.Unspecified,
    rateChangeStrategy: RateChangeStrategy = RateChangeStrategy.AnimatedChange(),
    gestureStrategy: GestureStrategy = GestureStrategy.DragAndPress,
    shimmerEffect: ShimmerEffect? = null,
    itemCount: Int = 5,
    space: Dp = 0.dp,
    ratingInterval: RatingInterval = RatingInterval.Unconstrained,
    allowZeroRating: Boolean = true,
    onRatingChangeFinished: ((Float) -> Unit)? = null,
    onRatingChange: (Float) -> Unit
) {

    val painterBackground = rememberVectorPainter(image = imageVectorEmpty)
    val painterForeground = rememberVectorPainter(image = imageVectorFilled)

    val painterWidth = painterBackground.intrinsicSize.width
    val painterHeight = painterBackground.intrinsicSize.height

    val colorFilterEmpty: ColorFilter? = remember(tintEmpty) {
        if (tintEmpty != null) {
            ColorFilter.tint(tintEmpty)
        } else null
    }

    val colorFilterFilled: ColorFilter? = remember(tintFilled) {
        if (tintFilled != null) {
            ColorFilter.tint(tintFilled)
        } else null
    }

    // This is for setting initial value to zero if it's set initially and
    // setting value to zero is not allowed with gestures
    var checkInitialZeroWhenNotAllowed by remember {
        mutableStateOf(allowZeroRating.not() && rating == 0f)
    }

    val ratingFraction = if (checkInitialZeroWhenNotAllowed && rating == 0f) 0f else
        rating.getRatingForInterval(
            ratingInterval = ratingInterval,
            allowZero = allowZeroRating,
        )

    LaunchedEffect(key1 = checkInitialZeroWhenNotAllowed) {
        if (checkInitialZeroWhenNotAllowed) {
            checkInitialZeroWhenNotAllowed = false
        }
    }

    RatingBarImpl(
        modifier = modifier,
        rating = ratingFraction,
        intrinsicWidth = painterWidth,
        intrinsicHeight = painterHeight,
        itemSize = itemSize,
        rateChangeStrategy = rateChangeStrategy,
        gestureStrategy = gestureStrategy,
        shimmerEffect = shimmerEffect,
        itemCount = itemCount,
        space = space,
        block = { updatedRating: Float, spaceBetween: Float, shimmerData: ShimmerData? ->
            drawRatingPainters(
                updatedRating,
                itemCount,
                painterBackground,
                painterForeground,
                colorFilterEmpty,
                colorFilterFilled,
                shimmerData,
                spaceBetween
            )
        },
        ratingInterval = ratingInterval,
        allowZeroRating = allowZeroRating,
        onRatingChangeFinished = onRatingChangeFinished,
        onRatingChange = onRatingChange
    )
}
