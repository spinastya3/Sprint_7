package service;

import models.Order;

public class OrderGenerator {

    public static Order order(){
        return new Order("Garry",
                "Potter",
                "Privet Drive, 4",
                "4",
                "+79991234567",
                5,
                "2030-06-06",
                "test",
                null);
    }
}
