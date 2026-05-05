package com.apat.machinehelper.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apat.machinehelper.ui.theme.Green500
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 重量计算视图
 */
@Composable
fun WeightView() {
    var iMetal by remember { mutableIntStateOf(1) }
    var iType by remember { mutableIntStateOf(1) }
    var txtCD by remember { mutableStateOf("") }
    var txtZJ by remember { mutableStateOf("") }
    var txtNJ by remember { mutableStateOf("") }
    var txtGD by remember { mutableStateOf("") }
    var txtResult by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val arrMetalMidu = listOf(0.00787, 0.00728, 0.00851, 0.0027) // 钢、球墨铸铁、铜、铝

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "重量计算",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 金属材质选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("钢", "球墨铸铁", "铜", "铝").forEachIndexed { index, name ->
                        FilterChip(
                            selected = iMetal == index + 1,
                            onClick = { iMetal = index + 1 },
                            label = { Text(name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 形状选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("圆柱", "圆管", "方块").forEachIndexed { index, name ->
                        FilterChip(
                            selected = iType == index + 1,
                            onClick = { iType = index + 1 },
                            label = { Text(name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 根据形状显示不同的输入框
                when (iType) {
                    1 -> {
                        // 圆柱
                        InputRow("输入直径：", txtZJ, { txtZJ = it }, "mm")
                    }
                    2 -> {
                        // 圆管
                        InputRow("输入外径：", txtZJ, { txtZJ = it }, "mm")
                        Spacer(modifier = Modifier.height(8.dp))
                        InputRow("输入内径：", txtNJ, { txtNJ = it }, "mm")
                    }
                    3 -> {
                        // 方块
                        InputRow("输入宽度：", txtZJ, { txtZJ = it }, "mm")
                        Spacer(modifier = Modifier.height(8.dp))
                        InputRow("输入高度：", txtGD, { txtGD = it }, "mm")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                InputRow("输入长度：", txtCD, { txtCD = it }, "mm")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val dMidu = arrMetalMidu[iMetal - 1]
                        val dCD = txtCD.toDoubleOrNull()
                        var dResult = 0.0

                        when (iType) {
                            1 -> {
                                val dZJ = txtZJ.toDoubleOrNull()
                                if (dZJ != null && dCD != null) {
                                    dResult = PI * (dZJ / 2).pow(2.0) * dCD * dMidu / 1000
                                }
                            }
                            2 -> {
                                val dZJ = txtZJ.toDoubleOrNull()
                                val dNJ = txtNJ.toDoubleOrNull()
                                if (dZJ != null && dNJ != null && dCD != null) {
                                    dResult = PI * ((dZJ / 2).pow(2.0) - (dNJ / 2).pow(2.0)) * dCD * dMidu / 1000
                                }
                            }
                            3 -> {
                                val dZJ = txtZJ.toDoubleOrNull()
                                val dGD = txtGD.toDoubleOrNull()
                                if (dZJ != null && dGD != null && dCD != null) {
                                    dResult = dZJ * dGD * dCD * dMidu / 1000
                                }
                            }
                        }

                        txtResult = String.format("%.2f Kg", dResult)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green500)
                ) {
                    Text("计算重量")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("重量：", fontWeight = FontWeight.Medium)
                    Text(text = txtResult, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            title = { Text("错误") },
            text = { Text("请确保输入有效的标称尺寸") },
            confirmButton = {
                TextButton(onClick = { showError = false }) {
                    Text("确定")
                }
            }
        )
    }
}

@Composable
fun InputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(100.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Text(unit, modifier = Modifier.padding(start = 8.dp))
    }
}