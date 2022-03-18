package my.portfolio.prjkt.views;


import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;
import my.portfolio.prjkt.ext.TabExt;
import my.portfolio.prjkt.views.flashcard.FlashCardView;
import my.portfolio.prjkt.views.home.HomeView;
import my.portfolio.prjkt.views.profile.ProfileView;
import my.portfolio.prjkt.views.project.ProjectView;

import static my.portfolio.prjkt.ext.TabExt.createTab;

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

        H1 viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");
        var layout = new HorizontalLayout(new TabExt(createMenuItems()));
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

    private Tab[] createMenuItems() {
        return new Tab[]{
                createTab("Home", HomeView.class),
                createTab("Flash Card", FlashCardView.class),
                createTab("Project", ProjectView.class),
                createTab("Profile", ProfileView.class)
        };
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
