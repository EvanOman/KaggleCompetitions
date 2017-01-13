```scala
import org.jsoup.Jsoup
```


><pre>
> import org.jsoup.Jsoup
> <pre>




```scala
val df = ss.createDataFrame(0 to 5 zip (10 to 15))
```


><pre>
> df: org.apache.spark.sql.DataFrame = [_1: int, _2: int]
> <pre>




```scala
df.show
```


><pre>
> +---+---+
> | _1| _2|
> +---+---+
> |  0| 10|
> |  1| 11|
> |  2| 12|
> |  3| 13|
> |  4| 14|
> |  5| 15|
> +---+---+
> <pre>




```scala
df.withColumn("_3", $"_1" + $"_2").show
```


><pre>
> +---+---+---+
> | _1| _2| _3|
> +---+---+---+
> |  0| 10| 10|
> |  1| 11| 12|
> |  2| 12| 14|
> |  3| 13| 16|
> |  4| 14| 18|
> |  5| 15| 20|
> +---+---+---+
> <pre>




```scala
val cleanHTML = udf((in: String) => Jsoup.parse(in).text())
```


><pre>
> cleanHTML: org.apache.spark.sql.expressions.UserDefinedFunction = UserDefinedFunction(<function1>,StringType,Some(List(StringType)))
> <pre>




```scala
df.withColumn("new", cleanHTML(lit("<a>this is an anchor</a>"))).show
```


><pre>
> +---+---+-----------------+
> | _1| _2|              new|
> +---+---+-----------------+
> |  0| 10|this is an anchor|
> |  1| 11|this is an anchor|
> |  2| 12|this is an anchor|
> |  3| 13|this is an anchor|
> |  4| 14|this is an anchor|
> |  5| 15|this is an anchor|
> +---+---+-----------------+
> <pre>




```scala
val data = ss.sparkContext.textFile("t.txt")
data.foreach{println}
```


><pre>
> line1
> line3
> line2
> data: org.apache.spark.rdd.RDD[String] = t.txt MapPartitionsRDD[5] at textFile at <console>:68
> <pre>