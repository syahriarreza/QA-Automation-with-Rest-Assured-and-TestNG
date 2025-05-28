package cucumber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectRequest {
    private String name;
    private Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private int year;
        private double price;
        private String cpu_model;
        private String hard_disk_size;
        private String capacity;
        private String screen_size;
        private String color;
    }

}