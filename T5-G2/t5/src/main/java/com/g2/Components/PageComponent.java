package com.g2.Components;

import java.util.Map;

public abstract class PageComponent {
     // Metodo astratto che deve essere implementato dalle sottoclassi
    public abstract Map<String, Object> getModel();

    // Metodo di utilità per gestire la logica comune, se necessario
    protected String processCommonLogic() {
        // Implementa la logica comune qui
        return "Logica comune elaborata";
    }
}
