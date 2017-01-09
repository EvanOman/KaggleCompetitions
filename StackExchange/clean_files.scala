import sys.process._

/* Call main */
main()

def main(): Unit = 
{
	val files = List("cooking", "crypto", "robotics", "biology", "travel", "diy", "test")
	val dir = "./dat/"
	val ext = ".csv"

	/* Removes newlines within quotes */
	val magicGawkScript = """gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' """

	files.foreach{ f =>
		val inFile = dir + f + ext
		val outFile = dir + f + "_clean" + ext
		magicGawkScript + inFile + " > " + outFile !

		/* replace """  with " "" and then "" with \" */
		val tmpFile = new File("/tmp/abc.txt") // Temporary File
		val w = new PrintWriter(tmpFile)
		Source.fromFile(outFile).getLines
			.map { x => if(x.contains("proxy")) s"# $x" else x }
			.foreach(x => w.println(x))
		w.close()
		tmpFile.renameTo(outFile)
	}
}

val outFile = "t.txt"
val tmpFile = new File("/tmp/abc.txt") // Temporary File
val w = new PrintWriter(tmpFile)
Source.fromFile(outFile).getLines.map { x => w.println(x+x) }
w.close()
tmpFile.renameTo(outFile)