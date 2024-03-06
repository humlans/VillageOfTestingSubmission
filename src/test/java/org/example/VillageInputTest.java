package org.example;

import org.example.interfaces.IOccupationAction;
import org.example.objects.Building;
import org.example.objects.Project;
import org.example.objects.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VillageInputTest {

    private DatabaseConnection databaseConnection;
    private VillageInput villageInput;

    @BeforeEach
    public void setup(){
        this.databaseConnection = mock(DatabaseConnection.class);
        Village village = new Village();
        villageInput = new VillageInput(village, databaseConnection);
    }

    @ParameterizedTest
    @CsvSource (value = {"These", "are", "placeholders"})
    public void databaseConnection_loadVillage_successfulLoad(String choice){
        Village expectedVillage = createVillage();
        InputStream backup = System.in;
        String chosenInput = "4\n" + choice + "\n" + "6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.LoadVillage("These")).thenReturn(expectedVillage);
        when(villageInput.databaseConnection.LoadVillage("are")).thenReturn(expectedVillage);
        when(villageInput.databaseConnection.LoadVillage("placeholders")).thenReturn(expectedVillage);

        villageInput.Run();
        Village actualVillage = villageInput.village;

        assertAll(
                () -> assertEquals(expectedVillage.getWorkers(),actualVillage.getWorkers()),
                () -> assertEquals(expectedVillage.getMaxWorkers(), actualVillage.getMaxWorkers()),
                () -> assertEquals(expectedVillage.getProjects(), actualVillage.getProjects()),
                () -> assertEquals(expectedVillage.getWood(), actualVillage.getWood()),
                () -> assertEquals(expectedVillage.getFood(), actualVillage.getFood()),
                () -> assertEquals(expectedVillage.getMetal(), actualVillage.getMetal()),
                () -> assertEquals(expectedVillage.getBuildings(), actualVillage.getBuildings()),
                () -> assertEquals(expectedVillage.getProjects(), actualVillage.getProjects()),
                () -> assertEquals(expectedVillage, actualVillage)
        );

        System.setIn(backup);
    }

    @ParameterizedTest
    @CsvSource (value = {"htrte","fail"})
    public void databaseConnection_loadVillage_failsToLoad(String choice){
        InputStream backup = System.in;
        String chosenInput = "4\n" + choice + "\n" + "6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        villageList.add("fail");
        villageList.add("htrte");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.LoadVillage(any())).thenReturn(null);
        when(villageInput.databaseConnection.LoadVillage("are")).thenReturn(createVillage());
        when(villageInput.databaseConnection.LoadVillage("fail")).thenReturn(null);

        villageInput.Run();
        Village actualVillage = villageInput.village;

        assertEquals(actualVillage, villageInput.village);
        assertNull(villageInput.databaseConnection.LoadVillage(choice));
        Mockito.verify(databaseConnection, times(2)).LoadVillage(choice);

        System.setIn(backup);
    }

    @Test
    public void databaseConnection_loadVillage_invalidTownName(){
        InputStream backup = System.in;
        String choice = "nonsense";
        String chosenInput = "4\n" + choice + "\n" + "6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.LoadVillage("These")).thenReturn(createVillage());
        when(villageInput.databaseConnection.LoadVillage("are")).thenReturn(createVillage());
        when(villageInput.databaseConnection.LoadVillage("placeholders")).thenReturn(createVillage());


        villageInput.Run();
        Village actualVillage = villageInput.village;

        assertEquals(actualVillage, villageInput.village);
        Mockito.verify(databaseConnection, times(0)).LoadVillage(choice);

        System.setIn(backup);
    }


    @Test
    public void databaseConnection_saveVillage_successfullySaved(){
        Village village = villageInput.village;
        InputStream backup = System.in;
        String choice = "These";
        String chosenInput = "5\n" + choice + "\ny\n6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.SaveVillage(village, "These")).thenReturn(true);

        villageInput.Run();

        assertTrue(villageInput.databaseConnection.SaveVillage(village, "These"));
        Mockito.verify(databaseConnection, times(2)).SaveVillage(village, "These");

        System.setIn(backup);
    }

    @Test
    public void databaseConnection_saveVillage_interruptsSaveWithChoice(){
        Village village = villageInput.village;
        InputStream backup = System.in;
        String choice = "are";
        String chosenInput = "5\n" + choice + "\nno\n6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.SaveVillage(village, "are")).thenReturn(false);

        villageInput.Run();

        // Save village should never be called because the function Save in VillageInput is interrupted before that.
        Mockito.verify(databaseConnection, times(0)).SaveVillage(village, "are");

        System.setIn(backup);
    }

    @Test
    public void databaseConnection_saveVillage_failedToSave(){
        Village village = villageInput.village;
        InputStream backup = System.in;
        String choice = "are";
        String chosenInput = "5\n" + choice + "\ny\n6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.SaveVillage(village, "are")).thenReturn(false);

        villageInput.Run();

        assertFalse(villageInput.databaseConnection.SaveVillage(village, "are"));
        Mockito.verify(databaseConnection, times(2)).SaveVillage(village, "are");

        System.setIn(backup);
    }

    @Test
    public void databaseConnection_saveVillage_invalidTownName(){
        Village chosenVillage = createVillage();
        InputStream backup = System.in;
        String choice = "nonsense";
        String chosenInput = "5\n" + choice + "\n" + "6\n";
        ByteArrayInputStream chosenIn = new ByteArrayInputStream(chosenInput.getBytes());
        System.setIn(chosenIn);
        villageInput.scanner = new Scanner(chosenInput);
        ArrayList<String> villageList = new ArrayList<>();
        villageList.add("These");
        villageList.add("are");
        villageList.add("placeholders");
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(villageList);
        when(villageInput.databaseConnection.SaveVillage(chosenVillage, "htr")).thenReturn(false);

        villageInput.Run();

        assertFalse(villageInput.databaseConnection.SaveVillage(chosenVillage, "htr"));
        Mockito.verify(databaseConnection, times(1)).SaveVillage(chosenVillage, "htr");
        Mockito.verify(databaseConnection, times(1)).SaveVillage(villageInput.village, "nonsense");

        System.setIn(backup);
    }


    private Village createVillage(){
        ArrayList<Worker> workers = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Project> projects = new ArrayList<>();
        Village village = new Village(false, 5, 0, 0, workers, buildings, projects, 0, 0, 0, 5, 0,5);
        return village;
    }

}