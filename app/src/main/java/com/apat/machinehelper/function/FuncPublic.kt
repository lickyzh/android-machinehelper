package com.apat.machinehelper.function

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * 公共函数工具类
 * 包含阿拉伯数字转中文大写金额、日期转中文大写等功能
 */

/**
 * 阿拉伯数字金额转换为中文大写金额
 * @param arabicNum 要转换的金额数字
 * @return 中文大写金额字符串
 */
fun translationArabicNum(arabicNum: Double): String {
    val df = DecimalFormat("0.00")
    val arabicNumStr = df.format(arabicNum)
    val dotIndex = arabicNumStr.indexOf(".")

    if (dotIndex == -1) {
        return integerToChinese(arabicNumStr.toLong()) + "元"
    }

    val integerPart = arabicNumStr.substring(0, dotIndex)
    val decimalPart = arabicNumStr.substring(dotIndex + 1)

    val intStr = integerToChinese(integerPart.toLong())
    var doubleStr = integerToChinese(decimalPart.toLong())

    if (decimalPart.toLong() == 0L) {
        return intStr + "元"
    }

    // 处理拾、佰、仟等单位
    if (decimalPart.toLong() in 10..19) {
        doubleStr = doubleStr.replace("拾", "壹")
    } else {
        if (decimalPart.toLong() > 0) {
            val units = listOf("拾", "佰", "仟", "万", "亿")
            for (unit in units) {
                doubleStr = doubleStr.replace(unit, "")
            }
        }
    }

    val first = decimalPart.substring(0, 1)
    if (first == "0") {
        doubleStr += "分"
    } else {
        val last = decimalPart.substring(1)
        doubleStr = doubleStr.substring(0, 1) + "角" + doubleStr.substring(1)
        if (last.toLong() > 0) {
            doubleStr += "分"
        }
    }

    return intStr + "元" + doubleStr
}

/**
 * 整数转换为中文大写
 * @param number 整数
 * @return 中文大写字符串
 */
private fun integerToChinese(number: Long): String {
    if (number == 0L) return ""

    val zhNumbers = listOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
    val units = listOf("", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟")

    val str = number.toString()
    val length = str.length

    var result = ""
    var beforeNum = 0

    for (i in 0 until length) {
        val index = length - 1 - i
        val currentNum = str[i].digitToInt()

        if (i == 0) {
            if (currentNum != 0) {
                result = zhNumbers[currentNum]
                continue
            }
        } else {
            beforeNum = str[i - 1].digitToInt()
        }

        val unitIndex = index
        if (unitIndex in listOf(1, 2, 3, 5, 6, 7, 9, 10, 11)) {
            // 处理一开头的含十单位
            if (currentNum == 1 && unitIndex in listOf(1, 5, 9) && index == length - 1) {
                result = units[unitIndex] + result
            } else if (currentNum != 0) {
                result = zhNumbers[currentNum] + units[unitIndex] + result
            } else if (beforeNum != 0) {
                result = zhNumbers[currentNum] + result
            }
            continue
        }

        if (unitIndex in listOf(4, 8, 12)) {
            result = units[unitIndex] + result
            if ((beforeNum != 0 && currentNum == 0) || currentNum != 0) {
                result = zhNumbers[currentNum] + result
            }
        }
    }

    return result
}

/**
 * 数字转换为中文大写（银行格式）
 * @param number 数字
 * @param spelling 是否包含单位
 * @return 中文大写字符串
 */
fun numberCN(number: Int, spelling: Boolean = true): String {
    if (number < 0) return ""

    val units = listOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
    val bigUnits = listOf("", "拾", "佰", "仟", "萬", "亿", "兆")

    var result = ""
    var remainder = number
    var index = 0

    while (remainder > 0) {
        val digit = remainder % 10
        remainder /= 10

        if (digit != 0) {
            var digitText = units[digit]
            if (spelling) {
                digitText += bigUnits[index]
            }
            result = digitText + result
        } else {
            if (result.isNotEmpty()) {
                result = "零$result"
            }
        }
        index++
    }

    return result
}

/**
 * 日期转换为中文大写日期格式
 * @param year 年
 * @param month 月
 * @param day 日
 * @return 中文大写日期字符串
 */
fun dateToCN(year: Int, month: Int, day: Int): String {
    var dateCN = numberCN(year, false) + "年"

    // 处理月份
    dateCN += when (month) {
        1, 2, 10 -> "零" + numberCN(month)
        else -> numberCN(month)
    }
    dateCN += "月"

    // 处理日期
    dateCN += when {
        day < 11 || day == 20 || day == 30 -> "零" + numberCN(day)
        else -> numberCN(day)
    }
    dateCN += "日"

    return dateCN
}