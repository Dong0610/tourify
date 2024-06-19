package dong.duan.livechat.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dong.datn.tourify.R
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.whiteSmoke

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
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (currentTheme == 1) whiteSmoke else lightGrey,
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
                fontSize = 16.sp,
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
                                fontSize = 14.sp,
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
