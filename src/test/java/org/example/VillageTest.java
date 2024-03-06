package org.example;

import org.example.interfaces.ICompleteAction;
import org.example.interfaces.IOccupationAction;
import org.junit.jupiter.api.BeforeEach;

import org.example.objects.Building;
import org.example.objects.Project;
import org.example.objects.Worker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VillageTest {

    // Tests for all the add-functions: AddWorker(), AddNewProject(), AddFood(), AddMetal(), AddWood().
    @ParameterizedTest
    @CsvSource(value = {"Jane,farmer,1", "John,lumberjack,1", "Yngvild,builder,1", "Jurek,miner,1"})
    public void addWorker_successfullyAddedNewWorkerToEmptyVillage(String expectedName,
                                                                      String expectedOccupation,
                                                                      int expectedListSize){
        Village village = createEmptyVillage();

        village.AddWorker(expectedName, expectedOccupation);
        String actualName = village.getWorkers().get(0).getName();
        String actualOccupation = village.getWorkers().get(0).getOccupation();
        int actualListSize = village.getWorkers().size();

        assertAll(
                () -> assertEquals(expectedName, actualName),
                () -> assertEquals(expectedOccupation, actualOccupation),
                () -> assertEquals(expectedListSize, actualListSize)
        );
    }

    @ParameterizedTest
    @CsvSource ( value = {"Jacinda,farmer", "Yvonne,lumberjack", "Kicki,builder", "Tilda,miner"})
    public void addWorker_successfullyAddedNewWorkerToVillageWith4Workers(String expectedName,
                                                                             String expectedOccupation){
        // The previous workers are added with a constructor in createVillageWithPeople().
        Village village = createVillageWithPeople();
        int expectedListSize = 5;

        village.AddWorker(expectedName, expectedOccupation);
        String actualName = village.getWorkers().get(4).getName();
        String actualOccupation = village.getWorkers().get(4).getOccupation();
        int actualListSize = village.getWorkers().size();

        assertAll(
                () -> assertEquals(expectedName, actualName),
                () -> assertEquals(expectedOccupation, actualOccupation),
                () -> assertEquals(expectedListSize, actualListSize)
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Mila,builder", "Pedro,lumberjack", "Hercules,farmer"})
    public void addWorker_maxWorkersZeroReached_couldNotAddNewWorker(String name, String occupation){
        Village village = createEmptyVillage();
        village.setMaxWorkers(0);
        int expectedListSize = 0;

        village.AddWorker(name, occupation);
        int actualListSize = village.getWorkers().size();

        assertEquals(expectedListSize, actualListSize);
    }

    @ParameterizedTest
    @CsvSource (value = {"Hermes,builder", "Venus,lumberjack", "Zeus,miner"})
    public void addWorker_maxWorkersFiveReached_couldNotAddNewWorker(String name, String occupation){
        Village village = createVillageWithPeople();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker("Pascal", "farmer", village.getOccupationHashMap().get("farmer")));
        int expectedListSize = 5;
        String expectedLastWorkerName = "Pascal";
        String expectedLastWorkerOccupation = "farmer";
        Worker expectedWorker = workers.get(4);

        village.AddWorker(name, occupation);
        int actualListSize = workers.size();

        assertAll(
                () -> assertEquals(expectedListSize, actualListSize),
                () -> assertSame(expectedWorker, village.getWorkers().get(4)),
                () -> assertEquals(expectedLastWorkerName, village.getWorkers().get(actualListSize-1).getName()),
                () -> assertEquals(expectedLastWorkerOccupation, village.getWorkers().get(actualListSize-1).getOccupation())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"5,0,0,0,House", "15,5,10,5,House", "5,1,0,0,Woodmill","10,8,5,7,Woodmill",
            "3,5,0,0,Quarry", "10,13,7,8,Quarry", "5,2,0,0,Farm", "42,3,37,1,Farm",
            "50,50,0,0,Castle", "150,250,100,200,Castle"})
    public void addProject_successfullyAddsProjectsToEmptyProjectList(int existingWood,
                                                                      int existingMetal,
                                                                      int expectedWood,
                                                                      int expectedMetal,
                                                                      String expectedProject){
        /* The test will first try to add a project with the minimum amount of materials needed.
           From the beginning this test failed because there was only an ">" between needed materials
           and owned material. After an added "=" this test is successful.
           The second time it tries to add the same kind of project the materials are over what is needed.
        */
        Village village = createEmptyVillage();
        village.setMetal(existingMetal);
        village.setWood(existingWood);
        ArrayList<Project> projects = village.getProjects();
        int expectedProjectListSize = 1;

        village.AddProject(expectedProject);

        assertAll(
                () -> assertEquals(expectedProjectListSize, projects.size()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedProject, projects.get(0).getName())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"5,0,0,0,House", "23,4,18,4,House", "5,1,0,0,Woodmill",
                        "14,17,9,16,Woodmill", "3,5,0,0,Quarry", "17,7,14,2,Quarry",
                        "5,2,0,0,Farm", "16,13,11,11,Farm", "50,50,0,0,Castle", "57,61,7,11,Castle"})
    public void addProject_successfullyAddsProjectsToProjectListWithExistingProjects(int existingWood,
                                                                                     int existingMetal,
                                                                                     int expectedWood,
                                                                                     int expectedMetal,
                                                                                     String expectedProject){
        Village village = createEmptyVillage();
        village.setMetal(existingMetal);
        village.setWood(existingWood);
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project("House", 3, () -> village.getPossibleProjects().get("House").GetProject().Complete()));
        projects.add(new Project("Woodmill", 3, () -> village.getPossibleProjects().get("Woodmill").GetProject().Complete()));
        int expectedProjectListSize = 3;

        village.AddProject(expectedProject);
        assertAll(
                () -> assertEquals(expectedProjectListSize, projects.size()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedProject, projects.get(2).getName())
        );

    }

    @ParameterizedTest
    @CsvSource(value = {"4,0,House", "2,5,House", "4,1,Woodmill","5,0,Woodmill",
                        "2,4,Quarry", "5,3,Quarry", "4,2,Farm", "5,1,Farm",
                        "49,49,Castle", "20,67,Castle"})
    public void addProject_insufficientMaterials_failsToAddProject(int expectedWood,
                                                                   int expectedMetal,
                                                                   String expectedProjectName){
        /* The test is designed to first try the number just below the minimum amount
            of materials needed and then with even less than that.
         */

        Village village = createEmptyVillage();
        village.setMetal(expectedMetal);
        village.setWood(expectedWood);
        ArrayList<Project> projects = village.getProjects();
        int expectedProjectListSize = 0;

        village.AddProject(expectedProjectName);

        assertAll(
                () -> assertEquals(expectedProjectListSize, projects.size()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedMetal, village.getMetal())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Skyscraper", "Home", "Church"})
    public void addProject_invalidProjectName_failsToAddProjectToEmptyList(String name){
        Village village = createEmptyVillage();
        ArrayList<Project> projects = village.getProjects();
        int expectedListSize = 0;

        village.AddProject(name);

        assertEquals(expectedListSize, projects.size());
    }

    @ParameterizedTest
    @CsvSource (value = {"School", "Gym"})
    public void addProject_invalidProjectName_failsToAddProjectToListWithProjects(String name){
        Village village = createEmptyVillage();
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project("House", 3, () -> village.getPossibleProjects().get("House").GetProject().Complete()));
        projects.add(new Project("Woodmill", 3, () -> village.getPossibleProjects().get("Woodmill").GetProject().Complete()));
        int expectedListSize = 2;
        String expectedLastProjectName = "Woodmill";
        int expectedLastProjectDaysLeft = 3;

        village.AddProject(name);

        assertAll(
                () -> assertEquals(expectedListSize, projects.size()),
                () -> assertEquals(expectedLastProjectName, village.getProjects().get(1).getName()),
                () -> assertEquals(expectedLastProjectDaysLeft, village.getProjects().get(1).getDaysLeft())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"2,5,7,Paula", "0,0,0,Patrik", "12,24,36,Olivia"})
    public void addFood_successfullyAddsAmountOfFood(int foodPerDay, int existingFood,
                                                     int expectedFood, String name){
        Village village = createEmptyVillage();
        village.setFoodPerDay(foodPerDay);
        village.setFood(existingFood);

        village.AddFood(name);

        assertEquals(expectedFood, village.getFood());
    }

    @ParameterizedTest
    @CsvSource(value = {"8,1,9,Yvis", "0,0,0,York", "45,6,51,Mark"})
    public void addMetal_successfullyAddsAmountOfMetal(int metalPerDay,
                                                       int existingMetal,
                                                       int expectedMetal,
                                                       String name){
        Village village = createEmptyVillage();
        village.setMetal(existingMetal);
        village.setMetalPerDay(metalPerDay);

        village.AddMetal(name);

        assertEquals(expectedMetal, village.getMetal());
    }

    @ParameterizedTest
    @CsvSource(value = {"14,3,17,Frodo", "0,0,0,Fiona", "7,0,7,Fideli"})
    public void addWood_successfullyAddsAmountOfWood(int woodPerDay,
                                                     int existingWood,
                                                     int expectedWood,
                                                     String name){
        Village village = createEmptyVillage();
        village.setWood(existingWood);
        village.setWoodPerDay(woodPerDay);

        village.AddWood(name);

        assertEquals(expectedWood, village.getWood());
    }


    // Below the test are for the Build().
    @Test
    public void build_buildWithEmptyProjectListAndNoBuildings_FailToBuildBecauseNoProjectsToBuildOn(){
        Village village = createEmptyVillage();
        int expectedProjectListSize = 0;
        int expectedBuildingsListSize = 0;

        village.Build("Jane");

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedProjectListSize, village.getProjects().size())
        );
    }

    @Test
    public void build_buildWithEmptyProjectListButAlreadyExistingBuildings_FailToBuildBecauseNoProjectsToBuildOn(){
        Village village = createEmptyVillage();
        ArrayList<Building> buildings = village.getBuildings();
        buildings.add(new Building("House"));
        buildings.add(new Building("Woodmill"));
        int expectedProjectListSize = 0;
        int expectedBuildingsListSize = 2;
        String expectedLastBuildingName = "Woodmill";

        village.Build("June");

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedLastBuildingName, village.getBuildings().get(1).getName()),
                () -> assertEquals(expectedProjectListSize, village.getProjects().size())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"House,2,1", "Woodmill,3,2", "Farm,2,1", "Castle,49,48", "Quarry,4,3"})
    public void build_buildOnAProjectWithOnlyOneProjectInList(String name, int daysLeft, int expectedDaysLeft){
        Village village = createVillageWithPeople();
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project(name, daysLeft,
                    () -> village.getPossibleProjects().get(name).GetProject().Complete()));

        village.Build("Jane");

        assertAll(
                () -> assertEquals(expectedDaysLeft, village.getProjects().get(0).getDaysLeft()),
                () -> assertEquals(name, village.getProjects().get(0).getName())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"House,3,2", "Castle,14,13", "Quarry,5,4", "Farm,2,1", "Woodmill,4,3"})
    public void build_buildOnAProjectWithMultipleProjectsInList(String name, int daysLeft, int expectedDaysLeft){
        Village village = createVillageWithPeople();
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project(name, daysLeft,
                () -> village.getPossibleProjects().get(name).GetProject().Complete()));
        projects.add(new Project("Woodmill", 5,
                () -> village.getPossibleProjects().get("Woodmill").GetProject().Complete()));
        projects.add(new Project("Quarry", 7,
                () -> village.getPossibleProjects().get("Quarry").GetProject().Complete()));

        village.Build("Jane");

        assertAll(
                () -> assertEquals(expectedDaysLeft, village.getProjects().get(0).getDaysLeft()),
                () -> assertEquals(name, village.getProjects().get(0).getName()),
                () -> assertEquals("Woodmill", village.getProjects().get(1).getName()),
                () -> assertEquals(5, village.getProjects().get(1).getDaysLeft())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"House,7,1,1,5,false", "Quarry,5,2,1,5,false", "Woodmill,5,1,2,5,false",
                            "Farm,5,1,1,10,false", "Castle,5,1,1,5,true"})
    public void build_buildAndCompleteAProjectWithEmptyBuildingsList(String buildingName,
                                                                  int expectedMaxWorkers,
                                                                  int expectedMetalPerDay,
                                                                  int expectedWoodPerDay,
                                                                  int expectedFoodPerDay,
                                                                  boolean gameOver){
        Village village = createVillageWithPeople();
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project(buildingName, 1,
                    () -> village.getPossibleProjects().get(buildingName).GetProject().Complete()));
        int expectedProjectListSize = 0;
        int expectedBuildingsListSize = 1;

        village.Build("Jane");

        assertAll(
                () -> assertEquals(expectedProjectListSize, village.getProjects().size()),
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedMaxWorkers, village.getMaxWorkers()),
                () -> assertEquals(expectedFoodPerDay, village.getFoodPerDay()),
                () -> assertEquals(expectedMetalPerDay, village.getMetalPerDay()),
                () -> assertEquals(expectedWoodPerDay, village.getWoodPerDay()),
                () -> assertEquals(gameOver, village.isGameOver())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"House,7,1,1,5,false", "Quarry,5,2,1,5,false", "Woodmill,5,1,2,5,false",
            "Farm,5,1,1,10,false", "Castle,5,1,1,5,true"})
    public void build_buildAndCompleteAProjectWithBuildingsAlreadyInBuildingList(String buildingName,
                                                                     int expectedMaxWorkers,
                                                                     int expectedMetalPerDay,
                                                                     int expectedWoodPerDay,
                                                                     int expectedFoodPerDay,
                                                                     boolean gameOver){
        Village village = createVillageWithPeople();
        ArrayList<Building> buildings = village.getBuildings();
        buildings.add(new Building("House"));
        buildings.add(new Building("Quarry"));
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project(buildingName, 1,
                () -> village.getPossibleProjects().get(buildingName).GetProject().Complete()));
        int expectedProjectListSize = 0;
        int expectedBuildingsListSize = 3;

        village.Build("Jane");

        assertAll(
                () -> assertEquals(expectedProjectListSize, village.getProjects().size()),
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedMaxWorkers, village.getMaxWorkers()),
                () -> assertEquals(expectedFoodPerDay, village.getFoodPerDay()),
                () -> assertEquals(expectedMetalPerDay, village.getMetalPerDay()),
                () -> assertEquals(expectedWoodPerDay, village.getWoodPerDay()),
                () -> assertEquals(gameOver, village.isGameOver())
        );
    }


    // DoWork() is tested below.
    @ParameterizedTest
    @CsvSource (value = {"Paul,farmer", "Ursula,lumberjack", "Ariel,builder", "Sebastian,miner"})
    public void doWork_workerIsDead(String expectedName, String occupation){
        Village village = createEmptyVillage();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker(expectedName, occupation, village.getOccupationHashMap().get(occupation),
                true, false, 3));
        int expectedDaysHungry = 3;
        int expectedFood = 0;
        int expectedMetal = 10;
        int expectedWood = 10;

        village.getWorkers().get(0).DoWork();

        assertAll(
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertFalse(village.getWorkers().get(0).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertEquals(expectedName, village.getWorkers().get(0).getName()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal,village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Paul,farmer,10,10,1", "Ursula,lumberjack,11,10,0", "Ariel,builder,10,10,0", "Sebastian,miner,10,11,0"})
    public void doWork_workerDoesExpectedWork(String expectedName, String occupation,
                                              int expectedWood, int expectedMetal, int expectedFood){
        Village village = createEmptyVillage();
        village.setWoodPerDay(1);
        village.setMetalPerDay(1);
        village.setFoodPerDay(1);
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker(expectedName, occupation, village.getOccupationHashMap().get(occupation),
                false, true, 0));
        int expectedDaysHungry = 0;

        village.getWorkers().get(0).DoWork();

        assertAll(
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertTrue(village.getWorkers().get(0).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertEquals(expectedName, village.getWorkers().get(0).getName()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedFood, village.getFood())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Paul,farmer", "Ursula,lumberjack", "Ariel,builder", "Sebastian,miner"})
    public void doWork_workerIsHungryDoesNotWorkButIsAlive(String expectedName, String occupation){
        Village village = createEmptyVillage();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker(expectedName, occupation, village.getOccupationHashMap().get(occupation),
                true, true, 1));
        int expectedDaysHungry = 2;
        int expectedFood = 0;
        int expectedMetal = 10;
        int expectedWood = 10;

        village.getWorkers().get(0).DoWork();

        assertAll(
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertTrue(village.getWorkers().get(0).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertEquals(expectedName, village.getWorkers().get(0).getName()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal,village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Paul,farmer", "Ursula,lumberjack", "Ariel,builder", "Sebastian,miner"})
    public void doWork_workerIsHungryDoesNotWorkAndDies(String expectedName, String occupation){
        Village village = createEmptyVillage();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker(expectedName, occupation, village.getOccupationHashMap().get(occupation),
                true, true, 2));
        int expectedDaysHungry = 3;
        int expectedFood = 0;
        int expectedMetal = 10;
        int expectedWood = 10;

        village.getWorkers().get(0).DoWork();

        assertAll(
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertFalse(village.getWorkers().get(0).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertEquals(expectedName, village.getWorkers().get(0).getName()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal,village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood())
        );
    }



    // Day() is tested below.
    @ParameterizedTest
    @CsvSource (value = {"Sven,miner", "Olaf,lumberjack", "Elsa,farmer", "Anna,builder"})
    public void day_feedWorker_successfullyFedOneWorker(String name, String occupation){
        Village village = createEmptyVillage();
        village.setFood(5);
        ArrayList<Worker> workers = village.getWorkers();
        village.setWoodPerDay(0);
        village.setFoodPerDay(0);
        village.setMetalPerDay(0);
        IOccupationAction occupationAction = village.getOccupationHashMap().get(occupation);
        workers.add(new Worker(name, occupation, occupationAction));
        int expectedFood = 4;
        int expectedWood = 10;
        int expectedMetal = 10;
        int expectedBuildingsListSize = 0;

        village.Day();

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertTrue(workers.get(0).isHungry())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Pollo,lumberjack", "Lola,miner", "Peanut,farmer", "Helix,builder"})
    public void day_feedWorker_couldNotFeedOneWorkerInsufficientAmountOfFood(String name, String occupation){
        Village village = createEmptyVillage();
        village.setMetalPerDay(0);
        village.setFood(0);
        village.setFoodPerDay(0);
        village.setWoodPerDay(0);
        ArrayList<Worker> workers = village.getWorkers();
        IOccupationAction occupationAction = village.getOccupationHashMap().get(occupation);
        workers.add(new Worker(name, occupation, occupationAction, true, true, 1));
        int expectedFood = 0;
        int expectedWood = 10;
        int expectedMetal = 10;
        int expectedBuildingsListSize = 0;
        int expectedDaysHungry = 2;

        village.Day();

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertTrue(workers.get(0).isHungry()),
                () -> assertEquals(expectedDaysHungry, workers.get(0).getDaysHungry())
     );
    }

    @Test
    public void day_feedWorker_couldNotFeedFourWorkersInsufficientAmountOfFood(){
        Village village = createVillageWithPeople();
        village.setMetalPerDay(0);
        village.setFood(0);
        village.setFoodPerDay(0);
        village.setWoodPerDay(0);
        ArrayList<Worker> workers = village.getWorkers();
        int expectedFood = 0;
        int expectedWood = 0;
        int expectedMetal = 0;
        int expectedBuildingsListSize = 0;
        int expectedDaysHungry = 1;

        village.Day();

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertTrue(workers.get(0).isHungry()),
                () -> assertEquals(expectedDaysHungry, workers.get(0).getDaysHungry())
        );
    }


    @Test
    public void day_feedWorker_couldOnlyFeedTwoOutOfFourWorkers(){
        Village village = createVillageWithPeople();
        village.setMetalPerDay(0);
        village.setFood(2);
        village.setFoodPerDay(0);
        village.setWoodPerDay(0);
        ArrayList<Worker> workers = village.getWorkers();
        int expectedFood = 0;
        int expectedWood = 0;
        int expectedMetal = 0;
        int expectedBuildingsListSize = 0;
        int expectedDaysHungry = 1;

        village.Day();

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertTrue(workers.get(0).isHungry()),
                () -> assertTrue(workers.get(1).isHungry()),
                () -> assertTrue(workers.get(2).isHungry()),
                () -> assertTrue(workers.get(3).isHungry()),
                () -> assertEquals(0, workers.get(0).getDaysHungry()),
                () -> assertEquals(0, workers.get(1).getDaysHungry()),
                () -> assertEquals(expectedDaysHungry, workers.get(2).getDaysHungry()),
                () -> assertEquals(expectedDaysHungry, workers.get(3).getDaysHungry()),
                () -> assertFalse(village.isGameOver())
        );
    }

    @Test
    public void day_feedWorker_successfullyFedTwoWorkers(){
        Village village = createEmptyVillage();
        village.setFood(5);
        village.setWoodPerDay(0);
        village.setFoodPerDay(0);
        village.setMetalPerDay(0);
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker("Sven", "miner", village.getOccupationHashMap().get("miner")));
        workers.add(new Worker("Pierre", "builder", village.getOccupationHashMap().get("builder")));
        int expectedFood = 3;
        int expectedWood = 10;
        int expectedMetal = 10;
        int expectedBuildingsListSize = 0;

        village.Day();

        assertAll(
                () -> assertEquals(expectedBuildingsListSize, village.getBuildings().size()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood())
        );
    }

    @Test
    public void day_feedWorker_singleWorkerDied_allDeadAndGameOver(){
        Village village = createEmptyVillage();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker("One", "miner", village.getOccupationHashMap().get("miner"), true, true, 2));
        int expectedMetal = 10;
        int expectedWood = 10;
        int expectedFood = 0;
        int expectedDaysHungry = 3;
        int expectedSizeOfWorkers = 1;
        int expectedDaysGone = 1;

        village.Day();

        assertAll(
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertEquals(expectedSizeOfWorkers, village.getWorkers().size()),
                () -> assertFalse(village.getWorkers().get(0).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertTrue(village.isGameOver()),
                () -> assertEquals(expectedDaysGone, village.getDaysGone())
        );

    }

    @Test
    public void day_feedWorker_twoWorkersDied_allDeadAndGameOver(){
        Village village = createEmptyVillage();
        ArrayList<Worker> workers = village.getWorkers();
        workers.add(new Worker ("Laa-Laa", "miner", village.getOccupationHashMap().get("miner"), true, true, 2));
        workers.add(new Worker ("Tinky Winky", "lumberjack", village.getOccupationHashMap().get("lumberjack"), true, true, 2));
        int expectedMetal = 10;
        int expectedWood = 10;
        int expectedFood = 0;
        int expectedDaysHungry = 3;
        int expectedSizeOfWorkers = 2;
        int expectedDaysGone = 1;

        village.Day();

        assertAll(
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(0).getDaysHungry()),
                () -> assertEquals(expectedDaysHungry, village.getWorkers().get(1).getDaysHungry()),
                () -> assertEquals(expectedSizeOfWorkers, village.getWorkers().size()),
                () -> assertFalse(village.getWorkers().get(0).isAlive()),
                () -> assertFalse(village.getWorkers().get(1).isAlive()),
                () -> assertTrue(village.getWorkers().get(0).isHungry()),
                () -> assertTrue(village.getWorkers().get(1).isHungry()),
                () -> assertTrue(village.isGameOver()),
                () -> assertEquals(expectedDaysGone, village.getDaysGone())
        );
    }

    @Test
    public void day_GoToNextDayWithNoWorkers(){
        Village village = new Village();
        int expectedFood = 10;
        int expectedWood = 0;
        int expectedMetal = 0;
        int expectedDaysGone = 1;
        int expectedSizeOfWorkers = 0;

        village.Day();

        assertAll(
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedDaysGone, village.getDaysGone()),
                () -> assertEquals(expectedSizeOfWorkers, village.getWorkers().size())
        );
    }

    @ParameterizedTest
    @CsvSource (value = {"Quarry,3,2,5,5,5,1,1,1,1,0,2", "Quarry,1,5,5,5,5,2,2,1,1,1,1",
                        "House,2,1,5,5,5,1,1,1,1,0,2", "House,1,5,7,5,5,1,1,1,1,1,1",
                        "Woodmill,5,4,5,5,5,1,1,1,1,0,2", "Woodmill,1,5,5,5,5,1,1,2,2,1,1",
                        "Farm,4,3,5,5,5,1,1,1,1,0,2", "Farm,1,5,5,10,10,1,1,1,1,1,1"})
    public void day_buildCompleteAndAllAddFunctionsWithFourDifferentWorkersInVillage(String expectedBuildingName,
                                                          int daysLeft, int expectedDaysLeft,
                                                          int expectedMaxWorkers,
                                                          int expectedFood, int expectedFoodPerDay,
                                                          int expectedMetal, int expectedMetalPerDay,
                                                          int expectedWood, int expectedWoodPerDay,
                                                          int expectedBuildingListSize,
                                                          int expectedProjectListSize){
        Village village = createVillageWithPeople();
        ArrayList<Project> projects = village.getProjects();
        projects.add(new Project(expectedBuildingName, daysLeft, () -> village.getPossibleProjects().get(expectedBuildingName).GetProject().Complete()));
        projects.add(new Project("Woodmill", 5, () -> village.getPossibleProjects().get("Woodmill").GetProject().Complete()));
        int expectedDaysGone = 1;

        village.Day();

        assertAll(
                () -> assertEquals(expectedDaysLeft, projects.get(0).getDaysLeft()),
                () -> assertEquals(expectedMaxWorkers, village.getMaxWorkers()),
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedFoodPerDay, village.getFoodPerDay()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedMetalPerDay, village.getMetalPerDay()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedWoodPerDay, village.getWoodPerDay()),
                () -> assertEquals(expectedBuildingListSize, village.getBuildings().size()),
                () -> assertEquals(expectedProjectListSize, village.getProjects().size()),
                () -> assertEquals(expectedDaysGone, village.getDaysGone())
        );

    }

    // Thew two functions to either create a village with people or without people.
    public Village createVillageWithPeople(){
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Project> projects = new ArrayList<>();
        ArrayList<Worker> workers = new ArrayList<>();
        Village village = new Village(false, 4, 0, 0, workers, buildings,
                projects, 1, 1, 5, 5,
                0, 3);
        IOccupationAction farmerInterface = village.getOccupationHashMap().get("farmer");
        IOccupationAction minerInterface = village.getOccupationHashMap().get("miner");
        IOccupationAction builderInterface = village.getOccupationHashMap().get("builder");
        IOccupationAction lumberjackInterface = village.getOccupationHashMap().get("lumberjack");

        workers.add(new Worker("June", "builder", builderInterface));
        workers.add(new Worker("Jack", "lumberjack", lumberjackInterface));
        workers.add(new Worker("Jared", "miner", minerInterface));
        workers.add(new Worker("Jerry", "farmer", farmerInterface));

        return village;
    }

    public Village createEmptyVillage(){
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Project> projects = new ArrayList<>();
        ArrayList<Worker> workers = new ArrayList<>();

        Village village = new Village(false, 0, 10, 10, workers, buildings, projects,
                            0, 0, 0, 1, 0, 3);
        return village;
    }





}