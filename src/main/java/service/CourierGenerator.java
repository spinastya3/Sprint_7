package service;

import models.Courier;
import utils.RandomGenerator;

public class CourierGenerator {

    public static Courier courier(){
        return new Courier()
                .withLogin("Potter" + RandomGenerator.randomString(3))
                .withPassword("555")
                .withFirstName("Garry" + RandomGenerator.randomString(3));
    }
}
