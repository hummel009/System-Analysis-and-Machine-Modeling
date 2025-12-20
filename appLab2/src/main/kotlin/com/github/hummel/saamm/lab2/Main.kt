package com.github.hummel.saamm.lab2

import java.util.*

private const val PARTS_1_FOR_PRODUCT: Int = 3
private const val PARTS_2_FOR_PRODUCT: Int = 2

private const val PRODUCTS_FOR_PACKET: Int = 8
private const val PACKETS_FOR_STORAGE: Int = 3

fun main() {
	val statisticsArray = simulateRuns(10)

	printAverageStatistics(statisticsArray)
}

fun simulateRuns(runs: Int): Array<Statistics> {
	val statisticsArray = Array(runs) { Statistics() }
	val threadArray = statisticsArray.map { stat ->
		Thread {
			Factory().apply {
				statistics = stat
			}.run()
		}
	}

	threadArray.forEach { it.start() }
	threadArray.forEach { it.join() }

	return statisticsArray
}

class Factory {
	private val random: Random = Random()

	private var partsType1: Int = 0
	private var partsType2: Int = 0
	private var accumulatorPartsType1: Int = 0
	private var accumulatorPartsType2: Int = 0
	private var technoModuleParts: Int = 0
	private var packPlaceProducts: Int = 0
	private var packPlacePackets: Int = 0
	private var storagePackets: Int = 0

	var statistics: Statistics = Statistics()

	fun run() {
		val queue = PriorityQueue<Task>(compareBy { it.endTime })
		var currentTime = 0.0

		queue.add(Task(currentTime, TaskType.GENERATOR))

		while (storagePackets <= 1000) {
			val (endTime, taskType) = queue.poll()
			currentTime = endTime

			when (taskType) {
				TaskType.GENERATOR -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (random.nextBoolean()) {
						partsType1++
						statistics.partsType1++
						queue.add(Task(currentTime + time, TaskType.MACHINE_1))
					} else {
						partsType2++
						statistics.partsType2++
						queue.add(Task(currentTime + time, TaskType.MACHINE_2))
					}

					queue.add(Task(currentTime + time, TaskType.GENERATOR))
				}

				TaskType.MACHINE_1 -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (partsType1 >= 1) {
						partsType1--
						accumulatorPartsType1++
						statistics.accumulatorPartsType1++

						queue.add(Task(currentTime + time, TaskType.TRANSPORTER))
					}
				}

				TaskType.MACHINE_2 -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (partsType2 >= 1) {
						partsType2--
						accumulatorPartsType2++
						statistics.accumulatorPartsType2++

						queue.add(Task(currentTime + time, TaskType.TRANSPORTER))
					}
				}

				TaskType.ASSEMBLER -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (technoModuleParts >= PARTS_1_FOR_PRODUCT + PARTS_2_FOR_PRODUCT) {
						technoModuleParts -= PARTS_1_FOR_PRODUCT + PARTS_2_FOR_PRODUCT
						packPlaceProducts++
						statistics.packPlaceProducts++

						queue.add(Task(currentTime + time, TaskType.PACKER))
					}
				}

				TaskType.TRANSPORTER -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (accumulatorPartsType1 >= PARTS_1_FOR_PRODUCT && accumulatorPartsType2 >= PARTS_2_FOR_PRODUCT) {
						accumulatorPartsType1 -= PARTS_1_FOR_PRODUCT
						accumulatorPartsType2 -= PARTS_2_FOR_PRODUCT

						technoModuleParts += PARTS_1_FOR_PRODUCT + PARTS_2_FOR_PRODUCT

						queue.add(Task(currentTime + time, TaskType.ASSEMBLER))
					}

					if (packPlacePackets >= PACKETS_FOR_STORAGE) {
						packPlacePackets -= PACKETS_FOR_STORAGE
						storagePackets += PACKETS_FOR_STORAGE
						statistics.storagePackets += PACKETS_FOR_STORAGE
					}
				}

				TaskType.PACKER -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (packPlaceProducts >= PRODUCTS_FOR_PACKET) {
						packPlaceProducts -= PRODUCTS_FOR_PACKET
						packPlacePackets++
						statistics.packPlacePackets++

						queue.add(Task(currentTime + time, TaskType.TRANSPORTER))
					}
				}
			}
		}

		statistics.duration = (currentTime / 1000)
	}
}

class Statistics {
	var partsType1: Int = 0
	var partsType2: Int = 0
	var accumulatorPartsType1: Int = 0
	var accumulatorPartsType2: Int = 0
	var packPlaceProducts: Int = 0
	var packPlacePackets: Int = 0
	var storagePackets: Int = 0
	var duration: Double = 0.0

	fun getProduceTime(): Double = duration / (storagePackets * 8)
}

data class Task(val endTime: Double, val taskType: TaskType)

enum class TaskType {
	GENERATOR, MACHINE_1, MACHINE_2, ASSEMBLER, TRANSPORTER, PACKER
}