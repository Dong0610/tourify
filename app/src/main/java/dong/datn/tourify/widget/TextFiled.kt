package dong.duan.livechat.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dong.datn.tourify.R
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.widget.onClick

@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(8.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .wrapContentHeight(),

        maxLines = maxLines,
        textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp)
    )
}

private val AppTextInputColors: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorBorderColor = MaterialTheme.colorScheme.onBackground,
        errorTextColor = MaterialTheme.colorScheme.onBackground,
        errorLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorLabelColor = MaterialTheme.colorScheme.onBackground,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
        focusedSupportingTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onBackground
    )

@Composable
fun InputValue(
    value: String,
    hint: String = "",
    maxLines: Int=1,
    textSize: TextUnit =16.sp,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (currentTheme == 1) whiteSmoke else iconBackground,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .background(Color.Transparent),
            textStyle = TextStyle(
                color = textColor(context),
                fontSize = textSize,
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            ),
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = TextStyle(
                                color = darkGray,
                                fontSize = textSize,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun InputValue(
    value: String,
    hint: String = "",
    modifier: Modifier=Modifier,
    maxLines: Int=1,
    textSize: TextUnit =16.sp,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = transparent,
                shape = RoundedCornerShape(12.dp)
            ).wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .background(Color.Transparent),
            textStyle = TextStyle(
                color = textColor(context),
                fontSize = textSize,
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            ),
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = TextStyle(
                                color = darkGray,
                                fontSize = textSize,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Composable
fun SearchBox(
    onTouch: (String) -> Unit
) {
    val context = LocalContext.current

    val valueSearch = remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (currentTheme == 1) whiteSmoke else iconBackground,
                shape = RoundedCornerShape(8.dp)
            ).border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = if(currentTheme==1) transparent else lightGrey),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = valueSearch.value.text,
            onValueChange = { valueSearch.value=TextFieldValue(it)},
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
                .background(Color.Transparent),
            textStyle = TextStyle(
                color = textColor(context),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp), contentAlignment = Alignment.CenterStart
                ) {
                    if (valueSearch.value.text.isEmpty()) {
                        Text(
                            text = context.getString(R.string.search),
                            style = TextStyle(
                                color = darkGray,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
        Image(imageVector = Icons.Rounded.Search, contentDescription = "Search", Modifier.onClick {
            onTouch.invoke(valueSearch.value.text)
        }, colorFilter = ColorFilter.tint(textColor(context)))
        Spacer(modifier = Modifier.width(12.dp))
    }
}
