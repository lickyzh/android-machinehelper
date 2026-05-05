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
import com.apat.machinehelper.ui.theme.Blue500
import kotlin.math.pow

/**
 * 公差计算视图
 */
@Composable
fun ToleranceView() {
    var nominalValue by remember { mutableStateOf("") }
    var isDiameterExternal by remember { mutableStateOf(true) }
    var selectedToleranceClass by remember { mutableStateOf("") }
    var selectedToleranceJS by remember { mutableStateOf("") }
    var upperDeviation by remember { mutableDoubleStateOf(0.0) }
    var lowerDeviation by remember { mutableDoubleStateOf(0.0) }
    var showingAlert by remember { mutableStateOf(false) }

    val toleranceClasses = if (isDiameterExternal) {
        listOf("a", "b", "c", "d", "e", "f", "g", "h", "js", "k", "m", "n", "p", "r", "s", "t", "u", "v", "x", "y", "z")
    } else {
        listOf("A", "B", "C", "D", "E", "F", "G", "H", "JS", "K", "M", "N", "P", "R", "S", "T", "U", "V", "X", "Y", "Z")
    }

    val toleranceNumbers = listOf("5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16")
    val jsGrades = listOf("JS5", "JS6", "JS7", "JS8", "JS9", "JS10", "JS11", "JS12", "JS13", "JS14", "JS15", "JS16")

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
                    text = "机械公差计算器",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 外径/内径选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = isDiameterExternal,
                        onClick = { isDiameterExternal = true },
                        label = { Text("外径") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FilterChip(
                        selected = !isDiameterExternal,
                        onClick = { isDiameterExternal = false },
                        label = { Text("内径") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 标称尺寸输入
                OutlinedTextField(
                    value = nominalValue,
                    onValueChange = { nominalValue = it },
                    label = { Text("标称尺寸 (mm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 公差类型选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = selectedToleranceClass != "js",
                        onClick = {
                            selectedToleranceClass = ""
                            selectedToleranceJS = ""
                        },
                        label = { Text("标准公差") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FilterChip(
                        selected = selectedToleranceClass == "js",
                        onClick = { selectedToleranceClass = "js" },
                        label = { Text("JS公差") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedToleranceClass == "js") {
                    // JS公差等级选择
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedToleranceJS,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("选择JS等级") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            jsGrades.forEach { grade ->
                                DropdownMenuItem(
                                    text = { Text(grade) },
                                    onClick = {
                                        selectedToleranceJS = grade
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // 标准公差代号选择
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 字母选择
                        var expandedLetter by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandedLetter,
                            onExpandedChange = { expandedLetter = !expandedLetter },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedToleranceClass.take(1),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("字母") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLetter) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedLetter,
                                onDismissRequest = { expandedLetter = false }
                            ) {
                                toleranceClasses.filter { it != "js" }.forEach { letter ->
                                    DropdownMenuItem(
                                        text = { Text(letter) },
                                        onClick = {
                                            selectedToleranceClass = letter + selectedToleranceClass.drop(1).filter { it.isDigit() }
                                            expandedLetter = false
                                        }
                                    )
                                }
                            }
                        }

                        // 数字选择
                        var expandedNumber by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandedNumber,
                            onExpandedChange = { expandedNumber = !expandedNumber },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedToleranceClass.filter { it.isDigit() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("数字") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNumber) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedNumber,
                                onDismissRequest = { expandedNumber = false }
                            ) {
                                toleranceNumbers.forEach { number ->
                                    DropdownMenuItem(
                                        text = { Text(number) },
                                        onClick = {
                                            selectedToleranceClass = selectedToleranceClass.filter { !it.isDigit() } + number
                                            expandedNumber = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // 当前选择显示
                if (selectedToleranceClass.isNotEmpty() || selectedToleranceJS.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "已选择: ${if (selectedToleranceJS.isEmpty()) selectedToleranceClass else selectedToleranceJS}",
                        fontWeight = FontWeight.Medium,
                        color = Blue500
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val nominal = nominalValue.toDoubleOrNull()
                        if (nominal == null || nominal <= 0) {
                            showingAlert = true
                            return@Button
                        }

                        if (selectedToleranceClass == "js") {
                            if (selectedToleranceJS.isEmpty()) {
                                showingAlert = true
                                return@Button
                            }
                            val jsNumber = selectedToleranceJS.drop(2).toIntOrNull() ?: 7
                            val tolerance = calculateJSTolerance(nominal, jsNumber)
                            upperDeviation = tolerance / 2
                            lowerDeviation = -tolerance / 2
                        } else {
                            if (selectedToleranceClass.length < 2) {
                                showingAlert = true
                                return@Button
                            }
                            val (upper, lower) = calculateDeviations(nominal, selectedToleranceClass, isDiameterExternal)
                            upperDeviation = upper
                            lowerDeviation = lower
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue500)
                ) {
                    Text("计算")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 结果显示
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "计算结果",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("上偏差：", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = String.format("%.3f mm", upperDeviation),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("下偏差：", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = String.format("%.3f mm", lowerDeviation),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    if (showingAlert) {
        AlertDialog(
            onDismissRequest = { showingAlert = false },
            title = { Text("错误") },
            text = { Text("请确保输入有效的标称尺寸和完整的公差信息") },
            confirmButton = {
                TextButton(onClick = { showingAlert = false }) {
                    Text("确定")
                }
            }
        )
    }
}

/**
 * 计算JS公差
 */
private fun calculateJSTolerance(nominalValue: Double, jsGrade: Int): Double {
    val i = nominalValue.pow(1.0 / 3.0) + 0.001 * nominalValue
    return i * jsGrade / 10.0
}

/**
 * 计算标准公差偏差
 */
private fun calculateDeviations(nominalValue: Double, toleranceClass: String, isExternal: Boolean): Pair<Double, Double> {
    val letter = toleranceClass.take(1)
    val number = toleranceClass.drop(1).toIntOrNull() ?: 7

    val basicTolerance = nominalValue.pow(1.0 / 3.0) * number / 100.0

    val letterValue = if (isExternal) {
        letter.lowercase().first().code - 'a'.code
    } else {
        letter.first().code - 'A'.code
    }

    val baseDeviation = letterValue * basicTolerance / 10.0

    return Pair(baseDeviation + basicTolerance / 2, baseDeviation - basicTolerance / 2)
}