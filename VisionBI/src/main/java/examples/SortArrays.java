package examples;

import java.util.Arrays;
import java.util.Comparator;

public class SortArrays {

    public static void main(String[] args) {
        // Array to be sorted
        String[] data = {"LEGAL_VEHICLE", "DEFAULT_COUNTRY", "LV_DESCRIPTION", "DEFAULT_COUNTRY_DEFAULT_COUNTRY_ISNOTNULL_R001", "LV_DESCRIPTION_LV_DESCRIPTION_CUSTOM_R001", "DEFAULT_COUNTRY_DEFAULT_COUNTRY_BYLOOKUP_R002", "LEGAL_VEHICLES_VALID"}; 
        	//{"LEGAL_VEHICLE","DEFAULT_COUNTRY","LV_DESCRIPTION","Default_Country_DEFAULT_COUNTRY_ISNOTNULL_R001",	"Lv_Description_LV_DESCRIPTION_CUSTOM_R001","Default_Country_Default_Country_BYLOOKUP_R002","Legal_Vehicles_VALID"};
//        {LEGAL_VEHICLE, DEFAULT_COUNTRY, LV_DESCRIPTION, Default_Country_DEFAULT_COUNTRY_ISNOTNULL_R001", "Lv_Description_LV_DESCRIPTION_CUSTOM_R001", "Default_Country_Default_Country_BYLOOKUP_R002", "LEGAL_VEHICLES_VALID"}

        // Sorting order array
        String[] sortOrder = {"DEFAULT_COUNTRY_DEFAULT_COUNTRY_ISNOTNULL_R001", "DEFAULT_COUNTRY_DEFAULT_COUNTRY_BYLOOKUP_R002", "LV_DESCRIPTION_LV_DESCRIPTION_CUSTOM_R001", "LEGAL_VEHICLES_VALID"}; 
        	
//        	{"Default_Country_DEFAULT_COUNTRY_ISNOTNULL_R001",	"Default_Country_Default_Country_BYLOOKUP_R002", "Lv_Description_LV_DESCRIPTION_CUSTOM_R001","Legal_Vehicles_VALID"};

        // Sort the data array based on the sortOrder array
        Arrays.sort(data, Comparator.comparingInt(s -> indexOf(sortOrder, s)));

        // Print the sorted array
        System.out.println("Sorted Data Array:");
        for (String item : data) {
            System.out.println(item);
        }
    }

    // Helper method to get the index of a string in an array
    private static int indexOf(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1; // Not found
    }
}
