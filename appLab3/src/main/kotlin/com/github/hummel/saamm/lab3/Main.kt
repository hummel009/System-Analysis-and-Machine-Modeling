package com.github.hummel.saamm.lab3

import java.io.File
import java.util.*

private const val PARTS_1_FOR_PRODUCT: Int = 3
private const val PARTS_2_FOR_PRODUCT: Int = 2

private const val PRODUCTS_FOR_PACKET: Int = 8
private const val PACKETS_FOR_STORAGE: Int = 3

val outputDir: File = mdIfNot("output")

fun main() {
	println("Which task: «1» or «2» or «3» or «4» or «5» or «6»")

	val input = readln().toInt()

	when (input) {
		1 -> {
			val statisticsArray = simulateRuns(100)

			researchDistributionGraph(statisticsArray)
			researchDistributionIdea(statisticsArray)
		}

		2 -> {
			val statisticsArray = simulateRuns(10)

			researchConfidenceInterval(statisticsArray)
		}

		3 -> {
			val statisticsArrayArray = Array(100) { i -> simulateRuns(i + 1) }

			researchAccuracy(statisticsArrayArray)
		}

		4 -> {
			val enoughRuns = 50
			val statisticsArrayArray = Array(10) { i ->
				simulateRuns(enoughRuns, generatorChance = (i + 1) * 0.05)
			}

			researchCorrelation(statisticsArrayArray)
		}

		5 -> {
			val statisticsArray = simulateRuns(1)

			researchTransitionPeriod(statisticsArray)

			val enoughRuns = 50
			val cutoff = 20000
			val statisticsArrayArray = arrayOf(
				simulateRuns(enoughRuns, exitTime = 100000.0), simulateRuns(enoughRuns, exitTime = 50000.0)
			)

			researchReductionPossibility(statisticsArrayArray, cutoff)
		}

		6 -> {
			val enoughRuns = 50
			val enoughTime = 50000
			val cutoff = 20000
			val statisticsArrayArray = arrayOf(
				simulateRuns(1, exitTime = cutoff + enoughRuns * (enoughTime - cutoff).toDouble()),
				simulateRuns(enoughRuns, exitTime = cutoff + (enoughTime - cutoff).toDouble())
			)

			researchContinousRunPossibility(statisticsArrayArray, cutoff)
		}
	}
}

fun simulateRuns(
	runs: Int, generatorChance: Double = 0.5, exitTime: Double = 48000.0
): Array<Statistics> {
	val statisticsArray = Array(runs) { Statistics() }
	val threadArray = statisticsArray.map { stat ->
		Thread {
			Factory(generatorChance, exitTime).apply {
				statistics = stat
			}.run()
		}
	}

	threadArray.forEach { it.start() }
	threadArray.forEach { it.join() }

	return statisticsArray
}

class Factory(
	private val generatorChance: Double, private val exitTime: Double
) {
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

		while (currentTime <= exitTime * 1000) {
			val task = queue.poll()
			currentTime = task.endTime

			when (task.taskType) {
				TaskType.GENERATOR -> {
					val time = (random.nextGaussian().coerceIn(-0.5, 0.5) + 0.5) * 1000 + 500

					if (random.nextDouble() <= generatorChance) {
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
						statistics.productTimes[currentTime / 1000] = currentTime / 1000 / storagePackets / 8
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
	val productTimes: MutableMap<Double, Double> = mutableMapOf()

	fun getProduceTime(): Double = duration / (storagePackets * 8)
}

data class Task(val endTime: Double, val taskType: TaskType)

enum class TaskType {
	GENERATOR, MACHINE_1, MACHINE_2, ASSEMBLER, TRANSPORTER, PACKER
}

fun mdIfNot(path: String): File {
	val folder = File(path)
	if (!folder.exists()) {
		folder.mkdirs()
	}
	return folder
}