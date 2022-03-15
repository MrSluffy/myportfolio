package my.portfolio.prjkt.views;


import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.Registration;
import my.portfolio.prjkt.views.home.HomeView;
import my.portfolio.prjkt.views.profile.ProfileView;
import my.portfolio.prjkt.views.project.ProjectView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements Broadcaster.BroadcastListener{


    private H1 viewTitle;

    public MainLayout() {
        addToNavbar(true, createHeaderContent());
        Broadcaster.register(this);
    }

    private Component createHeaderContent() {

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");
        var layout = new HorizontalLayout(createMenu());
        layout.addClassName("header-layout");
        layout.setWidthFull();
        layout.setHeight("3.9em");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Header header = new Header(viewTitle, layout);
        header.setWidthFull();
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        return header;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setHeight("4em");
        tabs.addClassName("tab-main");
        tabs.addClassNames("bg-base", "border-b", "border-contrast-10","box-border","flex", "h-xl", "items-end");
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Tab[] createMenuItems() {
        return new Tab[]{
                createTab("Home", HomeView.class),
                createTab("Project", ProjectView.class),
                createTab("Profile", ProfileView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }


    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        return layout;
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @Override
    public void receiveBroadcast(String message) {

    }

    @Override
    public Registration addAttachListener(ComponentEventListener<AttachEvent> listener) {
        return super.addAttachListener(listener);
    }

    @Override
    public Registration addDetachListener(ComponentEventListener<DetachEvent> listener) {
        return super.addDetachListener(listener);
    }

    @Override
    public void removeRouterLayoutContent(HasElement oldContent) {
        super.removeRouterLayoutContent(oldContent);
    }
}
