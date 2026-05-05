package com.apat.machinehelper.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apat.machinehelper.data.menuData

/**
 * 主视图 - 显示菜单列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    var selectedMenuId by remember { mutableIntStateOf(0) }
    var currentTitle by remember { mutableStateOf("机械小助手") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) },
                navigationIcon = {
                    if (selectedMenuId != 0) {
                        IconButton(onClick = {
                            selectedMenuId = 0
                            currentTitle = "机械小助手"
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedMenuId) {
                0 -> MenuListView(
                    onMenuSelected = { id, name ->
                        selectedMenuId = id
                        currentTitle = name
                    }
                )
                1 -> NumberView()
                2 -> LuowenView()
                3 -> WeightView()
                4 -> PullView()
                5 -> ToleranceView()
                else -> MenuListView(
                    onMenuSelected = { id, name ->
                        selectedMenuId = id
                        currentTitle = name
                    }
                )
            }
        }
    }
}

/**
 * 菜单列表视图
 */
@Composable
fun MenuListView(onMenuSelected: (Int, String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(menuData) { menu ->
            MenuCard(
                name = menu.name,
                icon = getIconForMenu(menu.image),
                onClick = { onMenuSelected(menu.id, menu.name) }
            )
        }
        item {
            // 版本信息
            Text(
                text = "1.0.406.241",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 菜单卡片组件
 */
@Composable
fun MenuCard(
    name: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 根据图标名称获取对应的Icon
 * 由于Android没有SF Symbols，这里使用Material Icons
 */
private fun getIconForMenu(iconName: String): ImageVector {
    return when (iconName) {
        "slider.horizontal.2.gobackward" -> Icons.Filled.Settings
        "circle.dashed.inset.filled" -> Icons.Filled.Circle
        "archivebox.circle" -> Icons.Filled.Inbox
        "link.circle" -> Icons.Filled.Link
        else -> Icons.Filled.Calculate
    }
}