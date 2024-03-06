package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFromBeginningToEnd {

    @Test
    public void completeGame(){
        Village village = new Village(); // DaysGone: 0, food: 10, foodPerDay: 5, wood: 0, woodPerDay: 1, metal: 0, metalPerDay: 1, Workers: 0, farmer: 0, miner: 0, lumberjack: 0, builder: 0, Buildings: 0, quarry: 0, woodmill: 0, house: 0, maxWorkers: 6, farm: 0, Castle: 0
        village.AddWorker("Daim", "farmer");
        village.AddWorker("Polly", "builder");
        village.AddWorker("Japp", "farmer");
        village.AddWorker("Mars", "lumberjack");
        village.AddWorker("Marabou", "miner");
        village.AddWorker("Haribo", "lumberjack");
        village.Day(); // food: 14, metal: 1, wood: 2
        village.Day(); // food: 18, metal: 2, wood: 4
        village.Day(); // food: 22, metal: 3, wood: 6
        village.AddProject("House"); // food: 22, metal: 3, wood: 1
        village.Day(); // food: 26, metal: 4, wood: 3
        village.Day(); // food: 30, metal: 5, wood: 5
        village.Day(); // food: 34, metal: 6, wood: 7
        village.AddWorker("Bimbo", "builder"); // food: 34, metal: 6, wood: 7
        village.AddWorker("Cloetta", "builder"); // food: 34, metal: 6, wood: 7
        village.AddProject("Woodmill"); // food: 34, metal: 5, wood: 2
        village.Day(); // food: 36, metal: 6, wood: 4
        village.AddProject("Quarry"); // food: 36, metal: 1, wood: 1
        village.Day(); // food: 38, metal: 2, wood: 3
        village.Day(); // food: 40, metal: 3, wood: 7
        village.Day(); // food: 42, metal: 4, wood: 11
        village.AddProject("House"); // food: 42, metal: 4, wood: 6
        village.Day(); // food: 44, metal: 6, wood: 10
        village.AddWorker("Candy", "builder");
        village.AddWorker("Hershey", "miner");
        village.Day(); // food: 44, metal: 10, wood: 14
        village.Day(); // food: 44, metal: 14, wood: 18
        village.AddProject("Woodmill"); // food: 44, metal: 13, wood: 13
        village.Day(); // food: 44, metal: 17, wood: 17
        village.Day(); // food: 44, metal: 21, wood: 23
        village.AddProject("House"); // food: 44, metal: 21, wood: 18
        village.Day(); // food: 44, metal: 25, wood: 24
        village.AddWorker("Chocolate", "miner");
        village.AddWorker("Chocolate", "lumberjack");
        village.AddProject("House"); // food: 44, metal: 25, wood: 19
        village.Day(); // food: 42, metal: 31, wood: 28
        village.Day(); // food: 40, metal: 37, wood: 37
        village.Day(); // food: 38, metal: 43, wood: 46
        village.Day(); // food: 36, metal: 49, wood: 55
        village.Day(); // food: 34, metal: 55, wood: 64
        village.AddWorker("Peanut", "farmer"); // food: 34, metal: 55, wood: 64
        village.AddWorker("Peanut", "builder"); // food: 34, metal: 55, wood: 64
        village.AddProject("House"); // food: 34, metal: 55, wood: 59
        village.Day(); // food: 35, metal: 61, wood: 68
        village.AddProject("House"); // food: 35, metal: 61, wood: 63
        village.AddProject("Castle"); // food: 35, metal: 11, wood: 13
        village.AddWorker("Plopp", "builder");
        village.Day(); // food: 36, metal: 17, wood: 22
        village.Day(); // food: 36, metal: 23, wood: 31
        village.Day(); // food: 36, metal: 29, wood: 40
        village.AddWorker("Gummy", "builder");
        village.AddWorker("Lakrits", "builder");
        village.AddWorker("Snickers", "builder");
        village.Day(); // food: 32, metal: 35, wood: 49
        village.Day(); // food: 29, metal: 41, wood: 58
        village.Day(); // food: 26, metal: 47, wood: 67
        village.Day(); // food: 23, metal: 53, wood: 76
        village.Day(); // food: 20, metal: 59, wood: 85

        int expectedFood = 20;
        int expectedMetal = 59;
        int expectedWood = 85;
        int expectedSizeOfWorkers = 18;
        int expectedSizeOfBuildings = 13;
        int expectedFoodPerDay = 5;
        int expectedMetalPerDay = 2;
        int expectedWoodPerDay = 3;
        int expectedDaysGone = 30;
        int expectedMaxWorkers = 18;

        assertAll(
                () -> assertEquals(expectedFood, village.getFood()),
                () -> assertEquals(expectedMetal, village.getMetal()),
                () -> assertEquals(expectedWood, village.getWood()),
                () -> assertEquals(expectedSizeOfWorkers, village.getWorkers().size()),
                () -> assertEquals(expectedSizeOfBuildings, village.getBuildings().size()),
                () -> assertEquals(expectedFoodPerDay, village.getFoodPerDay()),
                () -> assertEquals(expectedMetalPerDay, village.getMetalPerDay()),
                () -> assertEquals(expectedWoodPerDay, village.getWoodPerDay()),
                () -> assertEquals(expectedDaysGone, village.getDaysGone()),
                () -> assertTrue(village.isGameOver()),
                () -> assertEquals(expectedMaxWorkers, village.getMaxWorkers()),
                () -> assertTrue(village.isFull())
        );

    }

}