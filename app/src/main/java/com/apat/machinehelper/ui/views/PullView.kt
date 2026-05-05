package com.apat.machinehelper.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
 * 拉力计算视图
 */
@Composable
fun PullView() {
    var txtWai by remember { mutableStateOf("73") }
    var txtNei by remember { mutableStateOf("69") }
    var txtPull by remember { mutableStateOf("210") }
    var txtAsk by remember { mutableStateOf("200") }
    var txtResult by remember { mutableStateOf("") }

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
                    text = "拉力计算",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = txtWai,
                    onValueChange = { txtWai = it },
                    label = { Text("输入外径 (mm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = txtNei,
                    onValueChange = { txtNei = it },
                    label = { Text("输入内径 (mm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = txtPull,
                    onValueChange = { txtPull = it },
                    label = { Text("实际拉断 (KN)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = txtAsk,
                    onValueChange = { txtAsk = it },
                    label = { Text("拉断要求 (KN)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val dWai = txtWai.toDoubleOrNull() ?: 0.0
                        val dNei = txtNei.toDoubleOrNull() ?: 0.0
                        val dAsk = txtAsk.toDoubleOrNull() ?: 0.0
                        val dPull = txtPull.toDoubleOrNull() ?: 0.0

                        val dMJ_Nei = PI * (dNei / 2).pow(2.0)
                        val dMJ = PI * (dWai / 2).pow(2.0) - dMJ_Nei
                        val dXiShu = dPull / dMJ
                        val dTmp = dAsk / dXiShu + dMJ_Nei

                        txtResult = String.format("%.2f", sqrt(dTmp / PI) * 2)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green500)
                ) {
                    Text("计算拉断外径")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("拉断外径：", fontWeight = FontWeight.Medium)
                    Text(text = "$txtResult mm", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}