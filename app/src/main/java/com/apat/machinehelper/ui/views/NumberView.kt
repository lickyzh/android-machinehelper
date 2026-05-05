package com.apat.machinehelper.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apat.machinehelper.function.translationArabicNum
import com.apat.machinehelper.ui.theme.Green500
import java.text.SimpleDateFormat
import java.util.*

/**
 * 大写金额和日期转换视图
 */
@Composable
fun NumberView() {
    var txtMoney by remember { mutableStateOf("") }
    var txtCN by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var txtDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 大写金额转换
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "大写金额转换",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = txtMoney,
                    onValueChange = { txtMoney = it },
                    label = { Text("输入金额") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val num = txtMoney.toDoubleOrNull() ?: 0.0
                        txtCN = translationArabicNum(num)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green500)
                ) {
                    Text("转换大写金额")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("大写金额：", fontWeight = FontWeight.Medium)
                    Text(text = txtCN, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 大写日期转换
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "大写银行日期转换",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 日期选择器
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)

                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = { },
                    label = { Text("选择日期") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = selectedDate.time
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        calendar.time = selectedDate
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        txtDate = com.apat.machinehelper.function.dateToCN(year, month, day)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green500)
                ) {
                    Text("转换日期")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("大写：", fontWeight = FontWeight.Medium)
                    Text(text = txtDate, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}