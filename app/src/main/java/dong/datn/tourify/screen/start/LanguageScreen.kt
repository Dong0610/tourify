package dong.datn.tourify.screen.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.isSetUpLanguage
import dong.datn.tourify.app.language.Language
import dong.datn.tourify.app.language.LanguageUtil
import dong.datn.tourify.app.language.getLangArr
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.changeTheme
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick

@Composable
fun LanguageScreen(navController: NavHostController, onBack: () -> Unit) {
    val context = LocalContext.current

    if (isSetUpLanguage) {
        navController.navigate(AccountScreen.SignInScreen.route) {
            popUpTo(0)
        }
    }
     if(authSignIn!=null){
        onBack.invoke()
    }
    else {

        val selectedLanguageIndex = remember { mutableStateOf(-1) }
        val selectedLanguage = remember { mutableStateOf(Language()) }
        ViewParent(
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Language",
                        fontSize = 24.sp,
                        color = textColor(context),
                        fontFamily = FontFamily(
                            Font(
                                R.font.poppins_bold
                            )
                        ),
                        modifier = Modifier.clickable {
                            if (currentTheme == -1) {
                                currentTheme = 1
                                changeTheme(1, context)
                            } else {
                                currentTheme = -1
                                changeTheme(-1, context)
                            }

                        }
                    )
                    IconView(modifier = Modifier, icon = Icons.Rounded.Check) {
                        LanguageUtil.changeLang(selectedLanguage.value.code, context)
                        navController.navigate(AccountScreen.SignInScreen.route) {
                            popUpTo(0)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(Modifier.fillMaxSize()) {
                    LazyColumn(Modifier.fillMaxSize()) {
                        itemsIndexed(getLangArr(context)) { index, d ->
                            RowItemLanguages(index, d, selectedLanguageIndex.value) { index, data ->
                                selectedLanguageIndex.value = index
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun RowItemLanguages(index: Int, d: Language, select: Int, calback: (Int, Language) -> Unit) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(
        Modifier
            .fillMaxWidth(1f)
            .onClick {
                calback.invoke(index, d)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = d.flag!!),
            contentDescription = "${d.name}",
            Modifier
                .width(40.dp)
                .height(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = d.name!!,
            fontSize = 18.sp,
            color = textColor(LocalContext.current),
            fontFamily = FontFamily(Font(R.font.poppins_regular))
        )

        Spacer(modifier = Modifier.weight(1f))

        if (index == select) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (currentTheme == 1) white else black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = appColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (currentTheme == 1) white else black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

