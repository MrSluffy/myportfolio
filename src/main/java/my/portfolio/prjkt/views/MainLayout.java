package my.portfolio.prjkt.views;


import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.services.impl.MyUserServiceImp;
import my.portfolio.prjkt.exceptions.AuthException;
import my.portfolio.prjkt.ext.TabExt;
import my.portfolio.prjkt.views.flashcard.FlashCardView;
import my.portfolio.prjkt.views.home.HomeView;
import my.portfolio.prjkt.views.profile.ProfileView;
import my.portfolio.prjkt.views.project.ProjectView;

import static my.portfolio.prjkt.ext.TabExt.createTab;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements Broadcaster.BroadcastListener {


    private H1 viewTitle;

    private final Dialog formDialog = new Dialog();

    TextField loginUsername1 = new TextField("Username");
    PasswordField loginPassword = new PasswordField("Password");
    Button btnRegisterForm1 = new Button("Register");
    Button btnLoginForm1 = new Button("Sign-in");

    TextField registerUsername2 = new TextField("Username");
    PasswordField registerPassword1 = new PasswordField("Password");
    PasswordField registerPassword2 = new PasswordField("Confirm Password");
    Button btnRegisterForm2 = new Button("Register");
    Button btnLoginForm2 = new Button("Sign-in");
    private final MyUserServiceImp serviceImp;

    public MainLayout(MyUserServiceImp serviceImp) {
        this.serviceImp = serviceImp;

        configureDialog();

        addToNavbar(true, createHeaderContent());

        Broadcaster.register(this);
    }

    private void configureDialog() {
        VerticalLayout formDialogLayout = configureLoginFormDialogLayout();
        formDialog.add(formDialogLayout);
        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
    }

    private VerticalLayout configureLoginFormDialogLayout() {
        FormLayout formLayout = new FormLayout();
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section();
        section.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        var layout = new VerticalLayout();
        var head = new H3("Login");
        head.addClassName("headline");

        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.add(head);
        var btnClose = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> formDialog.close());
        btnClose.addClassName("btn-dialog");
        section.add(header, btnClose);
        loginUsername1.setRequired(true);
        loginPassword.setRequired(true);
        formLayout.add(loginUsername1, loginPassword);
        var hor = new HorizontalLayout();
        formDialog.setDraggable(true);
        formDialog.setResizable(true);
        hor.setWidthFull();
        hor.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        btnLoginForm1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRegisterForm1.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnLoginForm1.addClassName("btn-dialog");
        btnLoginForm1.addClickListener(event -> {
            try {
                serviceImp.authenticate(loginUsername1.getValue(), loginPassword.getValue());
                formDialog.close();
            } catch (AuthException e) {
                Notification.show("Wrong credentials",
                                5000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        btnRegisterForm1.addClickListener(event -> {
            formDialog.close();
            var dialog = new Dialog();
            VerticalLayout registerFormLayout = configureRegisterFormDialogLayout(dialog);
            dialog.add(registerFormLayout);
            dialog.open();
        });
        var btnGuest = new Button("Sign-in as Guest");
        btnGuest.addClassName("btn-dialog");
        btnGuest.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnGuest.addClickListener(event -> loginAsGuest());
        hor.add(btnGuest, btnRegisterForm1);
        btnLoginForm1.setWidthFull();
        layout.add(section, formLayout, btnLoginForm1, hor);
        return layout;
    }

    private void loginAsGuest() {
        serviceImp.guest();
        MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);
        Notification.show("Sign-in as " + user.getUserName(),
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        formDialog.close();
    }

    private VerticalLayout configureRegisterFormDialogLayout(Dialog dialog) {
        FormLayout formLayout = new FormLayout();
        var layout = new VerticalLayout();
        var head = new H3("Register");
        head.addClassName("headline");
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setModal(false);
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section();
        section.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Button btnClose = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> dialog.close());
        btnClose.addClassName("btn-dialog");
        header.add(head, btnClose);

        section.add(header);
        registerUsername2.setRequired(true);
        registerPassword1.setRequired(true);
        registerPassword2.setRequired(true);
        registerUsername2.setHelperText("You can use letter, number or period");
        formLayout.add(registerUsername2, registerPassword1);
        var hor = new HorizontalLayout();
        hor.setWidthFull();
        hor.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        btnRegisterForm2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLoginForm2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnRegisterForm2.addClassName("btn-dialog");
        btnRegisterForm2.addClickListener(event -> {
            register(
                    registerUsername2.getValue(),
                    registerPassword1.getValue(),
                    registerPassword2.getValue(),
                    dialog);
        });
        btnLoginForm2.addClickListener( event -> {
            dialog.close();
            formDialog.open();
        });
        hor.add(btnLoginForm2, btnRegisterForm2);
        registerPassword2.setWidthFull();
        layout.add(section, formLayout, registerPassword2, hor);
        return layout;
    }

    private void register(String username, String password1, String password2, Dialog dialog) {
        if (username.trim().isEmpty()) {
            notify(username);
        } else if (!password1.equals(password2)) {
            Notification.show("Password don't match",
                            5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (serviceImp.getUserName(username).isPresent()) {
            Notification.show(username + " username is already taken",
                            5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (password1.isEmpty()) {
            notify(password1);
        } else {
            dialog.close();
            serviceImp.register(username, password1);
            Notification.show(username + " successfully register",
                            5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    private void notify(String label) {

    }


    private Component createHeaderContent() {
        H1 viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");
        TabExt tabExt = new TabExt(createMenuItems());
        tabExt.addThemeVariants(TabsVariant.LUMO_HIDE_SCROLL_BUTTONS, TabsVariant.MATERIAL_FIXED);
        tabExt.addSelectedChangeListener(event -> {
            MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);
            if (user != null) {
                formDialog.setVisible(false);
                formDialog.setEnabled(false);
                loginUsername1.setEnabled(false);
                loginPassword.setEnabled(false);
            } else {
                formDialog.open();
            }
        });
        var layout = new HorizontalLayout(tabExt);
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
