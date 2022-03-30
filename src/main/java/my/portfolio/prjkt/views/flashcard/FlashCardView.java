package my.portfolio.prjkt.views.flashcard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.services.impl.DeviceServiceImp;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.data.services.impl.MyUserServiceImp;
import my.portfolio.prjkt.exceptions.AuthException;
import my.portfolio.prjkt.views.MainLayout;
import my.portfolio.prjkt.views.session.LogoutView;

import java.util.List;
import java.util.stream.Collectors;

import static my.portfolio.prjkt.util.Utilities.createIconItem;


@PageTitle("Flash Card")
@Route(value = "project/flashcard", layout = MainLayout.class)
public class FlashCardView extends Main implements HasComponents, HasStyle {

    private OrderedList flashList;
    private final FlashCardServiceImp serviceImp;
    private final MyUserServiceImp myUserServiceImp;
    private final DeviceServiceImp device;

    Dialog formDialog = new Dialog();

    Dialog aboutDialog = new Dialog();


    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    TextField answerField = new TextField("Answer");
    TextField questionField = new TextField("Question");


    Button add = new Button("Add");

    Button btnadd = new Button("Add");

    Select<String> sortBy = new Select<>();

    MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);




    public FlashCardView(FlashCardServiceImp serviceImp, MyUserServiceImp myUserServiceImp, DeviceServiceImp device) {
        this.serviceImp = serviceImp;
        this.myUserServiceImp = myUserServiceImp;
        this.device = device;

        constructUI();

        configureFlashes();

        configureDialog();

    }

    private void configureDialog() {
        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        formDialog.add(dialogLayout);
        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.getElement().setAttribute("aria-label", "Create new flashcard");

    }


    private VerticalLayout createDialogLayout(Dialog formDialog) {
        H3 dialogTitle = new H3("Create flashcard");
        dialogTitle.addClassName("dialog-title");

        Header header = new Header(dialogTitle);
        header.setWidthFull();
        header.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "items-center", "w-full");

        VerticalLayout createFields = createFlashField();

        VerticalLayout scrollContent = new VerticalLayout(createFields);

        Scroller scroller = new Scroller(scrollContent);

        Footer footer = createFooter(formDialog);
        VerticalLayout dialogContent = new VerticalLayout();

        header.setHeight("90px");
        dialogContent.addClassName("db-dialog");
        footer.setHeight("70px");
        footer.setWidthFull();
        scroller.setHeight("400px");
        footer.addClassNames("bg-contrast-5", "flex", "items-center", "w-full");
        dialogContent.add(header, scroller, footer);
        dialogContent.setSpacing(false);
        dialogContent.setPadding(false);
        dialogContent.getStyle().remove("width");
        dialogContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogContent.setClassName("dialog-no-padding-example-overlay");
        dialogContent.getStyle().set("width", "380px").set("max-width", "100%");

        return dialogContent;
    }

    private VerticalLayout createFlashField() {
        titleField.setWidthFull();
        titleField.setLabel("Title");
        titleField.setRequired(true);

        answerField.setWidthFull();
        answerField.setRequired(true);

        questionField.setWidthFull();
        questionField.setRequired(true);

        descriptionField.setWidthFull();
        descriptionField.setLabel("Details");
        descriptionField.setRequired(true);
        descriptionField.setMaxLength(500);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 500));

        urlField.setWidthFull();
        urlField.setRequired(true);
        urlField.setLabel("Source");
        urlField.setHelperText("Example: https://www.github.com/source");

        VerticalLayout section = new VerticalLayout(titleField,
                descriptionField, urlField, questionField, answerField);
        section.setPadding(false);
        section.setSpacing(false);
        section.setAlignItems(FlexComponent.Alignment.STRETCH);
        section.getElement().setAttribute("role", "region");
        return section;
    }


    private Footer createFooter(Dialog dialog) {

        add.addClassName("btn-dialog");

        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add.addClickListener(buttonClickEvent -> {
            if(!titleField.isEmpty() || !descriptionField.isEmpty() || !urlField.isEmpty() || !answerField.isEmpty() || !questionField.isEmpty()){
                addNewFlashCard(titleField.getValue(), descriptionField.getValue(), urlField.getValue(), answerField.getValue(), questionField.getValue());
            } else {
                Notification.show("Field cannot be empty",
                        5000,
                        Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                add);
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.addClassName("footer");

        return new Footer(buttonLayout);
    }


    private void addNewFlashCard(String title, String detail, String reference, String answer, String question) {
        try {
            serviceImp.save(title, detail, reference, answer, question);
            formDialog.close();
            flashList.removeAll();
            configureFlashes();
        } catch (AuthException e) {
            Notification.show("You need to sign in as Guest or Create a new account",
                    5000,
                    Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }


    public void configureFlashes() {

        List<FlashCard> flashCardList = serviceImp.findAll(sortBy.getValue());

        for(FlashCard flashCard : flashCardList){

            flashList.add(new FlashCardListItem(
                    flashCard.getId(),
                    flashCard.getCardTitle(),
                    flashCard.getCardDetail(),
                    flashCard.getCardReference(),
                    flashCard.getCardDate(),
                    flashCard.getCarqQuestion(),
                    flashCard.getCardAnswer(),
                    flashCard.getMyUserInFlashCard().getUserName(),
                    flashCard.isCorrect(),
                    flashCard.getCardNumber(),
                    flashCard.getUserListCorrectAnswer()
                            .stream()
                            .map(MyUser::getUserName)
                            .collect(Collectors.toList()),
                    device,
                    serviceImp));
        }

        btnadd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnadd.addClickListener( event -> formDialog.open());

        Icon icon = new Icon(VaadinIcon.PLUS);
        icon.addClassName("btn-icon");
        icon.addClassNames("size-l");

        var btn = new Button(icon);

        btn.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        btn.setHeightFull();
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        btn.addClickListener(event -> formDialog.open());
        flashList.add(btn);
        btnadd.setVisible(serviceImp.flashCardCount() > 4);
        btn.setVisible(serviceImp.flashCardCount() <= 4);
        sortBy.setVisible(serviceImp.flashCardCount() > 2);
    }

    private void constructUI() {
        addClassNames("image-list-view", "pb-l", "px-l");
        addClassName("view-card");
        HorizontalLayout container = getFirstHeader();
        HorizontalLayout con = getSecHeader();
        Label header = new Label("Flashes");
        Paragraph sub = new Paragraph("Review your knowledge instantly");
        sub.addClassName("sub-header");
        sub.addClassNames("text-xl");

        header.addClassName("header-h2");
        header.addClassNames("mb-0", "mt-l", "text-3xl");

        flashList = new OrderedList();
        flashList.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        flashList.addClassName("flashcard");

        configureAboutDialog();

        ComponentEventListener<ClickEvent<MenuItem>> listenerScore = e -> {
        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerAbout = e -> {
            aboutDialog.open();

        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerLogout = e -> new LogoutView();

        MenuBar menuBar = getMenuBar(listenerScore, listenerAbout, listenerLogout);

        container.add(header, menuBar);
        con.add(sortBy, btnadd);
        VerticalLayout headerContainer = getHeaderContainer(container, con, sub);

        add(headerContainer, flashList);

        configureSort();
    }

    private void configureAboutDialog() {
        VerticalLayout dialogLayout = createAboutDialogLayout(aboutDialog);
        dialogLayout.addClassNames("max-w-screen-lg", "mx-auto");
        dialogLayout.addClassName("about-dialog");
        aboutDialog.add(dialogLayout);
        aboutDialog.setDraggable(true);
        aboutDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        aboutDialog.getElement().setAttribute("aria-label", "About");
    }

    private VerticalLayout createAboutDialogLayout(Dialog aboutDialog) {
        var layout = new VerticalLayout();
        H4 head = new H4("About Flashcard");
        head.addClassName("sub-header");


        layout.setSpacing(false);
        var headerLayout = new HorizontalLayout();
        headerLayout.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "items-center", "w-full");
        Anchor spring = new Anchor("", "Spring boot");
        spring.addClassNames("text-l");
        spring.setHref("https://spring.io/projects/spring-boot");
        Anchor github = new Anchor("", "source");
        github.addClassNames("text-l");
        github.setHref("https://github.com/MrSluffy/myportfolio");
        Anchor vaadin = new Anchor("", "Vaadin");
        vaadin.addClassNames("text-l");
        vaadin.setHref("https://vaadin.com/docs/latest/");
        headerLayout.add(head);
        Paragraph sub = new Paragraph("Flash Card is a mixed of To-Do application and Quiz Application with a twist." +
                " I create this to help myself to review and test my knowledge and I hope you can find it helpful too" +
                " because I'm quite a bit forgetful HAHA. This project is created using ");
        sub.addClassNames("text-l");
        sub.add(spring);
        sub.add(" for the backend and ");
        sub.add(vaadin);
        sub.add(" framework for the frontend.");
        sub.add(" GitHub : ");
        sub.add(github);

        var closeBtn = new Button("Close");
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeBtn.addClickListener(e-> aboutDialog.close());
        var footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.add(closeBtn);
        layout.add(headerLayout, sub, footer);
        layout.getStyle().set("width", "360px").set("max-width", "100%");

        return layout;
    }

    private VerticalLayout getHeaderContainer(HorizontalLayout container, HorizontalLayout con, Paragraph sub) {
        VerticalLayout headerContainer = new VerticalLayout();
        headerContainer.setSpacing(false);
        headerContainer.setPadding(false);
        headerContainer.add(container, sub, con);
        return headerContainer;
    }

    private void configureSort() {
        sortBy.setLabel("Sort by");
        sortBy.setItems("Ascending", "Descending", "Default");
        sortBy.setValue("Default");
        sortBy.addValueChangeListener( e-> {
            flashList.removeAll();
            configureFlashes();
        });
    }

    private HorizontalLayout getFirstHeader() {
        HorizontalLayout container = new HorizontalLayout();
        container.setWidthFull();
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        container.addClassNames("flex-wrap", "items-center", "justify-between");
        container.setPadding(false);
        container.setSpacing(false);
        return container;
    }

    private HorizontalLayout getSecHeader() {
        var con = new HorizontalLayout();
        con.addClassNames("items-center", "justify-between");
        con.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        con.setWidthFull();
        return con;
    }

    private MenuBar getMenuBar(ComponentEventListener<ClickEvent<MenuItem>> listenerScore,
                               ComponentEventListener<ClickEvent<MenuItem>> listenerAbout,
                               ComponentEventListener<ClickEvent<MenuItem>> listenerLogout) {
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("flash-container");
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        menuBar.setOpenOnHover(true);
        MenuItem item = createIconItem(menuBar, VaadinIcon.ELLIPSIS_DOTS_V, "more");
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Your Score", listenerScore);
        if(user != null){
            subMenu.addItem("Logout", listenerLogout);
            subMenu.add(new Hr());
        }
        subMenu.addItem("About", listenerAbout);

        return menuBar;
    }
}
