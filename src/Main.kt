import java.awt.Color
import java.awt.Graphics
import java.lang.Math.sqrt
import javax.swing.JFrame


const val SIZEX = 600;
const val SIZEY = 600;
val BACKGROUND_COLOR = Color.BLACK;

class Canvas:JFrame(){
    init{
        setTitle("myFrame")
        setSize(SIZEX, SIZEY)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true//now frame will be visible, by default not visible
    }
    var array = Array<Array<Color?>>(SIZEY, {arrayOfNulls<Color?>(SIZEX)})
    override fun paint(g:Graphics?){
        for(i in 0..SIZEY-1) {
            for(j in 0..SIZEX-1) {
                g?.setColor(array[i][j])
                g?.drawRect(j, i, 1, 1);
            }
        }
    }
    fun putPixel(x:Int,y:Int,color: Color){
        array[y][x]=color
    }
}
var O = Vector(0.0,0.0,0.0)
var spheres = listOf(Sphere(Vector(0.0,-1.0,3.0),1.0,Color.RED),Sphere(Vector(2.0,0.0,4.0),1.0,Color.BLUE),Sphere(Vector(-2.0,0.0,4.0),1.0,Color.GREEN))
var Vh = 1 //Height of view window

var Vw = 1 //Width of view window

var dist = 1.0

fun canvasToViewPort(x: Int, y: Int): Vector {
    return Vector(x.toDouble()/SIZEX ,y.toDouble()/SIZEY, dist)
}
data class TPair(var t1:Int,var t2:Int);

fun traceRay(O: Vector, D: Vector, t_min: Int, t_max: Int): Color {
    var closest_T = Int.MAX_VALUE;
    var closest_sphere:Sphere? = null
    for(sphere in spheres){
        var t = intersectRaySphere(O,D,sphere);
        if(t.t1>t_min && t.t1<t_max && t.t1<closest_T){
            closest_T=t.t1
            closest_sphere=sphere
        }
        if(t.t2>t_min && t.t2<t_max && t.t2<closest_T){
            closest_T=t.t2
            closest_sphere=sphere
        }
    }
    if(closest_sphere==null){
        return BACKGROUND_COLOR;
    }
    return closest_sphere.color;
}

fun intersectRaySphere(O: Vector, D: Vector, sphere: Sphere): TPair {
    var C = sphere.center;
    var r = sphere.radius;
    var OC = vctrSubs(O,C);
    var k1 = vctrDot(D,D)
    var k2 = 2*vctrDot(OC,D);
    var k3 = vctrDot(OC,OC) - r*r;
    var discriminant = k2*k2 - 4*k1*k3;
    if(discriminant<0)
        return TPair(Int.MAX_VALUE, Int.MAX_VALUE);
    return TPair((-k2+sqrt(discriminant)/(2*k1)).toInt(), (-k2-sqrt(discriminant)/(2*k1)).toInt())
}

fun main() {
    val f = Canvas();

    for(i in -SIZEY/2 until SIZEY/2){
        for(j in -SIZEX/2 until SIZEX/2){
            var D = canvasToViewPort(j,i);
            var clr = traceRay(O,D,1, Int.MAX_VALUE);
            f.putPixel(SIZEX/2+j,SIZEY/2-i-1,clr)
        }
    }
    f.paint(null);
}

