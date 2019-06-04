package ec.app.tutorial5;

import ec.app.tutorial4.Task;

import java.util.ArrayList;

public class PSOTest {

    private ArrayList<Task> test_task;
    private ArrayList<Task> generatedTask = new ArrayList<>();
    private int level_created = 0;
    private  char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private Task[] parent_level;

    public PSOTest(){
        java.util.Random seed = new java.util.Random();
        seed.setSeed(0);// seed the generator
        ArrayList<Task> test_task =new ArrayList<>();
        this.test_task =test_task;
        testDataGenerator(6);
        PSO psoTest = new PSO();
    }

    private void testDataGenerator(int level) {
        for(int index= 0; index <level; index++){
            if(index==0){
                Task t0 = new Task();
                t0.setTask_size((long) (Math.random() *1000));
                t0.setId("a0");
                this.level_created++;
                this.test_task.add(t0);
                this.parent_level = new Task[1];
                parent_level[0] = t0;
            }
            else {
                int children_needed = 0;
//                children_needed = (int) Math.floor(this.parent_level.length + Math.random()*2*this.parent_level.length);
//                for()



            }
        }


    }

    private Boolean addRandomChildren(Task parent,int k){
        for(int index =0; index < k; k++){
            Task t = new Task();
            t.setTask_size((long) (Math.random()*1000));
            t.parent_id.add(parent.id);
            t.setId( alphabet[level_created]+ String.valueOf(k)+parent.id);//make sure it wont create tasks with the same id
        }



        return true;
    }

}
