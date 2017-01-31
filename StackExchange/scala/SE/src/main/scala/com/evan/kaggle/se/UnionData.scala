package com.evan.kaggle.se
import scala.math.random

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
/** Computes an approximation to pi */
object UnionData {
	def main(args: Array[String]) {
		val s = System.nanoTime
		val spark = SparkSession
			.builder
			.appName("Spark Pi")
			.getOrCreate()
		import spark.implicits._
		val files = List("cooking", "crypto", "robotics", "biology", "travel", "diy")
		val df_all = files.map(f => {
                        spark.read.option("header", "true").csv("../dat/"+f+"_clean.csv").withColumn("topic", lit(f))
                   }).reduce(_ unionAll _).filter(col("content") !== "")

		println("Saving!!")
		df_all.write.save("union.parquet")
		val e = System.nanoTime - s;
		println(s"That took ${e / 1e9d} seconds!!")
	}
}