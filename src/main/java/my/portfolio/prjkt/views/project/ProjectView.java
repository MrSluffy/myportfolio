package my.portfolio.prjkt.views.project;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.services.impl.ProjectServiceImp;
import my.portfolio.prjkt.views.MainLayout;

@PageTitle("Project")
@Route(value = "project", layout = MainLayout.class)
public class ProjectView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    private final ProjectServiceImp serviceImp;

    Button btn;

    public ProjectView(ProjectServiceImp serviceImp) {
        this.serviceImp = serviceImp;
        constructUI();

        Icon icon = new Icon(VaadinIcon.PLUS);
        icon.addClassName("btn-icon");
        icon.addClassNames("size-l");

        btn = new Button(icon);


        configureProject();
    }

    private void configureProject() {
        for(Project prjkt : serviceImp.getAllProject()){
            imageContainer.add(new ImageListViewCard(prjkt.getTitlePrjkt(), prjkt.getDescriptionPrjkt(),
                    "https://www.google.com/s2/favicons?domain=www.vaadin.com", prjkt.getTypePrjkt().getName(), prjkt.getDate()));
        }

        btn.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        btn.setHeightFull();
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        imageContainer.add(btn);
    }

    private void constructUI() {
        addClassNames("image-list-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("My Project");
        header.addClassName("header-h2");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(header);

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        container.add(header);
        add(container, imageContainer);

    }
}