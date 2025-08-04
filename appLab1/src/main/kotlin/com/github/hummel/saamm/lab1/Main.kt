package com.github.hummel.saamm.lab1

import org.apache.commons.math3.distribution.UniformRealDistribution
import org.apache.commons.math3.stat.inference.ChiSquareTest
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.BitmapEncoder.BitmapFormat
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.XYChart
import java.io.File
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

private val outputDir: File = mdIfNot("output")

fun main() {
	val x0 = 123456789L
	val a = 152353L
	val c = 5.0.pow(11).toLong()
	val m = 2.0.pow(30).toLong()

	val quantity = 100
	val randomNumbers = generateRandomNumbers(x0, a, c, m, quantity)

	randomNumbers.sort()

	plotHistogram(randomNumbers)

	chiSquareTest(randomNumbers)

	ksTest(randomNumbers)

	plotRandomUniform(randomNumbers)
}

fun generateRandomNumbers(x0: Long, a: Long, c: Long, m: Long, count: Int): DoubleArray {
	return generateSequence(x0) {
		(a * it + c) % m
	}.take(count).map {
		it.toDouble() / m
	}.toList().toDoubleArray()
}

fun plotHistogram(randomNumbers: DoubleArray) {
	val numberOfIntervals = ceil(log2(randomNumbers.size.toDouble())).toInt() + 1

	val minValue = randomNumbers.minOrNull() ?: 0.0
	val maxValue = randomNumbers.maxOrNull() ?: 1.0
	val intervalSize = (maxValue - minValue) / numberOfIntervals

	val histogram = IntArray(numberOfIntervals)
	randomNumbers.forEach { time ->
		val index = ((time - minValue) / intervalSize).toInt().coerceIn(0, numberOfIntervals - 1)
		histogram[index]++
	}

	val chart = CategoryChart(1600, 900)
	chart.title = "Гистограмма"
	chart.xAxisTitle = "X"
	chart.yAxisTitle = "Y"

	val xData = DoubleArray(numberOfIntervals) { minValue + it * intervalSize + intervalSize / 2 }
	val yData = histogram.map { it.toDouble() }.toDoubleArray()

	chart.addSeries("Гистограмма", xData, yData)

	BitmapEncoder.saveBitmap(chart, "./${outputDir}/histogram", BitmapFormat.JPG)
}

fun chiSquareTest(randomNumbers: DoubleArray) {
	val numberOfIntervals = ceil(log2(randomNumbers.size.toDouble())).toInt() + 1

	val minValue = randomNumbers.minOrNull() ?: 0.0
	val maxValue = randomNumbers.maxOrNull() ?: 1.0
	val intervalSize = (maxValue - minValue) / numberOfIntervals

	val observedFrequencies = IntArray(numberOfIntervals)
	randomNumbers.forEach { time ->
		val index = ((time - minValue) / intervalSize).toInt().coerceIn(0, numberOfIntervals - 1)
		observedFrequencies[index]++
	}

	val expectedFrequency = randomNumbers.size.toDouble() / numberOfIntervals
	val expectedFrequencies = DoubleArray(numberOfIntervals) { expectedFrequency }

	val observedFrequenciesLong = observedFrequencies.map { it.toLong() }.toLongArray()

	val chiSquareTest = ChiSquareTest()
	val pValue = chiSquareTest.chiSquareTest(expectedFrequencies, observedFrequenciesLong)

	val alpha = 0.05
	val isUniform = pValue > alpha

	println("Результат теста хи-квадрат: p-значение = $pValue")
	println("Данные${if (isUniform) "" else " не"} равномерно распределены.")
}

fun ksTest(randomNumbers: DoubleArray) {
	val uniformRealDistribution = UniformRealDistribution(0.0, 1.0)

	val ksTest = KolmogorovSmirnovTest()
	val pValue = ksTest.kolmogorovSmirnovTest(uniformRealDistribution, randomNumbers)

	val alpha = 0.05
	val isUniform = pValue > alpha

	println("Результат теста КС: p-значение = $pValue")
	println("Данные${if (isUniform) "" else " не"} равномерно распределены.")
}

fun plotRandomUniform(randomNumbers: DoubleArray) {
	val expectedValues = DoubleArray(100) { it * 0.01 }

	val chart = XYChart(1600, 900)
	chart.title = "Случайные значения"
	chart.xAxisTitle = "X"
	chart.yAxisTitle = "Y"

	chart.addSeries(
		"Случайные значения", randomNumbers.indices.map { it.toDouble() }.toDoubleArray(), randomNumbers
	)
	chart.addSeries(
		"Равномерное распределение", randomNumbers.indices.map { it.toDouble() }.toDoubleArray(), expectedValues
	)

	BitmapEncoder.saveBitmap(chart, "./${outputDir}/random_uniform", BitmapFormat.JPG)
}

fun mdIfNot(path: String): File {
	val folder = File(path)
	if (!folder.exists()) {
		folder.mkdirs()
	}
	return folder
}