public class TestPlanet {
    public static void main(String[] args) {
        Planet a = new Planet(1.0,2.0,3.0,4.0,50,null);
        Planet b = new Planet(2.0,4.0,8.0,16.0,20,null);
        System.out.println(a.calcForceExertedBy(b));
    }
}
