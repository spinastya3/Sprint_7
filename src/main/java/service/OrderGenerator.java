package service;

import models.Order;

import static utils.TestData.faker;

public class OrderGenerator {
    public static Order order() {

        return new Order(faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                "4",
                faker.phoneNumber().phoneNumber(),
                5,
                "2030-06-06",
                "test",
                null);
    }
}
