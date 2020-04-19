import java.awt.Color
import java.awt.Graphics
import java.lang.Math.sqrt
import javax.swing.JFrame


const val SIZEX = 600;
const val SIZEY = 600;
val BACKGROUND_COLOR = Color.WHITE;

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
var spheres = listOf(
        Sphere(Vector(0.0,-1.0,3.0),1.0,Color.RED),
        Sphere(Vector(2.0,0.0,4.0),1.0,Color.BLUE),
        Sphere(Vector(-2.0,0.0,4.0),1.0,Color.GREEN),
        Sphere(Vector(0.0,-5001.0,0.0),5000.0,Color.YELLOW))
var lights = listOf(
        Light(0,0.2,Vector(0.0,0.0,0.0)),
        Light(1,0.6,Vector(2.0,1.0,0.0)),
        Light(2,0.2,Vector(1.0,4.0,4.0)))

var Vh = 1 //Height of view window

var Vw = 1 //Width of view window

var dist = 1.0

fun canvasToViewPort(x: Int, y: Int): Vector {
    return Vector(x.toDouble()/SIZEX ,y.toDouble()/SIZEY, dist)
}
data class TPair(var t1:Double,var t2:Double);
var debugt = TPair(1.0,1.0);

fun traceRay(O: Vector, D: Vector, t_min: Int, t_max: Int): Color {
    var closest_T = Int.MAX_VALUE.toDouble();
    var closest_sphere:Sphere? = null
    for(sphere in spheres){
        var t = intersectRaySphere(O,D,sphere);
        debugt=t;
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
    var P = vctrSum(O,vctrScale(D,closest_T.toDouble()))
    var N = vctrSubs(P,closest_sphere.center)
    N = vctrScale(N,1/vctrLen(N))
    var colorFactor = computeLightning(P,N)
    var red = (closest_sphere.color.red*colorFactor).toInt();
    if(red>255)
        red = 255
    var green = (closest_sphere.color.green*colorFactor).toInt();
    if(green>255)
        green = 255
    var blue = (closest_sphere.color.blue*colorFactor).toInt();
    if(blue>255)
        blue = 255
    return Color(red,green,blue)
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
        return TPair(Int.MAX_VALUE.toDouble(), Int.MAX_VALUE.toDouble());
    return TPair(((k2*(-1)+sqrt(discriminant))/(2*k1)), ((k2*(-1)-sqrt(discriminant))/(2*k1)))
}
fun computeLightning(P:Vector,N:Vector):Double{
    var i = 0.0;
    var L:Vector
    for(light in lights){
        if(light.type==0){
            i += light.intensity
        }else{
            if(light.type==1){
                L = vctrSubs(light.dirpos,P)
            }else {
                L = light.dirpos
            }
            var nDotL=vctrDot(N,L);
            if(nDotL>0)
                i += (light.intensity*nDotL)/(vctrLen(N)*vctrLen(L))
        }
    }
    return i
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

