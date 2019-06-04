package ec.app.tutorial5;
//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
import ec.gp.GPIndividual;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PSO {
    private  ArrayList<String> gbest_results;
    public Queue<Task>  queue = new LinkedList<>();
    public int seed;
    //    private double[][] ETC;
    private double[][] Pbest;//
    private double[] Gbest;// set with the index of the best particle
    private int generation = 0;
    private ArrayList<Task> task_list = new ArrayList<>();
    private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
    private int Swarm_Size = 20; //without specification the swarm size is 20
//    private double c1 = 2.05;
//    private double c2 = 2.05;
    private double c1 = 0.01;
    private double c2 = 0.05;
    private double w = 0.03;//0.5314;
    private int MAX_ITER  = 60;//should be 500 now is testing
    //    private int numberOfVM;
    private ArrayList<Particle> list_particle = new ArrayList<>();
    //    private int[][] MAP;//hold the mapping schedule (task - VM )
    public boolean best_solution_boolean = false;
    public ArrayList<Object> updateVals = new ArrayList<>(); // returned arraylist which is used to calculate the fitness
    public int[] final_best_solution;//store the best solution in each generation
    public double Min_fiteness = Double.MAX_VALUE; // initial with the maximum value
    private double gbest_fitness = -Double.MAX_VALUE;
    private int[] bestSolution;
    String resultpath = "C:\\Users\\vince\\Desktop\\123\\checker.txt";//checker
    ArrayList<String> checker = new ArrayList<>();
//    ArrayList<String> check_solution = new ArrayList<>();
    public String final_result_path = "C:\\Users\\vince\\Desktop\\123\\result.txt";
    public String CSV_path ="C:\\Users\\vince\\Desktop\\123\\ces.csv";
    public FileWriter write_csv;
    public ArrayList<String> csv_recorder = new ArrayList<>();
    public Particle current_best_particle;

//    public Particle bestParticle = new Particle(this.task_list,this.ls_vms,-1);



    //Constructor for PSO which holds particles -(represents solution)
    public PSO(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int seed){//j is useless
        java.util.Random seedGenerator = new java.util.Random();
        this.seed = seed;
        seedGenerator.setSeed((long) seed);
        this.ls_vms = ls_vms;
        this.task_list = taskList;
        int generation = 0 ;
        this.generation = generation;
//        this.VEC = new double[this.task_list.size()];
//        this.numberOfVM = this.ls_vms.size();
//        this.MAP =new int[this.task_list.size()][this.task_list.size()];
        this.Pbest = new double[this.Swarm_Size][this.task_list.size()];
        this.final_best_solution = new int[this.task_list.size()];
        this.Gbest = new double[this.task_list.size()];
        this.bestSolution = new int[this.task_list.size()];
        this.current_best_particle= new Particle(taskList,ls_vms,-1);
//        try {
//            this.writter =new FileWriter("/home/yuyong1/Desktop/Summer_project/result/final.txt",true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            this.write_csv =  new FileWriter(CSV_path+"results"+this.seed+".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gbest_results = new ArrayList<String>();
        Initialization();
    }

    public PSO() {// constructor built here to let the particle class extend
    }

    @SuppressWarnings("Duplicates")
    //  intialise POP and VEC this two arrays with this function with random value varies from 0 to 1 based on uniform distribution
    public void Initialization() {
//        System.out.println("Printing hash code for old list and the first task inside of it:" + " 1  :" + this.task_list.hashCode() + " 2 :" + this.task_list.get(0).hashCode()  );

        for (int i=0; i < this.Swarm_Size; i++){
//            ArrayList<Task> clone_task = (ArrayList<Task>) Utility.DeepClone_Seializable(this.task_list);
//            if(this.task_list.get(0).getStart_time()==clone_task.get(0).getStart_time()) System.out.println("cloned correctly ");
//            System.out.println("Printing the new Hash Codes :" + " 1 : " + clone_task.hashCode() + " 2 : " + clone_task.get(0).hashCode());
            ArrayList<Task> clone_task = new ArrayList<>();
//            ArrayList<VirtualMachine> clone_vm = new ArrayList<>();
            clone_task = Utility.TaskListCloning(task_list);
//            clone_vm   = Utility.vmCloning(ls_vms);
            Particle p = new Particle(clone_task,this.ls_vms,-1);
            this.list_particle.add(p);
        }
//        for (int i = 0; i < this.Pbest.length; i++){
//            for(int j=0; j < this.Pbest[0].length; j++){
//                Pbest[i][j] = Double.MAX_VALUE;
//            }
//        }
        return;
    }

    public Map<String, ArrayList> taskMapping(int j)  {
        Map<String,ArrayList> returning = new HashMap<>();
//            ArrayList<Object> updateVal = new ArrayList<>();//the final Mapping solution
        returning = Main_Procedure();//solutions with the updated task list
        try {
            writeTofileChecker(this.resultpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.write_csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gbest_fitness = Double.MAX_VALUE;
        this.Gbest = new double[this.task_list.size()];
        this.reset();
        return returning;
    }

    private void reset() {
        this.list_particle.clear();
        this.Initialization();
    }


    public Map<String, ArrayList> Main_Procedure(){
        double time_best = Double.MAX_VALUE;
        double cost_best = Double.MAX_VALUE;
        double fitness = Double.MAX_VALUE;
        ArrayList<Task> updated_task = new ArrayList<>();
        ArrayList<Object> update  = new ArrayList<>();
        for(int i =0 ; i < list_particle.size(); i++){
            list_particle.get(i).setFitness(CalFitness(list_particle.get(i)));
            if(list_particle.get(i).getFitness() < fitness){
                fitness = list_particle.get(i).fitness;
            }
        }
        this.gbest_fitness = fitness;
        int best_index =-1;//represent the index of the current best solution
        for(int index = 0; index < Swarm_Size; index++) {
            CalFitness(list_particle.get(index));
        }
        int index_of_best_particle = -1;
        for(int i=0; i < Swarm_Size; i++){
            if(list_particle.get(i).fitness <  Min_fiteness){
                Min_fiteness = list_particle.get(i).fitness;
                index_of_best_particle= i ;
            }
            //checker for solution
//            for(int j = 0; j < this.task_list.size(); j++){
////                this.check_solution.add("Particle :" + i + "The solution of the initial generation is : " + list_particle.get(i).Solution[j] + "    the number of VM : "+ ls_vms.size() + " number of job : " + this.task_list.size());
//                this.checker.add("" + this.gbest_fitness);
//            }
            this.checker.add("The gbest now is : "+ this.gbest_fitness +" this is run : " + seed);
        }
        if(index_of_best_particle == -1 ) index_of_best_particle = this.list_particle.size()-1;
        Gbest = list_particle.get(index_of_best_particle).POP;
        try {
            System.out.println("Writing to CSV");
            this.write_csv.append(String.valueOf(list_particle.get(index_of_best_particle).time));
            this.write_csv.append(",");
            this.write_csv.append(String.valueOf(list_particle.get(index_of_best_particle).cost));
            this.write_csv.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int Ietr = 0 ;

        while(Ietr < MAX_ITER){
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + Ietr);

            for(int k=0; k < Swarm_Size; k++){
                Particle p= list_particle.get(k);
                //checking in file
                this.checker.add("Particle :" + k + " Iteration : " + Ietr + " Current Fitness :" + p.fitness + "   Pbest now is :  "+ p.pbest_fitness + "    Gbest now is :" + this.gbest_fitness);
                if(k==Swarm_Size-1 && Ietr == MAX_ITER-1){
                    this.checker.add("**************************    One     Iteration   Finished     *****************************");
                    System.out.println("/////////////----------one Iteration Finished//////---");
                }
                p.updateVelocity(Ietr,this.Gbest);
                p.MapTaskToVM(Ietr);
//                for(int j = 0; j < this.task_list.size(); j++){
//                    this.check_solution.add("Particle :" + k + "The solution of the initial generation is : " + list_particle.get(k).Solution[j]);
//                }
                CalFitness(p);//calculate the fitness of this current particle
                if(p.fitness < p.pbest_fitness){//update the local best
//                    System.out.println("updating local best");
                    p.setPbest_fitness(p.fitness,p.getSolution());
                    p.setLocal_best_index(Ietr);
                }
                if(p.pbest_fitness < this.gbest_fitness){//update global best
                    this.current_best_particle = p;
                    System.out.println("updating global best");
                    this.gbest_fitness = p.pbest_fitness;
                    this.Gbest  = p.POP;//g_best = Pbest k (update the coefficient of the current best solution)
                    this.bestSolution = p.getSolution().clone();
                    best_index = k;
                    ArrayList<Task> updating = new ArrayList<>();
                    for(Task t : p.task_list){
                        updating.add(t.clone());
                    }
                    updated_task = updating;
//                    this.bestParticle = p;
                }


            }

            Ietr++;
//            makeSpan = bestParticle.fitness;

            System.out.println("Gbest fitness in Generation: "+ Ietr+" is : " + this.gbest_fitness);
            try {
                this.write_csv.append(String.valueOf(this.current_best_particle.time));
                this.write_csv.append(",");
                this.write_csv.append(String.valueOf(this.current_best_particle.cost));
                this.write_csv.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.gbest_results.add(String.valueOf(this.gbest_fitness));

//            if(Ietr == MAX_ITER) System.out.println("this is the best solution" + makeSpan);
//            System.out.println("Now we are in the "+ Ietr +" Iteration and our makespan of the current best is: " + makeSpan );
//            System.out.println("Gbest Makespan : "+ gbest_fitness);
        }

        if(Ietr == MAX_ITER){
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
            for(int i=0; i < task_list.size(); i++){
                update.add(task_list.get(i));
                update.add(ls_vms.get( bestSolution[i]));
            }
//            try {
//                writefinalResult(final_result_path);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        Map<String,ArrayList> returning = new HashMap<>();
        returning.put("solution_updated",update);
        returning.put("updated_tasks",updated_task);

        return returning;
    }

    public double CalFitness(Particle p) {
        return p.CalculateFitness(p.Solution);
    }
    public void CSV_Writter(String filePath){


    }
    // getter and setters
    public void setTask_list(ArrayList<Task> task_list) {
        this.task_list = task_list;
    }
    public void setLs_vms(ArrayList<VirtualMachine> ls_vms) {
        this.ls_vms = ls_vms;
    }
    public void setGeneration(int generation) {
        this.generation = generation;
    }
    public void setC1(double c1) {
        this.c1 = c1;
    }
    public void setC2(double c2) {
        this.c2 = c2;
    }
    public void setW(double w) {
        this.w = w;
    }
    public void set_Swarm_Size(int num){
        this.Swarm_Size = num;
    }
    public ArrayList<Task> getTask_list() {
        return task_list;
    }
    public ArrayList<VirtualMachine> getLs_vms() {
        return ls_vms;
    }
    public int getGeneration() {
        return generation;
    }
    public int getSwarm_Size() {
        return Swarm_Size;
    }
    public double getC1() {
        return c1;
    }
    public double getC2() {
        return c2;
    }
    public double getW() {
        return w;
    }



    //checker
    public void writeTofileChecker(String filePath)throws IOException {


        FileWriter fileWriter = new FileWriter(filePath);
        if (filePath.contains("checker")) {
            if (!this.checker.isEmpty()) {
                for (int index = 0; index < this.checker.size(); index++) {
                    fileWriter.write(this.checker.get(index));
                    fileWriter.write("# ");
                    fileWriter.write("\n");

                }

            }
        }
//        else{
//            if (!this.check_solution.isEmpty()) {
//                for (int index = 0; index < this.check_solution.size(); index++) {
//                    fileWriter.write(this.check_solution.get(index));
//                    fileWriter.write("# ");
//                    fileWriter.write("\n");
//
//                }
//
//            }
//
//        }

        fileWriter.close();

    }


    class Particle extends PSO implements Cloneable{
        private ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
        private ArrayList<Task> task_list = new ArrayList<>();
        private int[] Solution;// this array store the current solution
        private double[] POP;// the array used to find the solution
        private double[] VEC;// the constant array used to mapping task to Vm
        //        private double[] Pbest_k;
        private double fitness = Double.MAX_VALUE;//the current fitness
        private double pbest_fitness;//store the local best fitness
        private int num_VMS;
        public double Velocity[];// the array store the velocity
        //        private double S[];
        private int[] bestSolutionSoFar;
        private int local_best_index;// the number of iteration when the best solution occurs
        private double[]local_best_pop;//the best coefficients we got(it was used to update the position)
        public double time;
        public double cost;

        public Particle(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){
            super();
            this.Solution = new int[taskList.size()];
            this.task_list = taskList;
            this.ls_vms = ls_vms;
            this.POP = new double[this.task_list.size()];
            this.VEC = new double[this.task_list.size()];
//            this.Pbest_k = new double[this.task_list.size()];
            this.num_VMS = ls_vms.size();
            this.Velocity = new double[this.task_list.size()];
            this.pbest_fitness = Double.MAX_VALUE;

            this.bestSolutionSoFar = new int[this.task_list.size()];
            Initialise_Particle();
            this.local_best_pop = POP;
            MapTaskToVM(0);
//            System.out.println("new Particle initialised");
        }


        //        1 Initialise
        public void Initialise_Particle() {
            for (int i = 0; i < this.task_list.size(); i++) {
                POP[i] = Math.random();
            }
            for (int j = 0; j < POP.length; j++) {
                int tmp = j * num_VMS;
                if (tmp > num_VMS) {
                    while (tmp > num_VMS) {
                        tmp = (int) Math.floor(tmp / num_VMS);
                    }
                }
                VEC[j] = tmp;
            }
            this.POP = POP;//update two array(POP and VEC ) hold in field
            this.VEC = VEC;
            for (int index = 0; index < Solution.length; index++) {
                Solution[index] = 0;//initialise the solution matrix
            }

        }

        //      2. Mapping
        public void MapTaskToVM(int iter ) {
            int[] solution = new int[this.task_list.size()];
            for (int i = 0; i < POP.length; i++) {
//                    solution[i] = (int) Math.floor(this.POP[i] * this.VEC[i]);// should be ceil in the paper but bound problem occured not sure what should do here
//                solution[i] =(int) Math.ceil(this.POP[i]*this.VEC[i]);
                solution[i] = (int) Math.ceil(this.POP[i] * (this.ls_vms.size()-1));
                if(solution[i]<0 ){solution[i] = 0;
                    System.out.println("out of the vm array negative");}
                if(solution[i]>(ls_vms.size()-1)){solution[i]=(ls_vms.size()-1);
                    System.out.println("out of the vm array positive");}
            }
            this.Solution = solution;
            return;
        }

        public ArrayList<VirtualMachine> getLs_vms() {
            return ls_vms;
        }
        public void setLs_vms(ArrayList<VirtualMachine> ls_vms) {
            this.ls_vms = ls_vms;
        }
        public ArrayList<Task> getTask_list() {
            return task_list;
        }
        public void setTask_list(ArrayList<Task> task_list) {
            this.task_list = task_list;
        }
        public int[] getSolution() {
            return Solution;
        }
        public void setSolution(int[] solution) {
            Solution = solution;
        }
        public double getFitness() {
            return fitness;
        }
        public void setFitness(double fitness) {
            this.fitness = fitness;
        }


        @SuppressWarnings("Duplicates")
        public void updateVelocity(int iter,double[] Gbest) {
            double r1 =0;
            double r2 =0;
            while(r1 == 0) {
                r1 = Math.random();
            }
            while(r2 == 0){
                r2 = Math.random();
            }
            for(int i =0 ; i < task_list.size(); i++){
                Velocity[i] = w * Velocity[i] + c1 * r1 *(this.local_best_pop[i] - this.POP[i]) + c2 * r2 * (Gbest[i] - this.POP[i]);
                this.POP[i] = this.POP[i] + Velocity[i];
                if(POP[i] < 0 ) {POP[i] = 0.001; }
                if(POP[i] > 1 ) {POP[i] = 0.999;
                }
            }
        }

        public double getPbest_fitness() {
            return pbest_fitness;
        }
        public void setPbest_fitness(double pbest_fitness,int[] BestSolutionSoFar) {
            this.pbest_fitness = pbest_fitness;
            this.bestSolutionSoFar = BestSolutionSoFar.clone();
        }
        public double CalculateFitness(int[] Solution) {
            resetTaskStartFinishTime(Solution);
            setTaskFinishTime(Solution);
            double total_time = 0;
            total_time += Utility.getTasksMaxSpan(this.task_list);
            double total_cost = 0;
            double totalRFT=0;
            for(VirtualMachine vm : ls_vms){
                totalRFT = Utility.getTasksMaxSpan(vm.getPriority_queue());
                total_cost = (double) totalRFT*vm.getUnit_cost_vm();
            }
            this.fitness = total_cost*0.1 + total_time*0.9;
            if(this.fitness<this.pbest_fitness) {
//                System.out.println("Pbest updating");
                this.time = total_time;
                this.cost = total_cost;
                this.local_best_pop = this.POP.clone();
            }
            return total_time;
        }

        private void setTaskFinishTime(int[] solution) {
            for(int index =0; index < task_list.size(); index++){
                Task t = task_list.get(index);
                ArrayList<Task> parentTasks = new ArrayList<>();
                parentTasks  = Utility.getParentTasksById(task_list,t.getId());
                t.setAllocation_time(Utility.getMaxFinishTime(parentTasks));
                t.setExe_time((double) t.getTask_size()/ls_vms.get(Solution[index]).getVelocity());

                double preFinishTime = Utility.getMaxFinishTime(ls_vms.get(Solution[index]).getPriority_queue());
                t.setStart_time(Utility.getMaxStartTime(preFinishTime,t.getAllocation_time()));
                t.setRelative_finish_time();
                t.setFinish_time();
                ls_vms.get(Solution[index]).setPriority_queue(t);
            }
        }
        private void resetTaskStartFinishTime(int[] solution) {
            for(Task t : task_list){
                t.waiting_time = 0;
                t.setStart_time(0);
                t.finish_time=0;
                t.setFinish_time();
            }
        }
        public Particle clone(){
            try{
                Particle pObj =  (Particle)(super.clone());
                return pObj;
            }
            catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return null;
        }

        public int getLocal_best_index() {
            return local_best_index;
        }

        public void setLocal_best_index(int local_best_index) {
            this.local_best_index = local_best_index;
        }
    }


}