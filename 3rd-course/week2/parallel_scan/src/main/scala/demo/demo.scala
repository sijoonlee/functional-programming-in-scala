package demo

import common.common.parallel

object demo {


  // iterative
  def scanLeftIter[A](input:Array[A], a0: A, f:(A,A)=>A, output:Array[A]):Unit = {
    output(0) = a0
    var a = a0
    var i = 0
    while(i<input.length){
      a = f(a, input(i)) // new a is dependent on the previous a ==> can't be parallelized
      i += 1
      output(i) = a
    }
  }

  // Using Reduce/Map - can be parallelized
  // map always gives as many elements as the input
  // need to compute the last element
  def reduceSeg[A](input: Array[A], left:Int, right:Int, a0:A, f:(A,A)=>A):A = ???

  def mapSeg[A,B](input: Array[A], left:Int, right:Int, fi:(Int,A) => B, output:Array[B]): Unit = ???

  def scanLeftSeg[A](input:Array[A], a0:A, f:(A,A)=>A, output:Array[A]) = {
    val fi = { (i:Int, v:A)=> reduceSeg(input, 0, i, a0, f)}
    mapSeg(input, 0, input.length, fi, output)
    val last = input.length - 1
    output(last+1) = f(output(last), input(last))
  }


  // Tree definitions
  sealed abstract class Tree[A]
  case class Leaf[A](a:A) extends Tree[A]
  case class Node[A](l:Tree[A], r:Tree[A]) extends Tree[A]

  // Tree storing intermediate values
  sealed abstract class TreeRes[A] { val res:A}
  case class LeafRes[A](override val res:A) extends TreeRes[A]
  case class NodeRes[A](l: TreeRes[A], override val res:A, r:TreeRes[A]) extends TreeRes[A]

  // transforming reduce function
  def reduceRes[A](t:Tree[A], f:(A,A)=>A):TreeRes[A] = t match {
    case Leaf(v) => LeafRes(v)
    case Node(l,r) => {
      val (tL, tR) = (reduceRes(l, f), reduceRes(r,f))
      NodeRes(tL, f(tL.res, tR.res), tR)
    }
  }

  // parallel reduce
  def upsweep[A](t:Tree[A], f:(A,A)=>A):TreeRes[A] = t match {
    case Leaf(v) => LeafRes(v)
    case Node(l, r) => {
      val (tL, tR) = parallel(upsweep(l, f), upsweep(r,f))
      NodeRes(tL, f(tL.res, tR.res), tR)
    }
  }

  // create collection
  //     62
  //  4     58
  // 1  3  8  50
  // a0 = 100
  // final collection = 100, 101, 104, 112, 162
  // for example, for the sub-tree of LeafRes(1), LeafRes(3)
  // downsweep[A](l, acc, f): 100 + 1
  // downsweep[A](r, f(acc, l.res), f): 101 + 3

  def downsweep[A](t:TreeRes[A], acc:A, f:(A,A)=>A):Tree[A] = t match {
    case LeafRes(a) => Leaf(f(acc, a))
    case NodeRes(l,_,r) => {
      val (tL, tR) = parallel(downsweep[A](l, acc, f),  // store acc
                              downsweep[A](r, f(acc, l.res), f)) // accumulate l.res to acc
                              // note that l.res is already calculated, so we can use parallel
      Node(tL, tR)
    }
  }

  def prepend[A](x:A, t:Tree[A]):Tree[A] = t match {
    case Leaf(v) => Node(Leaf(x), Leaf(v))
    case Node(l, r) => Node(prepend(x,l),r)
  }

  def scanLeftTree[A](t:Tree[A], a0:A, f:(A,A)=>A) :Tree[A] = {
    val tRes = upsweep(t, f)
    val scan = downsweep(tRes, a0, f)
    prepend(a0, scan)
  }


  // Trees that have Arrays in leaves instead of single elements
  // Each Leaf has the reference to an Array
  sealed abstract class TreeResA[A] {val res: A}
  case class LeafA[A](from:Int, to:Int, override val res:A) extends TreeResA[A]
  case class NodeA[A](l:TreeResA[A], override val res:A, r:TreeResA[A]) extends TreeResA[A]
  val threshold = 10

  def reduceSegA[A](inp: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A):A = {
    var a = a0
    var i = left
    while (i<right){
      a = f(a, inp(i))
      i += 1
    }
    a
  }

  def upsweepA[A](inp:Array[A], from:Int, to:Int, f:(A,A)=>A):TreeResA[A] = {
    if(to-from < threshold)
      LeafA(from, to, reduceSegA(inp, from+1, to, inp(from), f))
    else {
      val mid = from + (to-from)/2
      val (tL, tR) = parallel(upsweepA(inp, from, mid, f),
                              upsweepA(inp, mid, to, f))
      NodeA(tL, f(tL.res, tR.res), tR)
    }

  }

  def scanLeftSegA[A](inp: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A, out: Array[A]) = {
      if(left<right){
        var i = left
        var a = a0
        while(i<right){
          a = f(a, inp(i))
          i += 1
          out(i) = a // start from out(1)
        }
      }
  }

  def downsweepA[A](inp:Array[A], a0:A, f:(A,A)=>A, t:TreeResA[A], out:Array[A]):Unit = t match {
    case LeafA(from, to, res) => scanLeftSegA(inp, from, to, a0, f, out)
    case NodeA(l, _, r) => {
      val (_,_) = parallel(downsweepA(inp, a0, f, l, out), downsweepA(inp, f(a0, l.res), f, r, out))
    }
  }

  def scanLeftA[A](inp:Array[A], a0:A, f:(A,A)=>A, out:Array[A]) = {
    val t = upsweepA(inp, 0, inp.length, f)
    downsweepA(inp, a0, f, t, out)
    out(0) = a0 // prefend a0
  }



  def main(args: Array[String]): Unit = {

    // map
    println(List(1,3,8).map(x=>x*x)) // List(1, 9, 64)

    // fold
    println(List(1,3,8).fold(100)((s,x) => s + x)) // 112

    // scanLeft
    print(List(1,3,8).scanLeft(100)((s,x)=> s+x)) // List(100, 101, 104, 112)

    // using tree
    val t1 = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
    val plus = (x:Int, y:Int) => x + y
    println(reduceRes(t1, plus)) // NodeRes(NodeRes(LeafRes(1),4,LeafRes(3)),62,NodeRes(LeafRes(8),58,LeafRes(50)))


  }
}
