package Lesson_1;


public class Main {

    public static void main(String[] args) {

        ReplacingArrayValue<String> arr1 = new ReplacingArrayValue<>("1","2","3","4");
        arr1.replacingArrayValue(1, 3); // a и b это намера элементов которые необходимо поменять местами

        ArrConvertToList<Integer> arr2 = new ArrConvertToList(1, 2, 3, 4);
        arr2.arrtolist();

        Apple apple = new Apple();

        Apple apple1 = new Apple();
        Apple apple2 = new Apple();
        Apple apple3 = new Apple();
        Apple apple4 = new Apple();

        Orange orange = new Orange();
        Orange orange1 = new Orange();
        Orange orange2 = new Orange();

        Box<Apple>appleBoxPure = new Box<>();

        Box<Orange> orangeBox =new Box<>(orange1,orange2);

        Box<Orange> orangeBox1 = new Box<>(orange, 2);

        Box<Apple> appleBox1 = new Box<>(apple, 3);
        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox1.compare(orangeBox1));

        Box<Apple> appleBox = new  Box<>(apple1,apple2);
        System.out.println(appleBox.getWeight());
        System.out.println(appleBox.compare(orangeBox));

        appleBox.add(apple3,apple4);
        System.out.println(appleBox.getWeight());
        System.out.println(appleBox.compare(orangeBox));

        System.out.println();

        appleBoxPure.info();
        appleBox1.info();
        appleBox1.pourItOver(appleBoxPure);
        appleBoxPure.info();
        appleBox1.info();
    }


}
