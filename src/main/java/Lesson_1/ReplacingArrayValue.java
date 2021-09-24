package Lesson_1;

public class ReplacingArrayValue<T> {
    private T[] obj;
    private T tempArr;

    public ReplacingArrayValue(T... obj) {
        this.obj = obj;
    }

    public void replacingArrayValue(int a, int b){

        for (int i = 0; i < this.obj.length; i++) {

            if (i == a)
                tempArr = this.obj[i];
            if (i == b){
                this.obj[1] = this.obj[i];
                this.obj[i] = tempArr;
            }
        }
        for (int i = 0; i < this.obj.length; i++) {
            System.out.println(this.obj[i]);
        }

    }

}
