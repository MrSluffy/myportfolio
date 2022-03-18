package my.portfolio.prjkt.ext;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class TabExt extends Tabs{

    public TabExt(Component[] component) {
        setHeight("4em");
        addClassName("tab-main");
        addClassNames("bg-base", "border-b", "border-contrast-10","box-border","flex", "h-xl", "items-end");
        setId("tabs");
        add(component);
    }

    public static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

}
