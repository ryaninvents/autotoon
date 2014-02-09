import org.opencv.core._

object Test{
  def main(args: Array[String]) = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    val m = Mat.eye(3,3,CvType.CV_8UC1)
    var i = 0
    var j = 0
    for(i <- 0 to 2){
      print("[")
      for(j <- 0 to 2){
        print(m.get(i,j)(0)+" ")
      }
      println("]")
    }
  }
}