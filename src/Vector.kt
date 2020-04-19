import java.lang.Math.sqrt

class Vector(var x:Double, var y:Double, var z:Double)


fun vctrSum(a:Vector,b:Vector):Vector{
    return Vector(a.x+b.x,a.y+b.y,a.z+b.z)
}
fun vctrScale(a:Vector, b:Double):Vector{
    return Vector(a.x*b,a.y*b,a.z*b);
}
fun vctrDot(a:Vector,b:Vector):Double{
    return a.x*b.x+a.y*b.y+a.z*b.z;
}
fun vctrSubs(a:Vector,b:Vector):Vector{
    return Vector(a.x-b.x,a.y-b.y,a.z-b.z)
}
fun vctrLen(a:Vector):Double{
    return kotlin.math.sqrt(vctrDot(a, a));
}