import sys.process._
import scala.io.Source
import java.io._

/* Call main */
main()

def main(): Unit = 
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
			var line_clean = line.replaceAll("\"\"\"\"", "\"\" \"\"").
								  replaceAll("\"\"\"(,|$)", "\"\" \"$1").
								  replaceAll("\"\"\"", "\" \"\"").
								  replaceAll("\"\"", "'")
			w.println(line_clean)
		})
		w.close()

		/* rename file */
		tmpFile.renameTo(new File(outFile))
	}
}