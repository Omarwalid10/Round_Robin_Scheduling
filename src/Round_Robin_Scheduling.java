import java.util.*;
public class Round_Robin_Scheduling {
    public static void main(String[] args) {

        HashMap<String,Integer>arrival=new HashMap<String,Integer>();
        HashMap<String,Integer>burstTime=new HashMap<String,Integer>();
        HashMap<String,Integer>burstChange=new HashMap<String,Integer>();
        HashMap<String,Integer>completion=new HashMap<String,Integer>();
        HashMap<String,Integer>waiting=new HashMap<String,Integer>();
        HashMap<String,Integer>turnAround=new HashMap<String,Integer>();
        Queue<String> readyQueue = new LinkedList<String>();//add peek remove
        String chart="",executionOrder="";
        Scanner input=new Scanner(System.in);
        System.out.println("Enter Number of processes : ");
        int size=input.nextInt();
        System.out.println("Enter Time Quantum : ");
        int qTime=input.nextInt();
        System.out.println("Enter context Switching : ");
        int switching=input.nextInt();
        int tempSwitch=switching;
        int maxArrive=0;
        for (int i=0; i<size; i++){
            System.out.println("Enter process "+(i+1)+" (Name , Burst Time , arrival Time) :");
            String name=input.next();
            //System.out.println("Enter Burst Time of process "+(i+1));
            int burst=input.nextInt();
            // System.out.println("Enter arrival Time process "+(i+1));
            int arrive= input.nextInt();
            if(arrive>maxArrive)maxArrive=arrive;
            arrival.put(name,arrive);
            burstTime.put(name,burst);
            burstChange.put(name,burst);
            completion.put(name,0);
        }
//        System.out.println(arrival);
//        System.out.println(burstTime);
//        System.out.println(burstChange);
        int timer=0;
        String currentpro="";
        int currentEx=0;
        boolean first=true;
        int count=0;
        while (!readyQueue.isEmpty()||timer<=maxArrive||!currentpro.equals("")){
            if(first) {
                for (String name : arrival.keySet()) {
                    if (arrival.get(name) == timer) {
                        currentpro = name;
                        currentEx=burstChange.get(name);
                        count=qTime;
                        first=false;
                        break;
                    }
                }
            }
            if(timer<=maxArrive){
                for (String name : arrival.keySet()) {
                    int value=arrival.get(name);
                    if (value == timer) {
                        if(!name.equals(currentpro)&&!readyQueue.contains(name)){
                            readyQueue.add(name);
                        }
                        if(currentpro.equals("")){
                            currentpro = name;
                            currentEx=burstChange.get(name);
                            count=qTime;
                        }
                    }
                }
            }
            if(!currentpro.equals("")){
                if(currentEx>0&&count>0){
                    currentEx--;
                    count--;
                }
                //when quantum time end or execution time
                else{
                    //every change save it
                    chart+=(currentpro+"->");
                    //to check if any process arrive in context switching time
                    while (tempSwitch>0) {
                        for (String name : arrival.keySet()) {
                            int value = arrival.get(name);
                            if (value == timer) {
                                if (!name.equals(currentpro) && !readyQueue.contains(name)) {
                                    readyQueue.add(name);
                                }
                            }
                        }
                        //System.out.println("switch  "+timer+"  "+currentpro);
                        tempSwitch--;
                        timer++;
                    }
                    //to create new temp switching
                    tempSwitch=switching;
                    //to store the new execution
                    burstChange.put(currentpro,currentEx);
                    if(currentEx>0) {
                        readyQueue.add(currentpro);
                    }
                    else{
                        //execution order of each process save after the process end its burst time
                        executionOrder+=(currentpro+"->");
                        completion.put(currentpro,timer);
                        turnAround.put(currentpro,timer-arrival.get(currentpro));//completion - arrival
                        waiting.put(currentpro,turnAround.get(currentpro)-burstTime.get(currentpro));//turn around - burst
                    }
                    if(!readyQueue.isEmpty()){
                        currentpro=readyQueue.peek();
                        readyQueue.remove();
                        currentEx=burstChange.get(currentpro)-1;
                        count=qTime-1;
                    }
                    else{
                        currentpro="";
                    }
                }
            }
            // System.out.println(timer+"  "+currentpro);
            //because in context switching the timer increase one more without make this condition
            if(switching>0) {
                for (String name : arrival.keySet()) {
                    int value = arrival.get(name);
                    if (value == timer) {
                        if (!name.equals(currentpro) && !readyQueue.contains(name)) {
                            readyQueue.add(name);
                        }
                    }
                }
            }
            timer++;
        }


        System.out.println("-----------");
//        System.out.println(arrival);
//        System.out.println(burstTime);
//        System.out.println(burstChange);
//        System.out.println(readyQueue);
//        System.out.println(completion);
//        System.out.println(turnAround);
//        System.out.println(waiting);
        double turnAverage=0,waitingAverage=0;
        for (int value : turnAround.values()) {
            turnAverage+=(double)value;
        }
        turnAverage/=(double)size;
        for (int value : waiting.values()) {
            waitingAverage+=(double)value;
        }
        waitingAverage/=(double)size;
        System.out.println("------------------------------------------------------------------------");
        System.out.println("The Chart -------------->  "+ chart);
        System.out.println("The Execution Order ---->  "+ executionOrder);
        System.out.print("\nProgram Name.\tArrival Time\tBurst Time\tTurnAround Time\t\tWaiting Time\n");
        for (String name : arrival.keySet()) {
            System.out.println("\t"+name+"\t\t\t\t"+arrival.get(name)+"\t\t\t\t"+burstTime.get(name)+"\t\t\t\t"+turnAround.get(name)+"\t\t\t\t"+waiting.get(name));
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Turn Around time Average --------> "+turnAverage);
        System.out.println("Waiting Time Average     --------> "+waitingAverage);






    }
}
