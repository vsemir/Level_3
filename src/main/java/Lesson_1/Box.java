package Lesson_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Box<T extends Fruit> {

    private ArrayList<T> list;

    public Box(T... fruits) {
        this.list = new ArrayList<>(Arrays.asList(fruits));
    }

    public Box(T fruit, int count){
        this.list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(fruit);
        }

    }

    public float getWeight() {
        float weight = 0.0f;
        for (Fruit f : list) {
            weight += f.getWeight();
        }
        return weight;
    }

    public void add(T... fruit){
        list.addAll(List.of(fruit));
    }

    public void info (){
        System.out.println(list.size());
    }

    public boolean compare(Box<?> o) {
        return  Math.abs(this.getWeight() - o.getWeight()) < 0.001;
    }

    public void pourItOver(Box <T> anotherBox){
        anotherBox.list.addAll(list);
        list.clear();
    }
}
