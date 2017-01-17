import sys.process._
import scala.io.Source
import java.io._
import org.jsoup.Jsoup

object CleanFiles
{
	def main(args: Array[String]): Unit = 
	{
		println("CLEANING CSV FILES")
		
		run_cleaning()
	}

	def run_cleaning(): Unit = 
	{
		val files = List("cooking", "crypto", "robotics", "biology", "travel", "diy", "test")
		val dir = "./dat/"
		val ext = ".csv"

		/* Script to remove newlines within quotes */
		val newLineSc = "./rmNewLine.sh "

		files.foreach{ f =>
			/* gen in/out file names */
			val inFile = dir + f + ext
			val outFile = dir + f + "_clean" + ext

			println(s"Cleaning file $inFile and outputing file $outFile")

			/* Run gawk script to remove all newlines within quotes */
			(newLineSc + inFile + " " + outFile) !

			val tmpFile = new File("./dat/tmp") // Temporary File
			val w = new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)))
			/* replace """  with " "" and then "" with \" */
			Source.fromFile(outFile).getLines.foreach(line => {
				/* Replace triple quotes at the end of the line first, the beginning */
				var line_clean = line.replaceAll("\"{5,}", ""). // Get rid of all 5 quotes plus, because they are dumb
									  replaceAll("\"\"\"\"", "\"\" \"\""). // Replace all """" with "" "" (empty double quote)
									  replaceAll("\"\"\"(,|$)", "\"\" \"$1"). // Replace all """ with " "" (quote at the beginning of post)
									  replaceAll("\"\"\"", "\" \"\""). // Replace all """ with "" " (quote at the beginning of post)
									  replaceAll("\"\"", "'"). // Replace all inner quotes with an apostrophe 
									  replaceAll("&quot;", "'") // Replace html quote with an apostrophe
				val doc = Jsoup.parse(line_clean)
				/* TODO: This removes inline code as well, may cause issues */
				doc.select("code, rm, img").remove()
				w.println(doc.text())
			})
			w.close()

			/* rename file */
			tmpFile.renameTo(new File(outFile))
		}
	}
}

