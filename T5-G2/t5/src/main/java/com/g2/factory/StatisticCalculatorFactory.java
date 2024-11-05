package com.g2.factory;

import com.commons.model.StatisticRole;
import com.g2.Interfaces.IStatisticCalculator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticCalculatorFactory {
    @Autowired
    private List<IStatisticCalculator> calculators;

    private static final Map<StatisticRole, IStatisticCalculator> statisticCalculatorMap = new HashMap<>();

    @PostConstruct
    public void initFactory() {
        for(IStatisticCalculator calculator : calculators)
            statisticCalculatorMap.put(calculator.getRole(), calculator);
    }

    public static IStatisticCalculator getStatisticCalculator(StatisticRole role) {
        IStatisticCalculator calculator = statisticCalculatorMap.get(role);

        if (calculator == null)
            throw new RuntimeException("Unsupported statistic calculator: " + role + ". ===MAP: " + statisticCalculatorMap);

        return calculator;
    }
}
