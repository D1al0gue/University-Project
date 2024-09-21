// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 3
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/**
 * Simulation of a Hospital ER
 * 
 * The hospital has a collection of Departments, including the ER department, each of which has
 *  and a treatment room.
 * 
 * When patients arrive at the hospital, they are immediately assessed by the
 *  triage team who determine the priority of the patient and (unrealistically) a sequence of treatments 
 *  that the patient will need.
 *
 * The simulation should move patients through the departments for each of the required treatments,
 * finally discharging patients when they have completed their final treatment.
 *
 *  READ THE ASSIGNMENT PAGE!
 */

public class HospitalERCompl{

    /**
     * The map of the departments.
     * The names of the departments should be "ER", "Surgery", "X-ray", "MRI", and "Ultrasound"
     */

    private Map<String, Department> departments = new HashMap<String, Department>();

    // Copy the code from HospitalERCore and then modify/extend to handle multiple departments

    private Queue<Patient> waitingRoom = new ArrayDeque<>();
    private Set<Patient> treatmentRoom = new HashSet<>();
    // fields for the statistics
    /*# YOUR CODE HERE */
    private int totalPatientsTreated;
    private int waitingTime;
    private int pri1Treated;
    private int pri1WaitingTime;

    // Fields for the simulation
    private boolean running = false;
    private int time = 0; // The simulated time - the current "tick"
    private int delay = 300;  // milliseconds of real time for each tick


    /**
     * stop any running simulation
     * Define the departments available and put them in the map of departments.
     * Each department needs to have a name and a maximum number of patients that
     * it can be treating at the same time.
     * reset the statistics
     */
    public void reset(boolean usePriorityQueues){
        /*# YOUR CODE HERE */

        running = false; 
        UI.sleep(2*delay);

        time = 0;

        departments.clear();
        departments.put("ER",          new Department("ER", 8, usePriorityQueues));
        departments.put("Surgery",     new Department("Surgery", 2, usePriorityQueues));
        departments.put("X-ray",       new Department("X-ray", 2, usePriorityQueues));
        departments.put("MRI",         new Department("MRI", 1, usePriorityQueues));
        departments.put("Ultrasound",  new Department("Ultrasound", 3, usePriorityQueues));

        totalPatientsTreated = 0;
        waitingTime = 0;
        pri1Treated = 0;
        pri1WaitingTime = 0;

        UI.clearGraphics();
        UI.clearText();
    }

    /**
     * Main loop of the simulation
     */
    public void run() {
        if (running) {
            return; // don't start simulation if already running one!
        }
        running = true;
    
        while (running) {
            // Advance the time by one tick
            time++;
    
            List<Patient> toMoveOn = new ArrayList<>();
            for (Department dept : departments.values()){
                List<Patient> toRemove = new ArrayList<>();
                for (Patient p : dept.getTreatmentRoom()){
                    if (p.currentTreatmentFinished()){
                        toRemove.add(p);
                    }
                }
                for (Patient p : toMoveOn){
                    dept.removePatient(p);
                }
            }

            for (Patient p : toMoveOn){
                p.removeCurrentTreatment();
                if (p.allTreatmentsCompleted()){
                    discharge(p);
                } else {
                    String nextDeptName = p.getCurrentDepartment();
                    Department nextDept = departments.get(nextDeptName);
                    if (nextDept == null) {throw new RuntimeException("No such department: "+ nextDeptName);}
                    nextDept.addPatient(p);
                }
            }

            for (Department dept : departments.values()){
                for (Patient p : dept.getTreatmentRoom()){
                    p.advanceCurrentTreatmentByTick();
                }
            }

            for (Department dept : departments.values()){
                for (Patient p : dept.getWaitingRoom()){
                    p.waitForATick();
                }
            }

            for (Department dept : departments.values()){
                dept.movePatientsToTreatmentRoom();
            }

            Patient newPatient = PatientGenerator.getNextPatient(time);
            if (newPatient != null){
                UI.println(time + ": Arrived: " + newPatient);
                String loc = newPatient.getCurrentDepartment();
                if (!departments.containsKey(loc)){
                    UI.println("No such location "+ loc + " for " + newPatient);
                } else {
                    departments.get(loc).addPatient(newPatient);
                }
            }

            redraw(); // Redraw the departments
            UI.sleep(delay);

        }
        reportStatistics();
    }


    

    /**
     * Report that a patient has been discharged, along with any
     * useful statistics about the patient
     */
    public void discharge(Patient patient) {
        totalPatientsTreated++;
        waitingTime += patient.getTotalWaitingTime();
        if (patient.getPriority() == 1){
            pri1Treated++;
            pri1WaitingTime += patient.getTotalWaitingTime();
        }
        UI.println(time+ " Discharge: " + patient);
    }

    /**
     * Report summary statistics about the simulation
     */
    public void reportStatistics(){
        /*# YOUR CODE HERE */
        UI.println("Total patients treated: " + totalPatientsTreated);
        UI.println("Total priority1 patients treated: " + pri1Treated);
        //average waiting time for patients

        if (totalPatientsTreated > 0){
            double averageTime = waitingTime/totalPatientsTreated;
            double averageTime1 = pri1WaitingTime/pri1Treated;

            UI.println("Average visit time: " + averageTime);
            UI.println("Average Priority 1 visit time: " + averageTime1);
        }
    }


    // METHODS FOR THE GUI AND VISUALISATION

    /**
     * Set up the GUI: buttons to control simulation and sliders for setting parameters
     */
    public void setupGUI(){
        UI.addButton("Reset (Queue)", () -> {this.reset(false); });
        UI.addButton("Reset (Pri Queue)", () -> {this.reset(true);});
        UI.addButton("Start", ()->{if (!running){ run(); }});   //don't start if already running!
        UI.addButton("Pause & Report", ()->{running=false;});
        UI.addSlider("Speed", 1, 400, (401-delay),
            (double val)-> {delay = (int)(401-val);});
        UI.addSlider("Av arrival interval", 1, 50, PatientGenerator.getArrivalInterval(),
                     PatientGenerator::setArrivalInterval);
        UI.addSlider("Prob of Pri 1", 1, 100, PatientGenerator.getProbPri1(),
                     PatientGenerator::setProbPri1);
        UI.addSlider("Prob of Pri 2", 1, 100, PatientGenerator.getProbPri2(),
                     PatientGenerator::setProbPri2);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Redraws all the departments
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 15);
        UI.drawString("Waiting Queues", 200, 15);
        UI.drawLine(0,32,400, 32);
        double y = 80;
        for (Department dept : departments.values()){
            dept.redraw(y);
            UI.drawLine(0,y+2,400, y+2);
            y += 50;
        }
    }

    /**
     * Construct a new HospitalER object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments){
        HospitalERCompl er = new HospitalERCompl();
        er.setupGUI();
        er.reset(false);   // initialise with an ordinary queue.
        UI.println("-------------");
        UI.println("Tried loading the completion, but can't, can't get the patient to draw, nor the rooms.");
        UI.println("report statistics working");
    }        


}
