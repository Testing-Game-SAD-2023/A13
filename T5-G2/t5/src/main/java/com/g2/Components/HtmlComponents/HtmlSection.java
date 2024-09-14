package com.g2.Components.HtmlComponents;

import com.g2.Interfaces.ServiceManager;

public class HtmlSection extends HtmlComponent {
    public final String sectionName;

    public HtmlSection(ServiceManager serviceManager, String sectionName) {
        super(serviceManager);
        this.sectionName = sectionName;
    }

    @Override
    public String render() {
        // Renderizza una sezione specifica
        return "<div class='section'>" + getServiceData("YourServiceName", "YourAction") + "</div>";
    }

    @Override
    protected String getServiceData(String serviceName, String action, Object... params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServiceData'");
    }
}