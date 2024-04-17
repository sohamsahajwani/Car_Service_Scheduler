package com.volt_money.volt_money.Model;

import org.springframework.util.CollectionUtils;

import java.util.List;


public class ServiceOperator {

    static List<Operator> operators;

    public static List<Operator> getOperators() {
        if (CollectionUtils.isEmpty(operators)) {
            operators = List.of(
                    new Operator("1", "Operator 1", List.of()),
                    new Operator("2", "Operator 2", List.of()),
                    new Operator("3", "Operator 3", List.of())
            );
        }
        return operators;

    }
}
