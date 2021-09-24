package Lesson_1;

import java.util.ArrayList;

public class ArrConvertToList<T> {
    private T[] obj;

    public ArrConvertToList(T... obj) {
        this.obj = obj;
    }

    public  void arrtolist(){

        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < obj.length; i++) {
            arrayList.add(obj[i]);
        }
        System.out.println(arrayList);

    }

}
