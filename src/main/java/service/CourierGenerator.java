package service;

import models.Courier;
import static utils.TestData.faker;

public class CourierGenerator {

    public static Courier courier(){
        return new Courier()
                .withLogin(faker.name().username())
                .withPassword("test")
                .withFirstName(faker.name().firstName());
    }
}
