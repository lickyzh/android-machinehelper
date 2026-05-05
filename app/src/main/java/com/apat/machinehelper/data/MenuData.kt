package com.apat.machinehelper.data

import com.apat.machinehelper.model.MenuItem

/**
 * 菜单静态数据
 */
val menuData: List<MenuItem> = listOf(
    MenuItem(id = 1, name = "大写转换", image = "slider.horizontal.2.gobackward"),
    MenuItem(id = 2, name = "螺纹计算", image = "circle.dashed.inset.filled"),
    MenuItem(id = 3, name = "重量计算", image = "archivebox.circle"),
    MenuItem(id = 4, name = "拉力计算", image = "link.circle"),
    MenuItem(id = 5, name = "公差计算", image = "link.circle")
)