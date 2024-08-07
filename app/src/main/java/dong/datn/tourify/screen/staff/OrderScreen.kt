package dong.datn.tourify.screen.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.appContext
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Order
import dong.datn.tourify.model.OrderStatus
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.limeGreen
import dong.datn.tourify.ui.theme.orange
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.launch

@Composable
fun OrderScreen(nav: NavController, drawer: () -> Unit) {

    val stateDialog = remember {
        mutableStateOf(false)
    }
    val currentOrder = remember {
        mutableStateOf<Order?>(null)
    }
    val currentTour = remember {
        mutableStateOf<Tour?>(null)
    }
    val listWaiting = remember {
        mutableStateOf(viewModels.listAllOrder.value.filter { it.orderStatus == OrderStatus.WAITING })
    }

    val listUpcomming = remember {
        mutableStateOf(viewModels.listAllOrder.value.filter { it.orderStatus == OrderStatus.PENDING || it.orderStatus == OrderStatus.RUNNING })
    }



    ViewParent {
        Column(Modifier.matchParentSize()) {
            TextView(text = appContext.getString(R.string.cf_cancel_bill)+ ": "+ listWaiting.value.size  )
            LazyColumn(Modifier.heightPercent(40f)) {
                this.items(listWaiting.value) {
                    OrderItem(order = it) { order, tour ->
                        stateDialog.value = true
                        currentTour.value = tour
                        currentOrder.value = order
                        showToast(order.orderID)
                    }
                    SpaceH(h = 6)
                }
            }
            SpaceH(h = 16)
            TextView(text = appContext.getString(R.string.all_bill_upcoming)+ ": "+ listUpcomming.value.size)
            LazyColumn(Modifier.heightPercent(40f)) {
                this.items(listUpcomming.value) {
                    OrderAllItem(order = it) {
                    }
                    SpaceH(h = 6)
                }
            }
        }
    }

    if (stateDialog.value) {
        DialogCancelBill(currentOrder.value!!, currentTour.value!!, stateDialog) { order, tour ->
            viewModels.confirmCancelOrder(order, tour) {
                listWaiting.value = listWaiting.value.toMutableList().apply {
                    remove(currentOrder.value)
                }
            }
        }
    }

}

@Composable
fun OrderAllItem(order: Order, content: Any) {
    val coroutineScope = rememberCoroutineScope()
    var userName = remember {
        mutableStateOf("")
    }
    val tour = remember {
        mutableStateOf<Tour?>(null)
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userName.value =
                Firestore.getValue<String>("$USERS/${order.userOrderId}", "name").toString()
        }
        Firestore.fetchById<Tour>("$TOUR/${order.tourID}") {
            tour.value = it!!
        }
    }

    Row(
        Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight()
            .padding(4.dp)
            .onClick {

            }
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpaceW(w = 4)
        RoundedImage(
            data = if (tour.value != null) tour.value?.tourImage?.get(0) else R.drawable.img_test_data_1,
            modifier = Modifier
                .heightPercent(10f)
                .padding(vertical = 4.dp)
                .widthPercent(28f), shape = RoundedCornerShape(8.dp)
        )
        SpaceW(w = 12)
        Column(Modifier.weight(1f)) {
            TextView(
                text = tour.value?.tourName ?: "",
                maxLine = 2,
                font = Font(R.font.poppins_medium),
                textSize = 13
            )
            SpaceH(h = 3)
            TextView(
                text = appContext.getString(R.string.customer) + " " + userName.value,
                font = Font(R.font.poppins_regular),
                textSize = 12, maxLine = 1,
            )
            SpaceH(h = 3)
            TextView(
                text = appContext.getString(R.string.created_time) + ": " + order.orderDate,
                textSize = 12, maxLine = 1
            )
        }
        SpaceW(w = 4)

    }


}

@Composable
fun DialogCancelBill(
    order: Order,
    nav: Tour,
    stateDialog: MutableState<Boolean>, callback: (Order, Tour) -> Unit
) {
    val context = LocalContext.current
    val textOrder = remember {
        mutableStateOf("")

    }
    textOrder.value =
        context.getString(R.string.cf_order_cancel) + " " + order.prePayment.toCurrency() + "?"
    Dialog(
        onDismissRequest = { viewModels.dialogState.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(1f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(1f)
                ) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                        FaIcon(faIcon = FaIcons.ExclamationCircle, tint = orange, size = 28.dp)


                        Space(w = 8)
                        TextView(
                            text = context.resources.getString(R.string.warning),
                            modifier = Modifier,
                            textSize = 18,
                            font = Font(R.font.poppins_semibold),
                            color = orange
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = textOrder.value,
                        modifier = Modifier,
                        maxLine = 100,
                        textSize = 18,
                        font = Font(R.font.poppins_regular)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(Modifier.fillMaxWidth(1f)) {
                        Box(
                            Modifier
                                .onClick {
                                    stateDialog.value = false

                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(lightGrey, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center) {
                            Text(
                                LocalContext.current.getString(R.string.cancel),
                                Modifier,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            Modifier
                                .onClick {


                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(limeGreen, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LocalContext.current.getString(R.string.Ok),
                                Modifier,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateDialog.value = false
                                        callback.invoke(order, nav)
                                    })
                        }


                    }

                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, callback: (Order, Tour) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var userName = remember {
        mutableStateOf("")
    }
    val tour = remember {
        mutableStateOf<Tour?>(null)
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userName.value =
                Firestore.getValue<String>("$USERS/${order.userOrderId}", "name").toString()
        }
        Firestore.fetchById<Tour>("$TOUR/${order.tourID}") {
            tour.value = it!!
        }
    }

    Row(
        Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight()
            .padding(4.dp)
            .onClick {

            }
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpaceW(w = 4)
        RoundedImage(
            data = if (tour.value != null) tour.value?.tourImage?.get(0) else R.drawable.img_test_data_1,
            modifier = Modifier
                .heightPercent(10f)
                .padding(vertical = 4.dp)
                .widthPercent(28f), shape = RoundedCornerShape(8.dp)
        )
        SpaceW(w = 12)
        Column(Modifier.weight(1f)) {
            TextView(
                text = tour.value?.tourName ?: "",
                maxLine = 2,
                font = Font(R.font.poppins_medium),
                textSize = 13
            )
            SpaceH(h = 3)
            TextView(
                text = appContext.getString(R.string.customer) + " " + userName.value,
                font = Font(R.font.poppins_regular),
                textSize = 12, maxLine = 1,
            )
            SpaceH(h = 3)
            TextView(
                text = appContext.getString(R.string.created_time) + ": " + order.orderDate,
                textSize = 12, maxLine = 1
            )
        }
        Box(
            modifier = Modifier
                .background(appColor, shape = RoundedCornerShape(6.dp))
                .width(60.dp)
                .wrapContentHeight()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            TextView(
                text = appContext.getString(R.string.confirm),
                font = Font(R.font.poppins_medium),
                textSize = 12, maxLine = 1, color = white
            )
            Box(modifier = Modifier
                .matchParentSize()
                .clickable {
                    callback.invoke(order, tour.value!!)
                })
        }

        SpaceW(w = 4)

    }


}





















