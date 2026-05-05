package com.apat.machinehelper.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apat.machinehelper.ui.theme.Green500
import kotlin.math.pow

/**
 * 螺纹计算视图
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuowenView() {
    var txtZhiJing by remember { mutableStateOf("30") }
    var iLuoJu by remember { mutableIntStateOf(4) }
    val luoJuList = listOf(1.0, 1.25, 1.5, 1.75, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0)

    var txtDJ by remember { mutableStateOf("") }
    var txtDJu by remember { mutableStateOf("") }
    var txtDJd by remember { mutableStateOf("") }
    var txtZJ by remember { mutableStateOf("") }
    var txtZJu by remember { mutableStateOf("") }
    var txtZJd by remember { mutableStateOf("") }
    var txtXJ by remember { mutableStateOf("") }
    var txtXJu by remember { mutableStateOf("") }
    var txtXJd by remember { mutableStateOf("") }
    var txtZJu1 by remember { mutableStateOf("") }
    var txtZJd1 by remember { mutableStateOf("") }
    var txtSZ by remember { mutableStateOf("") }
    var txtSZM by remember { mutableStateOf("") }
    var txtSZMu by remember { mutableStateOf("") }
    var txtSZMd by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 螺纹中径计算标题
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "螺纹中径计算",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("螺纹尺寸：M", modifier = Modifier.width(100.dp))
                    OutlinedTextField(
                        value = txtZhiJing,
                        onValueChange = { txtZhiJing = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(80.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 螺距选择
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.width(100.dp)
                    ) {
                        OutlinedTextField(
                            value = luoJuList[iLuoJu].toString(),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            luoJuList.forEachIndexed { index, value ->
                                DropdownMenuItem(
                                    text = { Text(value.toString()) },
                                    onClick = {
                                        iLuoJu = index
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val iZJ = txtZhiJing.toDoubleOrNull() ?: 0.0
                            val luoJu = luoJuList[iLuoJu]

                            // 大径计算
                            txtDJ = String.format("%.3f", iZJ)
                            val dTmp = 0.015 + 0.011 * luoJu
                            txtDJu = String.format("-%.3f", dTmp)
                            txtDJd = String.format("-%.3f", dTmp + 0.18 * luoJu.pow(2.0 / 3.0) - (0.00315 / kotlin.math.sqrt(luoJu)))

                            // 中径小径计算
                            txtZJ = String.format("%.3f", iZJ - (luoJu * 0.6495))
                            txtXJ = String.format("%.3f", iZJ - (luoJu * 1.0825))
                            val dTD = 0.09 * luoJu.pow(0.4) * iZJ.pow(0.1)
                            txtZJd = String.format("-%.3f", dTmp + dTD)
                            txtZJu = txtDJu

                            txtZJu1 = String.format("+%.3f", 1.32 * dTD)
                            txtZJd1 = "0"

                            txtXJu = String.format("+%.3f", 0.23 * luoJu.pow(0.7))
                            txtXJd = "0"

                            // 三针测量尺寸
                            txtSZ = when (luoJu) {
                                2.0 -> "1.157"
                                2.5 -> "1.441"
                                3.0 -> "1.732"
                                3.5 -> "2.050"
                                4.0 -> "2.311"
                                4.5 -> "2.595"
                                5.0 -> "2.886"
                                5.5 -> "3.177"
                                6.0 -> "3.550"
                                else -> ""
                            }
                            txtSZM = String.format("%.3f", iZJ - (luoJu * 0.6495) + 3 * (txtSZ.toDoubleOrNull() ?: 0.0) - 0.866 * luoJu)
                            txtSZMu = txtZJu
                            txtSZMd = txtZJd
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Green500)
                    ) {
                        Text("计算")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 外螺纹结果
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "外螺纹 (6g)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ResultRow("大径：", txtDJ, txtDJu, txtDJd)
                ResultRow("中径：", txtZJ, txtZJu, txtZJd)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 内螺纹结果
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "内螺纹 (6H)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ResultRow("中径：", txtZJ, txtZJu1, txtZJd1)
                ResultRow("小径：", txtXJ, txtXJu, txtXJd)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 三针测量尺寸
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "三针测量尺寸",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("优选：$txtSZ")
                        Text("M值：$txtSZM")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(txtSZMu)
                        Text(txtSZMd)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, upper: String, lower: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$label $value")
        Column(horizontalAlignment = Alignment.End) {
            Text(upper)
            Text(lower)
        }
    }
}